package org.thinkingstudio.ryoamiclights.neoforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.SynchronousResourceReloader;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.neoforge.api.DynamicLightsInitializerEvent;

public class ForgeEventHandler {
    public static void registerEvents(IEventBus modEventBus) {
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        forgeEventBus.addListener(EventPriority.HIGHEST, RenderLevelStageEvent.class, event -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
                RyoamicLights.get().updateAll(event.getLevelRenderer());
            }
        });
        modEventBus.addListener(EventPriority.HIGHEST, RegisterClientReloadListenersEvent.class, event -> {
            event.registerReloadListener((SynchronousResourceReloader) ItemLightSources::load);
        });
        modEventBus.addListener(EventPriority.HIGHEST, RegisterKeyMappingsEvent.class, event -> {
            event.register(RyoamicLights.get().keyBinding);
        });

        forgeEventBus.post(new DynamicLightsInitializerEvent());
    }
}
