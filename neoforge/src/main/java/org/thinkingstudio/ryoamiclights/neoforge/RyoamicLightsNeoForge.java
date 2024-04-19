/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.neoforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.SynchronousResourceReloader;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.neoforged.fml.common.Mod;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.gui.SettingsScreen;
import org.thinkingstudio.ryoamiclights.neoforge.api.DynamicLightsInitializerEvent;

@Mod(RyoamicLights.NAMESPACE)
public class RyoamicLightsNeoForge {
    public RyoamicLightsNeoForge(IEventBus modEventBus) {
        if (FMLLoader.getDist().isClient()) {
            this.onInitializeClient(modEventBus);
        }
    }

    public void onInitializeClient(IEventBus modEventBus) {
        ModLoadingContext context = ModLoadingContext.get();

        RyoamicLights.get().clientInit();

        context.registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> IExtensionPoint.DisplayTest.IGNORESERVERONLY, (a, b) -> true));
        context.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> new SettingsScreen(screen)));
        modEventBus.addListener(EventPriority.HIGHEST, RenderLevelStageEvent.class, event -> {
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

        NeoForge.EVENT_BUS.post(new DynamicLightsInitializerEvent());
    }
}
