package org.thinkingstudio.ryoamiclights.forge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.forge.api.DynamicLightsInitializerEvent;

public class ForgeEventHandler {
    public static void registerEvents() {
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        forgeEventBus.<RenderLevelStageEvent>addListener(EventPriority.HIGHEST, event -> {
            if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
                RyoamicLights.get().updateAll(event.getLevelRenderer());
            }
        });
        modEventBus.<RegisterClientReloadListenersEvent>addListener(EventPriority.HIGHEST, event -> {
            event.registerReloadListener((SynchronousResourceReloader) ItemLightSources::load);
        });
        modEventBus.<RegisterKeyMappingsEvent>addListener(EventPriority.HIGHEST, event -> {
            event.register(RyoamicLights.get().keyBinding);
        });

        forgeEventBus.post(new DynamicLightsInitializerEvent());
    }
}
