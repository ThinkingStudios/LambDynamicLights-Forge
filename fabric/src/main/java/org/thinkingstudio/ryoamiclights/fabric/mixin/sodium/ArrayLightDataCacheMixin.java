/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.fabric.mixin.sodium;

import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.util.SodiumDynamicLightHandler;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {
		"me.jellysquid.mods.sodium.client.model.light.data.ArrayLightDataCache",
		"net.caffeinemc.mods.sodium.client.model.light.data.ArrayLightDataCache"
}, remap = false)
public abstract class ArrayLightDataCacheMixin {
	@Dynamic
	@Inject(method = "get(III)I", at = @At("HEAD"), require = 0)
	private void ryoamiclights$storeLightPos(int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
		if (!RyoamicLights.get().config.getDynamicLightsMode().isEnabled())
			return;

		// Store the current light position.
		// This is possible under smooth lighting scenarios, because AoFaceData in Sodium runs a get() call
		// before getting the lightmap.
		SodiumDynamicLightHandler.pos.get().set(x, y, z);
	}
}
