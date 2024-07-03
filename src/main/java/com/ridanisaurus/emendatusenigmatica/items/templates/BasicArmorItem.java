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

package com.ridanisaurus.emendatusenigmatica.items.templates;

import com.ridanisaurus.emendatusenigmatica.plugin.model.EffectModel;
import com.ridanisaurus.emendatusenigmatica.plugin.model.material.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.registries.EERegistrar;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import com.ridanisaurus.emendatusenigmatica.util.RomanNumberHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// TODO: ArmorTier class unfinished.
public class BasicArmorItem extends ArmorItem {
    private final MaterialModel material;
    public final boolean isSet;
    public final List<EffectModel> effects;
    public final int highlight2;
    public final int highlight1;
    public final int base;
    public final int shadow1;
    public final int shadow2;

    public BasicArmorItem(@NotNull MaterialModel material, Type type) {
        super(EERegistrar.armorMaterialsMap.get(material.getId()), type, new Properties());
        this.material = material;
        this.isSet = material.getArmor().isSetArmor();
        this.effects = material.getArmor().getEffects();
        this.highlight2 = material.getColors().getHighlightColor(3);
        this.highlight1 = material.getColors().getHighlightColor(1);
        this.base = material.getColors().getMaterialColor();
        this.shadow1 = material.getColors().getShadowColor(1);
        this.shadow2 = material.getColors().getShadowColor(2);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!(slotId >= Inventory.INVENTORY_SIZE && slotId < Inventory.INVENTORY_SIZE + 4 && entity instanceof Player player)) return;
        if (isSet && isSetActive(player)) {
            for (EffectModel effect : effects) {
                player.addEffect(new MobEffectInstance(effect.getEffect(), 600, effect.getLevel(), true, effect.isShowParticles(), effect.isShowIcon()));
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        Player player = Minecraft.getInstance().player;
        if (player != null && isSet) {
            ItemStack[] setPieces = getSet();
            components.add(Component.literal(this.material.getArmor().getSetName())
                .append(" (" + getPiecesEquipped(player) + "/" + setPieces.length + ") ").withStyle(ChatFormatting.AQUA));
            if (!Screen.hasShiftDown()) {
                components.add(Component.literal("Press [SHIFT] for more info").withStyle(ChatFormatting.DARK_GRAY));
            }
            if (Screen.hasShiftDown()) {
                //FIXME: Make sure this works as intended.
//                for (int i = 0; i < setPieces.length; i++) {
//                    components.add(setPieces[i].getHoverName().plainCopy().withStyle((hasSetPiece(player, EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, setPieces.length - i - 1)) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY)));
//                }
                components.add(setPieces[0].getHoverName().plainCopy().withStyle((hasSetPiece(player, EquipmentSlot.HEAD) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY)));
                components.add(setPieces[1].getHoverName().plainCopy().withStyle((hasSetPiece(player, EquipmentSlot.CHEST) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY)));
                components.add(setPieces[2].getHoverName().plainCopy().withStyle((hasSetPiece(player, EquipmentSlot.LEGS) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY)));
                components.add(setPieces[3].getHoverName().plainCopy().withStyle((hasSetPiece(player, EquipmentSlot.FEET) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY)));
                components.add(Component.literal(" "));
                components.add(Component.literal(this.material.getArmor().getSetDesc()).withStyle(isSetActive(player) ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));
                components.add(Component.literal(" "));
                components.add(Component.literal("Effect(s):").withStyle(ChatFormatting.GRAY));
                for (EffectModel effect : this.effects) {
                    components.add(Component.literal("- " + effect.getEffect().value().getDisplayName().getString() + " " + RomanNumberHelper.toRoman(effect.getLevel() + 1)).withStyle(isSetActive(player) ? ChatFormatting.BLUE : ChatFormatting.DARK_GRAY));
                }
            }
        }
    }

    public boolean isSetActive(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EERegistrar.helmetMap.get(this.material.getId()).get() &&
            player.getItemBySlot(EquipmentSlot.CHEST).getItem() == EERegistrar.chestplateMap.get(this.material.getId()).get() &&
            player.getItemBySlot(EquipmentSlot.LEGS).getItem() == EERegistrar.leggingsMap.get(this.material.getId()).get() &&
            player.getItemBySlot(EquipmentSlot.FEET).getItem() == EERegistrar.bootsMap.get(this.material.getId()).get();
    }

    public ItemStack[] getSet() {
        return new ItemStack[]{
            new ItemStack(EERegistrar.helmetMap.get(this.material.getId()).get()),
            new ItemStack(EERegistrar.chestplateMap.get(this.material.getId()).get()),
            new ItemStack(EERegistrar.leggingsMap.get(this.material.getId()).get()),
            new ItemStack(EERegistrar.bootsMap.get(this.material.getId()).get())
        };
    }

    public boolean hasSetPiece(Player player, EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> player.getItemBySlot(slot).getItem() == EERegistrar.helmetMap.get(this.material.getId()).get();
            case CHEST -> player.getItemBySlot(slot).getItem() == EERegistrar.chestplateMap.get(this.material.getId()).get();
            case LEGS -> player.getItemBySlot(slot).getItem() == EERegistrar.leggingsMap.get(this.material.getId()).get();
            case FEET -> player.getItemBySlot(slot).getItem() == EERegistrar.bootsMap.get(this.material.getId()).get();
            default -> false;
        };
    }

    private int getPiecesEquipped(Player player) {
        int armorPieces = 0;
        EquipmentSlot[] values = EquipmentSlot.values();
        for (EquipmentSlot slot : values) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && hasSetPiece(player, slot)) {
                armorPieces++;
            }
        }
        return armorPieces;
    }

    public MaterialModel getMaterialModel() {
        return material;
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(@NotNull ItemStack stack, @NotNull Entity entity, @NotNull EquipmentSlot slot, ArmorMaterial.@NotNull Layer layer, boolean innerModel) {
        if (!material.getColors().hasMaterialColor()) {
            return super.getArmorTexture(stack, entity, slot, layer, innerModel);
        } else {
            return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/armor/empty.png");
        }
    }

    public int getColorForIndex(int index){
        switch (index){
            case 0: return material.getColors().getHighlightColor(2);
            case 1: return material.getColors().getHighlightColor(1);
            case 3: return material.getColors().getShadowColor(1);
            case 4: return material.getColors().getShadowColor(2);
            default: material.getColors().getMaterialColor();
        }
        return material.getColors().getMaterialColor();
    }
}