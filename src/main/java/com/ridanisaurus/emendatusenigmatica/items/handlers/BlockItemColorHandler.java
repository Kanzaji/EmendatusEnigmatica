package com.ridanisaurus.emendatusenigmatica.items.handlers;

import com.ridanisaurus.emendatusenigmatica.blocks.handlers.IColorable;
import com.ridanisaurus.emendatusenigmatica.blocks.templates.*;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockItemColorHandler implements ItemColor {
    @Override
    public int getColor(@NotNull ItemStack stack, int layer) {
        if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof IColorable colorable) return switch (layer) {
            case 0 -> colorable.getHighlight2();
            case 1 -> colorable.getHighlight1();
            case 2 -> colorable.getBase();
            case 3 -> colorable.getShadow1();
            case 4 -> colorable.getShadow2();
            case 9 -> {
                if (item.getBlock() instanceof BasicWeatheringBlock oxidizedBlock) yield oxidizedBlock.getOxidizationColor();
                if (item.getBlock() instanceof BasicWaxedBlock oxidizedBlock) yield oxidizedBlock.getOxidizationColor();
                yield 0xFFFFFF;
            }
            default -> 0xFFFFFF;
        };
        return 0xFFFFFF;
    }
}
