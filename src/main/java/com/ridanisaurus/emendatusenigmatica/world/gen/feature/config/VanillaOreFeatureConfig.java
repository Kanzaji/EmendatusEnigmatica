package com.ridanisaurus.emendatusenigmatica.world.gen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.api.EmendatusDataRegistry;
import com.ridanisaurus.emendatusenigmatica.plugin.model.StrataModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.deposit.vanilla.VanillaDepositModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;

public class VanillaOreFeatureConfig implements FeatureConfiguration {
    public static final Codec<VanillaOreFeatureConfig> CODEC = RecordCodecBuilder.create((x) -> x.group(
        VanillaDepositModel.CODEC.fieldOf("model").forGetter(it -> it.model)
    ).apply(x, VanillaOreFeatureConfig::new));
    private final EmendatusDataRegistry registry;
    public final VanillaDepositModel model;
    public final Lazy<List<OreConfiguration.TargetBlockState>> targets;

    public VanillaOreFeatureConfig(VanillaDepositModel model) {
        this.model = model;
        // CODEC Requires acquiring DataRegistry from the Loader Instance.
        this.registry = EmendatusEnigmatica.getInstance().getDataRegistry();
        this.targets = Lazy.of(this::createTargetStateList);
    }

    private List<OreConfiguration.TargetBlockState> createTargetStateList() {
        List<OreConfiguration.TargetBlockState> states = new ArrayList<>();

        //TODO: Find a good way of checking if the block was found in the registry. It now returns a default object instead of null.
        if (this.model.getBlock() != null) {
            Block oreBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(model.getBlock()));
            if (oreBlock == null) {
                EmendatusEnigmatica.logger.warn("Unable to find {} in the registries.", model.getBlock());
                return states; // Empty, something went wrong
            }

            for (StrataModel stratum : registry.getStrata()) {
                if (model.getFillerTypes().contains(stratum.getId())) {
                    Block stratumBlock = BuiltInRegistries.BLOCK.get(stratum.getFillerType());
                    if (stratumBlock == null) {
                        EmendatusEnigmatica.logger.warn("Unable to find {} in the registries.", stratum.getFillerType());
                        continue;
                    }

                    states.add(OreConfiguration.target(new BlockMatchTest(stratumBlock), oreBlock.defaultBlockState()));
                }
            }

            return states;
        }

        for (MaterialModel material : registry.getMaterials()) {
            if (!material.getId().equals(model.getMaterial())) continue;

            for (StrataModel stratum : registry.getStrata()) {
                if (!model.getFillerTypes().contains(stratum.getId())) continue;

                Block stratumBlock = BuiltInRegistries.BLOCK.get(stratum.getFillerType());
                if (stratumBlock == null) {
                    EmendatusEnigmatica.logger.warn("Unable to find {} in forge registries", stratum.getFillerType());
                    continue;
                }

                DeferredBlock<Block> blockRegistryObject = EERegistrar.oreBlockTable.get(stratum.getId(), material.getId());
                if (blockRegistryObject == null) {
                    EmendatusEnigmatica.logger.warn("Unable to find the combination of {} and {} in the ore block table", stratum.getId(), material.getId());
                    continue;
                }

                BlockState oreBlockstate = blockRegistryObject.get().defaultBlockState();
                states.add(OreConfiguration.target(new BlockMatchTest(stratumBlock), oreBlockstate));
            }
        }

        return states;
    }
}
