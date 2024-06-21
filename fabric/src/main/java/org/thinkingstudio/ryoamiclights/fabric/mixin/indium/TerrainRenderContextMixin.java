package org.thinkingstudio.ryoamiclights.fabric.mixin.indium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import link.infra.indium.renderer.render.AbstractBlockRenderContext;
import link.infra.indium.renderer.render.TerrainRenderContext;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.thinkingstudio.ryoamiclights.RyoamicLights;

@Pseudo
@Mixin(value = TerrainRenderContext.class, remap = false)
public abstract class TerrainRenderContextMixin extends AbstractBlockRenderContext {
    @Dynamic
    @ModifyExpressionValue(method = "bufferQuad", at = @At(value = "INVOKE", target = "Llink/infra/indium/renderer/mesh/MutableQuadViewImpl;lightmap(I)I"), require = 0)
    private int lambdynlights$getLightmap(int original) {
        return RyoamicLights.get().getLightmapWithDynamicLight(this.blockInfo.blockPos, original);
    }
}
