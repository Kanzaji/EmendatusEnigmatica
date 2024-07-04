package com.ridanisaurus.emendatusenigmatica.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ridanisaurus.emendatusenigmatica.config.EEConfig;
import com.ridanisaurus.emendatusenigmatica.events.ArmorTextureEvent;
import com.ridanisaurus.emendatusenigmatica.items.templates.BasicArmorItem;
import com.ridanisaurus.emendatusenigmatica.util.ColorHelper;
import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.jetbrains.annotations.NotNull;

// Credit: PNC:R
public class ArmorTextureRenderer<E extends LivingEntity, M extends HumanoidModel<E>> extends RenderLayer<E, M> {

    private final HumanoidModel<E> body;
    private final HumanoidModel<E> legs;

    public ArmorTextureRenderer(RenderLayerParent<E, M> renderLayerParent, @NotNull EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.body = new HumanoidModel<>(entityModelSet.bakeLayer(ArmorTextureEvent.ARMOR));
        this.legs = new HumanoidModel<>(entityModelSet.bakeLayer(ArmorTextureEvent.LEGS));
    }

    @Override
    public void render(@NotNull PoseStack matrixStackIn, @NotNull MultiBufferSource bufferIn, int packedLightIn, @NotNull E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        for (int i = 0; i < 5; i++) {
            var rl = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/armor/layer_2/0"+i+ ".png");
            var renderType = RenderType.armorCutoutNoCull(rl);
            renderSlot(matrixStackIn, bufferIn, entity, EquipmentSlot.LEGS, packedLightIn, legs,
                    partialTicks, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderType, i);
        }
        for (int i = 0; i < 5; i++) {
            var rl = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "textures/armor/layer_1/0"+i+ ".png");
            var renderType = RenderType.armorCutoutNoCull(rl);
            renderSlot(matrixStackIn, bufferIn, entity, EquipmentSlot.CHEST, packedLightIn, body,
                    partialTicks, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderType, i);
            renderSlot(matrixStackIn, bufferIn, entity, EquipmentSlot.FEET, packedLightIn, body,
                    partialTicks, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderType, i);
            renderSlot(matrixStackIn, bufferIn, entity, EquipmentSlot.HEAD, packedLightIn, body,
                    partialTicks, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, renderType, i);
        }
    }

    private void renderSlot(PoseStack matrixStack, MultiBufferSource buffer, @NotNull E entity, EquipmentSlot slot, int light, HumanoidModel<E> model, float partialTicks, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, RenderType renderType, int colorIndex) {
        ItemStack stack = entity.getItemBySlot(slot);
        if (stack.getItem() instanceof BasicArmorItem armor && armor.getEquipmentSlot() == slot && armor.getMaterialModel().getColors().getMaterialColor() != -1) {
            this.getParentModel().copyPropertiesTo(model);
            model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            this.setModelSlotVisible(model, slot);
            Model model1 = ClientHooks.getArmorModel(entity, stack, slot, model);
            boolean glint = EEConfig.client.oldSchoolGlint.get() && stack.hasFoil();

            // secondary texture layer in all slots
//            float[] secondary = decomposeColorF(armor.getColorForIndex(colorIndex));
            this.doRender(matrixStack, buffer, light, glint, model1, armor.getColorForIndex(colorIndex), renderType);
        }
    }

    private void doRender(PoseStack matrixStack, MultiBufferSource buffer, int light, boolean glint, @NotNull Model model, int color, RenderType renderType) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, renderType, glint);
        model.renderToBuffer(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, color);
    }

    protected void setModelSlotVisible(@NotNull HumanoidModel<E> model, @NotNull EquipmentSlot slotIn) {
        model.setAllVisible(false);
        switch (slotIn) {
            case HEAD -> {
                model.head.visible = true;
                model.hat.visible = true;
            }
            case CHEST -> {
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
        }
    }
}