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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.neoforged.fml.common.Mod;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.gui.SettingsScreen;

@Mod(value = RyoamicLights.NAMESPACE, dist = Dist.CLIENT)
public class RyoamicLightsNeoForge {
    public RyoamicLightsNeoForge(ModContainer modContainer, IEventBus modEventBus) {
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;

        if (FMLLoader.getDist().isClient()) {
            RyoamicLights.get().clientInit();

            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> new SettingsScreen(screen));

            forgeEventBus.addListener(EventPriority.HIGHEST, RenderLevelStageEvent.class, event -> {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                    MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
                    RyoamicLights.get().updateAll(event.getLevelRenderer());
                }
            });
            modEventBus.addListener(EventPriority.HIGHEST, RegisterClientReloadListenersEvent.class, event -> {
                event.registerReloadListener((SynchronousResourceReloader) ItemLightSources::load);
            });
        }
    }
}
