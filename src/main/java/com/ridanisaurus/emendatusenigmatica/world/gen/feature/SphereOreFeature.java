package com.ridanisaurus.emendatusenigmatica.world.gen.feature;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.common.CommonBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sample.SampleBlockDefinitionModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.sphere.SphereDepositModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.registries.EETags;
import com.ridanisaurus.emendatusenigmatica.util.MathHelper;
import com.ridanisaurus.emendatusenigmatica.world.gen.feature.config.SphereOreFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
//import net.minecraft.world.level.material.Material;

import java.util.ArrayList;

public class SphereOreFeature extends Feature<SphereOreFeatureConfig> {
    private final EmendatusDataRegistry registry;
    //NOTE: Here was a placed boolean.
    // It was moved to the configuration object.
    // No idea what its purpose was, so it's config-dependent.

    public SphereOreFeature() {
        super(SphereOreFeatureConfig.CODEC);
        this.registry = EmendatusEnigmatica.getInstance().getDataRegistry();
    }

    @Override
    public boolean place(FeaturePlaceContext<SphereOreFeatureConfig> context) {
        RandomSource rand = context.random();
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        var config = context.config();
        var model = config.model;

        int yTop = model.getMaxYLevel();
        int yBottom = model.getMinYLevel();

        int yPos = rand.nextInt(yTop);
        yPos = Math.max(yPos, yBottom);
        // TODO: Fix the radius calculation
        int radius = model.getRadius();

        // Isn't this like, the same as not adding anything?
        radius += 0.5;
        radius += 0.5;
        radius += 0.5;

        final double invRadiusX = 1d / radius;
        final double invRadiusY = 1d / radius;
        final double invRadiusZ = 1d / radius;

        // CeilRadius is the same as radius? Radius is int!
        final int ceilRadiusX = (int) Math.ceil(radius);
        final int ceilRadiusY = (int) Math.ceil(radius);
        final int ceilRadiusZ = (int) Math.ceil(radius);

        double nextXn = 0;
        forX:
        for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY:
            for (int y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ:
                for (int z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = MathHelper.lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }
                    if (y + yPos > yTop || y + yPos < yBottom) {
                        continue;
                    }
                    int randomizer = Math.random() >= 0.5D ? 1 : 0;
                    placeBlock(level, rand, new BlockPos(pos.getX() + x + randomizer, yPos + y + randomizer, pos.getZ() + z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + -x + randomizer, yPos + y + randomizer, pos.getZ() + z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + x + randomizer, yPos + -y + randomizer, pos.getZ() + z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + x + randomizer, yPos + y + randomizer, pos.getZ() + -z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + -x + randomizer, yPos + -y + randomizer, pos.getZ() + z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + x + randomizer, yPos + -y + randomizer, pos.getZ() + -z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + -x + randomizer, yPos + y + randomizer, pos.getZ() + -z + randomizer), config);
                    placeBlock(level, rand, new BlockPos(pos.getX() + -x + randomizer, yPos + -y + randomizer, pos.getZ() + -z + randomizer), config);
                }
            }
        }
        if (rand.nextInt(100) < model.getChance() && !config.sampleBlocks.isEmpty()) {
            placeSurfaceSample(rand, pos, level, config);
        }
        return true;
    }

    private void placeBlock(WorldGenLevel level, RandomSource rand, BlockPos pos, SphereOreFeatureConfig config) {
        if (!config.target.test(level.getBlockState(pos), rand)) {
            return;
        }

        int index = rand.nextInt(config.blocks.size());
        try {
            CommonBlockDefinitionModel commonBlockDefinitionModel = config.blocks.get(index);
            if (commonBlockDefinitionModel.getBlock() != null) {
                Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(commonBlockDefinitionModel.getBlock()));
                level.setBlock(pos, block.defaultBlockState(), 2);
            } else if (commonBlockDefinitionModel.getTag() != null) {
                HolderSet.Named<Block> blockITag = BuiltInRegistries.BLOCK.getTag(EETags.getBlockTag(ResourceLocation.parse(commonBlockDefinitionModel.getTag()))).get();
                blockITag.getRandomElement(rand).ifPresent(block -> {
                    level.setBlock(pos, block.value().defaultBlockState(), 2);
                });
            } else if (commonBlockDefinitionModel.getMaterial() != null) {
                BlockState currentFiller = level.getBlockState(pos);
                String fillerId = BuiltInRegistries.BLOCK.getKey(currentFiller.getBlock()).toString();
                Integer strataIndex = registry.getStrataByIndex().getOrDefault(fillerId, null);
                if (strataIndex != null) {
                    StrataModel stratum = registry.getStrata().get(strataIndex);
                    Block block = EERegistrar.oreBlockTable.get(stratum.getId(), commonBlockDefinitionModel.getMaterial()).get();
                    level.setBlock(pos, block.defaultBlockState(), 2);
                }
            }
            config.placed = true;
        } catch (Exception e) {
            JsonElement modelJson = JsonOps.INSTANCE.withEncoder(SphereDepositModel.CODEC).apply(config.model).result().get();
            EmendatusEnigmatica.logger.error("index: " + index + ", model: " + new Gson().toJson(modelJson), e);
        }
    }

    private void placeSampleBlock(WorldGenLevel level, RandomSource rand, BlockPos samplePos, SphereOreFeatureConfig config) {
        try {
            int index = rand.nextInt(config.sampleBlocks.size());
            SampleBlockDefinitionModel sampleBlockDefinitionModel = config.sampleBlocks.get(index);

            if (sampleBlockDefinitionModel.getBlock() != null) {
                Block sampleBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(sampleBlockDefinitionModel.getBlock()));
                level.setBlock(samplePos, sampleBlock.defaultBlockState(), 2);
            } else if (sampleBlockDefinitionModel.getTag() != null) {
                HolderSet.Named<Block> blockITag = BuiltInRegistries.BLOCK.getTag(EETags.getBlockTag(ResourceLocation.parse(sampleBlockDefinitionModel.getTag()))).get();
                blockITag.getRandomElement(rand).ifPresent(block -> {
                    level.setBlock(samplePos, block.value().defaultBlockState(), 2);
                });
            } else if (sampleBlockDefinitionModel.getMaterial() != null) {
                Block sampleBlock = EERegistrar.oreSampleBlockTable.get(sampleBlockDefinitionModel.getStrata(), sampleBlockDefinitionModel.getMaterial()).get();
                level.setBlock(samplePos, sampleBlock.defaultBlockState(), 2);
            }
        } catch (Exception e) {
            JsonElement modelJson = JsonOps.INSTANCE.withEncoder(SphereDepositModel.CODEC).apply(config.model).result().get();
            EmendatusEnigmatica.logger.error("model: " + new Gson().toJson(modelJson), e);
        }
    }

    private void placeSurfaceSample(RandomSource rand, BlockPos pos, WorldGenLevel level, SphereOreFeatureConfig config) {
        BlockPos sample = new BlockPos(pos.getX(), level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()), pos.getZ());
        if (level.getBlockState(sample.below()).getBlock() == Blocks.WATER) {
            sample = new BlockPos(pos.getX(), level.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ()), pos.getZ());
        }
        if (sample.getY() > level.getMinBuildHeight() + 3 && level.getBlockState(sample.below()).is(BlockTags.LEAVES)) {
            for(int l = 0; l < 3; ++l) {
                int i = rand.nextInt(2);
                int j = rand.nextInt(2);
                int k = rand.nextInt(2);
                float f = (float)(i + j + k) * 0.333F + 0.5F;

                for(BlockPos samplePos : BlockPos.betweenClosed(sample.offset(-i, -j, -k), sample.offset(i, j, k))) {
                    if (samplePos.distSqr(sample) <= (double)(f * f) && config.placed) {
                        placeSampleBlock(level, rand, samplePos, config);
                    }
                }
                sample = sample.offset(-1 + rand.nextInt(2), -rand.nextInt(2), -1 + rand.nextInt(2));
            }
        }
        config.placed = false;
    }
}