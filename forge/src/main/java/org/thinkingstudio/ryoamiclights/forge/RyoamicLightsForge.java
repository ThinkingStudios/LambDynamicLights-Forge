/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.forge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.gui.SettingsScreen;

@Mod(RyoamicLights.MODID)
public class RyoamicLightsForge {
    public RyoamicLightsForge() {
        if (FMLLoader.getDist().isClient()) {
            ModLoadingContext context = ModLoadingContext.get();

            RyoamicLights.get().clientInit();

            ((ReloadableResourceManager) MinecraftClient.getInstance().getResourceManager()).registerReloader((SynchronousResourceReloader) ItemLightSources::load);
            ClientRegistry.registerKeyBinding(RyoamicLights.get().keyBinding);

            context.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
            context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ((client, screen) -> new SettingsScreen(screen)));

            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

            forgeEventBus.<RenderWorldLastEvent>addListener(EventPriority.HIGHEST, event -> {
                MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
                RyoamicLights.get().updateAll(event.getContext());
            });
        }
    }
}
