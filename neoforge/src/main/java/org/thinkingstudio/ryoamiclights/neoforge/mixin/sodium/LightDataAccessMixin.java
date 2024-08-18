/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.neoforge.mixin.sodium;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.thinkingstudio.ryoamiclights.util.SodiumDynamicLightHandler;

@Pseudo
@Mixin(targets = {
		"me.jellysquid.mods.sodium.client.model.light.data.LightDataAccess",
		"net.caffeinemc.mods.sodium.client.model.light.data.LightDataAccess"
}, remap = false)
public abstract class LightDataAccessMixin {
	@Dynamic
	@Inject(method = "getLightmap", at = @At("RETURN"), remap = false, require = 0, cancellable = true)
	private static void ryoamiclights$getLightmap(int word, CallbackInfoReturnable<Integer> cir) {
		int lightmap = SodiumDynamicLightHandler.getLightmap(SodiumDynamicLightHandler.pos.get(), word, cir.getReturnValueI());
		cir.setReturnValue(lightmap);
	}
}
