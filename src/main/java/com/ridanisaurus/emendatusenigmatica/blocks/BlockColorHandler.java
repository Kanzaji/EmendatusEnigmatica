package com.ridanisaurus.emendatusenigmatica.blocks;



import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockColorHandler implements BlockColor {

    @Override
    public int getColor(BlockState state, @Nullable BlockAndTintGetter reader, @Nullable BlockPos pos, int layer) {
        Block block = state.getBlock();
        if (layer == 0) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getHighlight2();
            }
            switch (block) {
                case BasicStorageBlock basicStorageBlock -> {
                    return basicStorageBlock.getHighlight2();
                }
                case BasicWeatheringBlock basicWeatheringBlock -> {
                    return basicWeatheringBlock.getHighlight2();
                }
                case BasicWaxedBlock basicWaxedBlock -> {
                    return basicWaxedBlock.getHighlight2();
                }
                case BasicClusterBlock basicClusterBlock -> {
                    return basicClusterBlock.getHighlight2();
                }
                case BasicClusterShardBlock basicClusterShardBlock -> {
                    return basicClusterShardBlock.getHighlight2();
                }
                case BasicBuddingBlock basicBuddingBlock -> {
                    return basicBuddingBlock.getHighlight2();
                }
                default -> {
                }
            }
        }
        if (layer == 1) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getHighlight1();
            }
            switch (block) {
                case BasicStorageBlock basicStorageBlock -> {
                    return basicStorageBlock.getHighlight1();
                }
                case BasicWeatheringBlock basicWeatheringBlock -> {
                    return basicWeatheringBlock.getHighlight1();
                }
                case BasicWaxedBlock basicWaxedBlock -> {
                    return basicWaxedBlock.getHighlight1();
                }
                case BasicClusterBlock basicClusterBlock -> {
                    return basicClusterBlock.getHighlight1();
                }
                case BasicClusterShardBlock basicClusterShardBlock -> {
                    return basicClusterShardBlock.getHighlight1();
                }
                case BasicBuddingBlock basicBuddingBlock -> {
                    return basicBuddingBlock.getHighlight1();
                }
                default -> {
                }
            }
        }
        if (layer == 2) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getBase();
            }
            switch (block) {
                case BasicStorageBlock basicStorageBlock -> {
                    return basicStorageBlock.getBase();
                }
                case BasicWeatheringBlock basicWeatheringBlock -> {
                    return basicWeatheringBlock.getBase();
                }
                case BasicWaxedBlock basicWaxedBlock -> {
                    return basicWaxedBlock.getBase();
                }
                case BasicClusterBlock basicClusterBlock -> {
                    return basicClusterBlock.getBase();
                }
                case BasicClusterShardBlock basicClusterShardBlock -> {
                    return basicClusterShardBlock.getBase();
                }
                case BasicBuddingBlock basicBuddingBlock -> {
                    return basicBuddingBlock.getBase();
                }
                default -> {
                }
            }
        }
        if (layer == 3) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getShadow1();
            }
            switch (block) {
                case BasicStorageBlock basicStorageBlock -> {
                    return basicStorageBlock.getShadow1();
                }
                case BasicWeatheringBlock basicWeatheringBlock -> {
                    return basicWeatheringBlock.getShadow1();
                }
                case BasicWaxedBlock basicWaxedBlock -> {
                    return basicWaxedBlock.getShadow1();
                }
                case BasicClusterBlock basicClusterBlock -> {
                    return basicClusterBlock.getShadow1();
                }
                case BasicClusterShardBlock basicClusterShardBlock -> {
                    return basicClusterShardBlock.getShadow1();
                }
                case BasicBuddingBlock basicBuddingBlock -> {
                    return basicBuddingBlock.getShadow1();
                }
                default -> {
                }
            }
        }
        if (layer == 4) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getShadow2();
            }
            switch (block) {
                case BasicStorageBlock basicStorageBlock -> {
                    return basicStorageBlock.getShadow2();
                }
                case BasicWeatheringBlock basicWeatheringBlock -> {
                    return basicWeatheringBlock.getShadow2();
                }
                case BasicWaxedBlock basicWaxedBlock -> {
                    return basicWaxedBlock.getShadow2();
                }
                case BasicClusterBlock basicClusterBlock -> {
                    return basicClusterBlock.getShadow2();
                }
                case BasicClusterShardBlock basicClusterShardBlock -> {
                    return basicClusterShardBlock.getShadow2();
                }
                case BasicBuddingBlock basicBuddingBlock -> {
                    return basicBuddingBlock.getShadow2();
                }
                default -> {
                }
            }
        }
        if (layer == 9) {
            if (block instanceof BasicWeatheringBlock) {
                return ((BasicWeatheringBlock) block).getOxidizationColor();
            }
            if (block instanceof BasicWaxedBlock) {
                return ((BasicWaxedBlock) block).getOxidizationColor();
            }
        }
        return 0xFFFFFF;
    }
}