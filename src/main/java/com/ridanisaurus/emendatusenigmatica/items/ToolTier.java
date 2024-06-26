package com.ridanisaurus.emendatusenigmatica.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.function.Supplier;

//TODO: UNFINISHED
public class ToolTier implements Tier
{

    private final int lvl;
    private final int uses;
    private final float efficiency;
    private final float attackDmg;
    private final int enchantability;
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


    public ToolTier(int lvl, int uses, float speed, float attackDmgBonus, int enchantability, @Nullable Supplier<Ingredient> repairIngredient) {
        this.lvl = lvl;
        this.uses = uses;
        this.efficiency = speed;
        this.attackDmg = attackDmgBonus;
        this.enchantability = enchantability;
        this.tag = tag;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public float getAttackDamageBonus() {
        return 0;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return null;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return null;
    }
}
