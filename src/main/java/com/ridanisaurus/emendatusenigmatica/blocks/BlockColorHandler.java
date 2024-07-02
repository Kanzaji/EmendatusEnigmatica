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
            if (block instanceof BasicStorageBlock) {
                return ((BasicStorageBlock) block).getHighlight2();
            }
            if (block instanceof BasicWeatheringBlock) {
                return ((BasicWeatheringBlock) block).getHighlight2();
            }
            if (block instanceof BasicWaxedBlock) {
                return ((BasicWaxedBlock) block).getHighlight2();
            }
            if (block instanceof BasicClusterBlock) {
                return ((BasicClusterBlock) block).getHighlight2();
            }
            if (block instanceof BasicClusterShardBlock) {
                return ((BasicClusterShardBlock) block).getHighlight2();
            }
            if (block instanceof BasicBuddingBlock) {
                return ((BasicBuddingBlock) block).getHighlight2();
            }
        }
        if (layer == 1) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getHighlight1();
            }
            if (block instanceof BasicStorageBlock) {
                return ((BasicStorageBlock) block).getHighlight1();
            }
            if (block instanceof BasicWeatheringBlock) {
                return ((BasicWeatheringBlock) block).getHighlight1();
            }
            if (block instanceof BasicWaxedBlock) {
                return ((BasicWaxedBlock) block).getHighlight1();
            }
            if (block instanceof BasicClusterBlock) {
                return ((BasicClusterBlock) block).getHighlight1();
            }
            if (block instanceof BasicClusterShardBlock) {
                return ((BasicClusterShardBlock) block).getHighlight1();
            }
            if (block instanceof BasicBuddingBlock) {
                return ((BasicBuddingBlock) block).getHighlight1();
            }
        }
        if (layer == 2) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getBase();
            }
            if (block instanceof BasicStorageBlock) {
                return ((BasicStorageBlock) block).getBase();
            }
            if (block instanceof BasicWeatheringBlock) {
                return ((BasicWeatheringBlock) block).getBase();
            }
            if (block instanceof BasicWaxedBlock) {
                return ((BasicWaxedBlock) block).getBase();
            }
            if (block instanceof BasicClusterBlock) {
                return ((BasicClusterBlock) block).getBase();
            }
            if (block instanceof BasicClusterShardBlock) {
                return ((BasicClusterShardBlock) block).getBase();
            }
            if (block instanceof BasicBuddingBlock) {
                return ((BasicBuddingBlock) block).getBase();
            }
        }
        if (layer == 3) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getShadow1();
            }
            if (block instanceof BasicStorageBlock) {
                return ((BasicStorageBlock) block).getShadow1();
            }
            if (block instanceof BasicWeatheringBlock) {
                return ((BasicWeatheringBlock) block).getShadow1();
            }
            if (block instanceof BasicWaxedBlock) {
                return ((BasicWaxedBlock) block).getShadow1();
            }
            if (block instanceof BasicClusterBlock) {
                return ((BasicClusterBlock) block).getShadow1();
            }
            if (block instanceof BasicClusterShardBlock) {
                return ((BasicClusterShardBlock) block).getShadow1();
            }
            if (block instanceof BasicBuddingBlock) {
                return ((BasicBuddingBlock) block).getShadow1();
            }
        }
        if (layer == 4) {
            if (block instanceof GemOreBlock || block instanceof MetalOreBlock || block instanceof GemOreBlockWithParticles || block instanceof  MetalOreBlockWithParticles || block instanceof SampleOreBlock || block instanceof SampleOreBlockWithParticles) {
                return ((IColorable) block).getShadow2();
            }
            if (block instanceof BasicStorageBlock) {
                return ((BasicStorageBlock) block).getShadow2();
            }
            if (block instanceof BasicWeatheringBlock) {
                return ((BasicWeatheringBlock) block).getShadow2();
            }
            if (block instanceof BasicWaxedBlock) {
                return ((BasicWaxedBlock) block).getShadow2();
            }
            if (block instanceof BasicClusterBlock) {
                return ((BasicClusterBlock) block).getShadow2();
            }
            if (block instanceof BasicClusterShardBlock) {
                return ((BasicClusterShardBlock) block).getShadow2();
            }
            if (block instanceof BasicBuddingBlock) {
                return ((BasicBuddingBlock) block).getShadow2();
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