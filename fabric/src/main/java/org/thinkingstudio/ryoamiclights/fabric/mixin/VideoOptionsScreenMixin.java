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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.obsidianui.Tooltip;
import org.thinkingstudio.ryoamiclights.gui.DynamicLightsOptionsOption;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin extends GameOptionsScreen {
	@Unique
	private Option<?> ryoamiclights$option;

	public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstruct(Screen parent, GameOptions gameOptions, CallbackInfo ci) {
		this.ryoamiclights$option = DynamicLightsOptionsOption.getOption(this);
	}

	@ModifyArg(
			method = "init",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/widget/ButtonListWidget;addEntries([Lnet/minecraft/client/option/Option;)V"
			),
			index = 0
	)
	private Option<?>[] addOptionButton(Option<?>[] old) {
		var options = new Option<?>[old.length + 1];
		System.arraycopy(old, 0, options, 0, old.length);
		options[options.length - 1] = this.ryoamiclights$option;
		return options;
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void onRender(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		Tooltip.renderAll(graphics);
	}
}
