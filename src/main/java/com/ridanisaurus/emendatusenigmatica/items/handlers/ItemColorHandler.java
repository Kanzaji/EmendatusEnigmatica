/*
 * MIT License
 *
 * Copyright (c) 2024. Ridanisaurus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.items.handlers;

import com.ridanisaurus.emendatusenigmatica.items.templates.*;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemColorHandler implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int layer) {
        Item item = stack.getItem();
        if (layer == 0) {
            switch (item) {
                case BasicItem basicItem -> {
                    return basicItem.highlight2;
                }
                case BasicSwordItem basicSwordItem -> {
                    return basicSwordItem.highlight2;
                }
                case BasicPickaxeItem basicPickaxeItem -> {
                    return basicPickaxeItem.highlight2;
                }
                case BasicAxeItem basicAxeItem -> {
                    return basicAxeItem.highlight2;
                }
                case BasicShovelItem basicShovelItem -> {
                    return basicShovelItem.highlight2;
                }
                case BasicHoeItem basicHoeItem -> {
                    return basicHoeItem.highlight2;
                }
                case BasicPaxelItem basicPaxelItem -> {
                    return basicPaxelItem.highlight2;
                }
                case BasicArmorItem basicArmorItem -> {
                    return basicArmorItem.highlight2;
                }
                default -> {
                }
            }
        }
        if (layer == 1) {
            switch (item) {
                case BasicItem basicItem -> {
                    return basicItem.highlight1;
                }
                case BasicSwordItem basicSwordItem -> {
                    return basicSwordItem.highlight1;
                }
                case BasicPickaxeItem basicPickaxeItem -> {
                    return basicPickaxeItem.highlight1;
                }
                case BasicAxeItem basicAxeItem -> {
                    return basicAxeItem.highlight1;
                }
                case BasicShovelItem basicShovelItem -> {
                    return basicShovelItem.highlight1;
                }
                case BasicHoeItem basicHoeItem -> {
                    return basicHoeItem.highlight1;
                }
                case BasicPaxelItem basicPaxelItem -> {
                    return basicPaxelItem.highlight1;
                }
                case BasicArmorItem basicArmorItem -> {
                    return basicArmorItem.highlight1;
                }
                default -> {
                }
            }
        }
        if (layer == 2) {
            switch (item) {
                case BasicItem basicItem -> {
                    return basicItem.base;
                }
                case BasicSwordItem basicSwordItem -> {
                    return basicSwordItem.base;
                }
                case BasicPickaxeItem basicPickaxeItem -> {
                    return basicPickaxeItem.base;
                }
                case BasicAxeItem basicAxeItem -> {
                    return basicAxeItem.base;
                }
                case BasicShovelItem basicShovelItem -> {
                    return basicShovelItem.base;
                }
                case BasicHoeItem basicHoeItem -> {
                    return basicHoeItem.base;
                }
                case BasicPaxelItem basicPaxelItem -> {
                    return basicPaxelItem.base;
                }
                case BasicArmorItem basicArmorItem -> {
                    return basicArmorItem.base;
                }
                default -> {
                }
            }
        }
        if (layer == 3) {
            switch (item) {
                case BasicItem basicItem -> {
                    return basicItem.shadow1;
                }
                case BasicSwordItem basicSwordItem -> {
                    return basicSwordItem.shadow1;
                }
                case BasicPickaxeItem basicPickaxeItem -> {
                    return basicPickaxeItem.shadow1;
                }
                case BasicAxeItem basicAxeItem -> {
                    return basicAxeItem.shadow1;
                }
                case BasicShovelItem basicShovelItem -> {
                    return basicShovelItem.shadow1;
                }
                case BasicHoeItem basicHoeItem -> {
                    return basicHoeItem.shadow1;
                }
                case BasicPaxelItem basicPaxelItem -> {
                    return basicPaxelItem.shadow1;
                }
                case BasicArmorItem basicArmorItem -> {
                    return basicArmorItem.shadow1;
                }
                default -> {
                }
            }
        }
        if (layer == 4) {
            switch (item) {
                case BasicItem basicItem -> {
                    return basicItem.shadow2;
                }
                case BasicSwordItem basicSwordItem -> {
                    return -1;
                }
                case BasicPickaxeItem basicPickaxeItem -> {
                    return -1;
                }
                case BasicAxeItem basicAxeItem -> {
                    return -1;
                }
                case BasicShovelItem basicShovelItem -> {
                    return -1;
                }
                case BasicHoeItem basicHoeItem -> {
                    return -1;
                }
                case BasicPaxelItem basicPaxelItem -> {
                    return -1;
                }
                case BasicArmorItem basicArmorItem -> {
                    return basicArmorItem.shadow2;
                }
                default -> {
                }
            }
        }

        return 0xFFFFFF;
    }
}