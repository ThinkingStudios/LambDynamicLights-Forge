package org.thinkingstudio.ryoamiclights.forge;

import net.minecraft.client.MinecraftClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.forge.api.DynamicLightsInitializerEvent;

public class ForgeEventHandler {
    public static void registerEvent() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        forgeEventBus.post(new DynamicLightsInitializerEvent());

        forgeEventBus.addListener(EventPriority.HIGHEST, ForgeEventHandler::renderWorldLast);
    }

    private static void renderWorldLast(RenderWorldLastEvent event) {
        MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
        RyoamicLights.get().updateAll(event.getContext());
    }
}
