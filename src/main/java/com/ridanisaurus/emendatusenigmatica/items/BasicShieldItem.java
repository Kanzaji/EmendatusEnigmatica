package com.ridanisaurus.emendatusenigmatica.items;

import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.renderers.ShieldTextureRenderer;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BasicShieldItem extends ShieldItem
{
    public final int highlight2;
    public final int highlight1;
    public final int base;
    public final int shadow1;
    public final int shadow2;
    public final TagKey<Item> repairItem;
    public final MaterialModel material;

    public BasicShieldItem(MaterialModel material, TagKey<Item> repairItem) {
        super(new Properties().durability(material.getArmor().getShield().getDurability()));
        this.highlight2 = material.getColors().getHighlightColor(3);
        this.highlight1 = material.getColors().getHighlightColor(1);
        this.base = material.getColors().getMaterialColor();
        this.shadow1 = material.getColors().getShadowColor(1);
        this.shadow2 = material.getColors().getShadowColor(2);
        this.repairItem = repairItem;
        this.material = material;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions()
        {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ShieldTextureRenderer.RENDERER;
            }
        });
    }

    public Ingredient getRepairMaterial() {
        return Ingredient.of(repairItem);
    }

    @Override
    public int getMaxDamage(@NotNull ItemStack stack) {
        return material.getArmor().getShield().getDurability();
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, @NotNull ItemStack repair) {
        return getRepairMaterial().test(repair);
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return material.getArmor().getEnchantability();
    }

    public MaterialModel getMaterialModel() {
        return material;
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(
            @NotNull ItemStack stack,
            @NotNull Entity entity,
            @NotNull EquipmentSlot slot,
            ArmorMaterial.@NotNull Layer layer,
            boolean innerModel
    ) {
        if(material.getColors().getMaterialColor() == -1) {
            return super.getArmorTexture(stack, entity, slot, layer, innerModel);
        } else {
            return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/armor/empty.png"); //toString no longer needed here.
        }
    }

    public int getColorForIndex(int index) {
        switch (index) {
            case 0: return material.getColors().getHighlightColor(2);
            case 1: return material.getColors().getHighlightColor(1);
            case 3: return material.getColors().getShadowColor(1);
            case 4: return material.getColors().getShadowColor(2);
            default: material.getColors().getMaterialColor();
        }
        return material.getColors().getMaterialColor();
    }
}
