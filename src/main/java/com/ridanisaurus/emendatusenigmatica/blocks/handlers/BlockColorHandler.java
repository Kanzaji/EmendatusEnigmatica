package com.ridanisaurus.emendatusenigmatica.blocks.handlers;

import com.ridanisaurus.emendatusenigmatica.blocks.templates.BasicWaxedBlock;
import com.ridanisaurus.emendatusenigmatica.blocks.templates.BasicWeatheringBlock;
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
        if (block instanceof IColorable colorable) return switch (layer) {
            case 0 -> colorable.getHighlight2();
            case 1 -> colorable.getHighlight1();
            case 2 -> colorable.getBase();
            case 3 -> colorable.getShadow1();
            case 4 -> colorable.getShadow2();
            case 9 -> {
                if (block instanceof BasicWeatheringBlock oxidizedBlock) yield oxidizedBlock.getOxidizationColor();
                if (block instanceof BasicWaxedBlock oxidizedBlock) yield oxidizedBlock.getOxidizationColor();
                yield 0xFFFFFF;
            }
            default -> 0xFFFFFF;
        };
        return 0xFFFFFF;
    }
}