/*
 *  MIT License
 *
 *  Copyright (c) 2020 Ridanisaurus
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

//FIXME: Tier interface has changed, some methods are no longer exist.
public class ToolTier implements Tier
{

    // Method "getLevel()" no longer exists from Tier.
    private final int lvl;
    private final int uses;
    private final float efficiency;
    private final float attackDmg;
    private final int enchantability;
    // Method "getTag()" no longer exists from Tier.
    @Nullable
    private final TagKey<Block> tag;
    @Nullable
    private final Supplier<Ingredient> repairIngredient;

    public ToolTier(int lvl, int uses, float efficiency, float attackDmg, int enchantability, @Nullable TagKey<Block> tag, @Nullable Supplier<Ingredient> repairIngredient) {
        this.lvl = lvl;
        this.uses = uses;
        this.efficiency = efficiency;
        this.attackDmg = attackDmg;
        this.enchantability = enchantability;
        this.tag = tag;
        this.repairIngredient = repairIngredient;
    }

    /*
    Originally there was a second constructor that got the Tag differently, but it cannot be retrieved from Tier.super.getTag() anymore.
     */

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDmg;
    }

    @Override
    public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
        return null;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    // Unsure what this is for, originally it contained text that looked important for Forge.
    @Override
    public String toString() {
        return super.toString();
    }
}
