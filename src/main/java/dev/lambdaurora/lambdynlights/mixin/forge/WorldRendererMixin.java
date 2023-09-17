package dev.lambdaurora.lambdynlights.mixin.forge;

import dev.lambdaurora.lambdynlights.LambDynLights;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    /**
     * Architectury API/Forge NOT HAVE {@code WorldRenderEvents}
     *
     * @author TexTrue
     */
    @Inject(at = @At("HEAD"), method = "render")
    public void render(MatrixStack outlinebuffersource, float i, long j, boolean k, Camera l, GameRenderer i1, LightmapTextureManager lightTexture, Matrix4f multibuffersource, CallbackInfo ci) {
        MinecraftClient.getInstance().getProfiler().push("dynamic_lighting");
        LambDynLights.get().updateAll((WorldRenderer) (Object) this);
    }
}
