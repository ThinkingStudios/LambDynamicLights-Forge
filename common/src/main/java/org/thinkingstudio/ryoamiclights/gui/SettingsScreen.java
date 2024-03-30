/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.gui;

import org.thinkingstudio.ryoamiclights.DynamicLightsConfig;
import org.thinkingstudio.ryoamiclights.ExplosiveLightingMode;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.RyoamicLightsCompat;
import org.thinkingstudio.obsidianui.Position;
import org.thinkingstudio.obsidianui.option.*;
import org.thinkingstudio.obsidianui.screen.SpruceScreen;
import org.thinkingstudio.obsidianui.widget.SpruceButtonWidget;
import org.thinkingstudio.obsidianui.widget.container.SpruceOptionListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the settings screen of RyoamicLights.
 *
 * @author LambdAurora
 * @version 1.3.4
 * @since 1.0.0
 */
public class SettingsScreen extends SpruceScreen {
    private final DynamicLightsConfig config;
    private final Screen parent;
    private final SpruceOption entitiesOption;
    private final SpruceOption blockEntitiesOption;
    private final SpruceOption waterSensitiveOption;
    private final SpruceOption creeperLightingOption;
    private final SpruceOption tntLightingOption;
    private final SpruceOption resetOption;
    private SpruceOptionListWidget list;

    public SettingsScreen(@Nullable Screen parent) {
        super(new TranslatableText("ryoamiclights.menu.title"));
        this.parent = parent;
        this.config = RyoamicLights.get().config;

        this.entitiesOption = new SpruceBooleanOption("ryoamiclights.option.entities",
                this.config::hasEntitiesLightSource,
                this.config::setEntitiesLightSource,
                new TranslatableText("ryoamiclights.tooltip.entities"), true);
        this.blockEntitiesOption = new SpruceBooleanOption("ryoamiclights.option.block_entities",
                this.config::hasBlockEntitiesLightSource,
                this.config::setBlockEntitiesLightSource,
                new TranslatableText("ryoamiclights.tooltip.block_entities"), true);
        this.waterSensitiveOption = new SpruceBooleanOption("ryoamiclights.option.water_sensitive",
                this.config::hasWaterSensitiveCheck,
                this.config::setWaterSensitiveCheck,
                new TranslatableText("ryoamiclights.tooltip.water_sensitive"), true);
        this.creeperLightingOption = new SpruceCyclingOption("entity.minecraft.creeper",
                amount -> this.config.setCreeperLightingMode(this.config.getCreeperLightingMode().next()),
                option -> option.getDisplayText(this.config.getCreeperLightingMode().getTranslatedText()),
                new TranslatableText("ryoamiclights.tooltip.creeper_lighting",
                        ExplosiveLightingMode.OFF.getTranslatedText(),
                        ExplosiveLightingMode.SIMPLE.getTranslatedText(),
                        ExplosiveLightingMode.FANCY.getTranslatedText()));
        this.tntLightingOption = new SpruceCyclingOption("block.minecraft.tnt",
                amount -> this.config.setTntLightingMode(this.config.getTntLightingMode().next()),
                option -> option.getDisplayText(this.config.getTntLightingMode().getTranslatedText()),
                new TranslatableText("ryoamiclights.tooltip.tnt_lighting",
                        ExplosiveLightingMode.OFF.getTranslatedText(),
                        ExplosiveLightingMode.SIMPLE.getTranslatedText(),
                        ExplosiveLightingMode.FANCY.getTranslatedText()));
        this.resetOption = SpruceSimpleActionOption.reset(btn -> {
            this.config.reset();
            MinecraftClient client = MinecraftClient.getInstance();
            this.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        });
    }

    @Override
    public void removed() {
        super.removed();
        this.config.save();
    }

    private int getTextHeight() {
        return (5 + this.textRenderer.fontHeight) * 3 + 5;
    }

    @Override
    protected void init() {
        super.init();

        this.list = new SpruceOptionListWidget(Position.of(this, 0, 43), this.width, this.height - 43 - 29 - this.getTextHeight());
        this.list.addSingleOptionEntry(this.config.dynamicLightsModeOption);
        this.list.addSingleOptionEntry(new SpruceSeparatorOption("ryoamiclights.menu.light_sources", true, null));
        this.list.addOptionEntry(this.entitiesOption, this.blockEntitiesOption);
        this.list.addOptionEntry(this.waterSensitiveOption, null);
        this.list.addOptionEntry(this.creeperLightingOption, this.tntLightingOption);
        this.addChild(list);

        this.addChild(this.resetOption.createWidget(Position.of(this, this.width / 2 - 155, this.height - 29), 150));
        this.addChild(new SpruceButtonWidget(Position.of(this, this.width / 2 - 155 + 160, this.height - 29), 150, 20, new TranslatableText("gui.done"),
                (btn) -> this.client.openScreen(this.parent)));
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        this.renderBackgroundTexture(0);
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 18, 16777215);
        if (RyoamicLightsCompat.isCanvasInstalled()) {
            drawCenteredText(matrices, this.textRenderer, I18n.translate("ryoamiclights.menu.canvas.1"), this.width / 2, this.height - 29 - (5 + this.textRenderer.fontHeight) * 3, 0xFFFF0000);
            drawCenteredText(matrices, this.textRenderer, I18n.translate("ryoamiclights.menu.canvas.2"), this.width / 2, this.height - 29 - (5 + this.textRenderer.fontHeight) * 2, 0xFFFF0000);
        }
    }
}
