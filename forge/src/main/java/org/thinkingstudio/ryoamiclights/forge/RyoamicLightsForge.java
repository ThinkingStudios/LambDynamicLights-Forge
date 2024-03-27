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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.forge.api.DynamicLightsInitializerEvent;
import org.thinkingstudio.ryoamiclights.gui.SettingsScreen;

@Mod(RyoamicLights.MODID)
public class RyoamicLightsForge {
    public RyoamicLightsForge() {
        if (FMLLoader.getDist().isClient()) {
            this.onInitializeClient();
        }
    }

    public void onInitializeClient() {
        ModLoadingContext context = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        RyoamicLights.get().clientInit();
        registerClientReloadListener();

        context.registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        context.registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> ((client, screen) -> new SettingsScreen(screen)));
        modEventBus.addListener(EventPriority.HIGHEST, this::renderWorldLast);

        MinecraftForge.EVENT_BUS.post(new DynamicLightsInitializerEvent());
    }

    public void renderWorldLast(@NotNull RenderWorldLastEvent event) {
        MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
        RyoamicLights.get().updateAll(event.getContext());
    }

    @OnlyIn(Dist.CLIENT)
    private void registerClientReloadListener() {
        ((ReloadableResourceManager) MinecraftClient.getInstance().getResourceManager()).registerReloader((SynchronousResourceReloader) ItemLightSources::load);
    }
}
