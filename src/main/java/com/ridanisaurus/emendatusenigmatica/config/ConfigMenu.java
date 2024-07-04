package com.ridanisaurus.emendatusenigmatica.config;

import com.ridanisaurus.emendatusenigmatica.util.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public class ConfigMenu extends Screen {
    private static final ResourceLocation LOGO = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "ee_logo.png");
    // Buttons
    private static final Component ENABLED  = Component.translatable("screen.emendatusenigmatica.config.enabled").withStyle(ChatFormatting.GREEN);
    private static final Component DISABLED = Component.translatable("screen.emendatusenigmatica.config.disabled").withStyle(ChatFormatting.RED);
    // Titles
    private static final Component TITLE    = Component.translatable("screen.emendatusenigmatica.config.title");
    private static final Component C_TITLE  = Component.translatable("screen.emendatusenigmatica.config.client.title");
    private static final Component V_TITLE  = Component.translatable("screen.emendatusenigmatica.config.validation.title");
    // Options
    private static final Component PATREON      = Component.translatable("screen.emendatusenigmatica.config.client.patreon.text");
    private static final Component PATREON_TIP  = Component.translatable("screen.emendatusenigmatica.config.client.patreon.tip");
    private static final Component GLINT        = Component.translatable("screen.emendatusenigmatica.config.client.glint.text");
    private static final Component GLINT_TIP    = Component.translatable("screen.emendatusenigmatica.config.client.glint.tip");
    private static final Component LOG_ERR      = Component.translatable("screen.emendatusenigmatica.config.validation.log_errors.text");
    private static final Component LOG_ERR_TIP  = Component.translatable("screen.emendatusenigmatica.config.validation.log_errors.tip");
    // Other
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
    private final Screen last;
    public ConfigMenu(Screen last) {
        super(TITLE);
        this.last = last;
    }

    @Override
    protected void init() {
        super.init();
        // Old LOGO Rendering. It does scale dynamically.
//        this.addRenderableOnly((guiGraphics, mouseX, mouseY, partialTick) -> {
//            int rWidth = (int) Math.min(((float) height /8)*4.3f, width*0.75f);
//            int rHeight = (int) Math.min((width*0.75f)/4.3f, (float) height /8);
//            guiGraphics.blit(LOGO, width/2-rWidth/2,8,0,0,rWidth, rHeight, rWidth, rHeight);
//        });

        this.layout.addToHeader(ImageWidget.texture(5*64, 64, LOGO, 5*64, 64), LayoutSettings::alignHorizontallyCenter);

        GridLayout grid = new GridLayout();
        grid.defaultCellSetting().paddingHorizontal(8).paddingVertical(6).alignHorizontallyCenter().alignVerticallyTop();

        addTitle(grid, C_TITLE);
        addButton(grid, PATREON, PATREON_TIP, EEConfig.client.showPatreonReward);
        addButton(grid, GLINT, GLINT_TIP, EEConfig.client.oldSchoolGlint);
        addTitle(grid, V_TITLE);
        addButton(grid, LOG_ERR, LOG_ERR_TIP, EEConfig.startup.logConfigErrors);

        this.layout.addToContents(grid);

        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, p_345431_ -> this.onClose()).width(200).build());
        this.layout.visitWidgets(p_344729_ -> {
            AbstractWidget abstractwidget = this.addRenderableWidget(p_344729_);
        });
        this.repositionElements();
    }

    private int currentRow = 1;
    private void addTitle(@NotNull GridLayout layout, Component title) {
        layout.addChild(new StringWidget(title, this.font), currentRow++, 1, 1, 2, LayoutSettings::alignVerticallyMiddle);
    }

    private void addButton(@NotNull GridLayout layout, Component text, Component tooltip, ModConfigSpec.BooleanValue config) {
        layout.addChild(new StringWidget(text, this.font), currentRow, 1, LayoutSettings::alignVerticallyMiddle);
        layout.addChild(Button
            .builder(config.get()? ENABLED: DISABLED, (event) -> {
                if (event.getMessage() == ENABLED) {
                    event.setMessage(DISABLED);
                    config.set(false);
                } else {
                    event.setMessage(ENABLED);
                    config.set(true);
                }
                config.save();
            })
            .tooltip(Tooltip.create(tooltip))
        .build(), currentRow++, 2, LayoutSettings::alignVerticallyMiddle);
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.setScreen(last);
    }
}
