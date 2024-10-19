/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.fabric.mixin.indium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import link.infra.indium.renderer.render.AbstractBlockRenderContext;
import link.infra.indium.renderer.render.TerrainRenderContext;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition("indium"))
@Pseudo
@Mixin(value = TerrainRenderContext.class, remap = false)
public abstract class TerrainRenderContextMixin extends AbstractBlockRenderContext {
	@Dynamic
	@ModifyExpressionValue(method = "bufferQuad", at = @At(value = "INVOKE", target = "Llink/infra/indium/renderer/mesh/MutableQuadViewImpl;lightmap(I)I"), require = 0)
	private int ryoamiclights$getLightmap(int original) {
		return RyoamicLights.get().getLightmapWithDynamicLight(this.blockInfo.blockPos, original);
	}
}