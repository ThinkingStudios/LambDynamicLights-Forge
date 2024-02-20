/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.fabric.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.obsidianui.Tooltip;
import org.thinkingstudio.ryoamiclights.gui.DynamicLightsOptionsOption;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin extends GameOptionsScreen {
	@Shadow
	private ButtonListWidget list;
	@Unique
	private Option ryoamiclights$option;

	public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstruct(Screen parent, GameOptions gameOptions, CallbackInfo ci) {
		this.ryoamiclights$option = new DynamicLightsOptionsOption(this);
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void onInit(CallbackInfo ci) {
		this.list.addSingleOptionEntry(this.ryoamiclights$option);
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void onRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		Tooltip.renderAll(this, matrices);
	}
}
