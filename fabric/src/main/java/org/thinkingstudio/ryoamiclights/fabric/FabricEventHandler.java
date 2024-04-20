package org.thinkingstudio.ryoamiclights.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import org.thinkingstudio.ryoamiclights.RyoamicLights;

public class FabricEventHandler {
    public static void registerEvents() {
        WorldRenderEvents.START.register(context -> {
            MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
            RyoamicLights.get().updateAll(context.worldRenderer());
        });
    }
}
