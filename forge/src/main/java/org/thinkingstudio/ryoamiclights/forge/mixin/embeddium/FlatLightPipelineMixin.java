/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.forge.mixin.embeddium;

import org.thinkingstudio.ryoamiclights.util.SodiumDynamicLightHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(targets = "me.jellysquid.mods.sodium.client.model.light.flat.FlatLightPipeline", remap = false)
public abstract class FlatLightPipelineMixin {
	@Dynamic
	@Inject(
			method = "getOffsetLightmap",
			at = @At(value = "RETURN", ordinal = 1),
			require = 0,
			remap = false,
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void ryoamiclights$getLightmap(
			BlockPos pos, Direction face, CallbackInfoReturnable<Integer> cir,
			int word, int adjWord
	) {
		int lightmap = SodiumDynamicLightHandler.getLightmap(pos, adjWord, cir.getReturnValueI());
		cir.setReturnValue(lightmap);
	}
}
