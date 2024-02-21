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

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
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
        new RyoamicLights().clientInit();
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::renderWorldLastEvent);
        MinecraftForge.EVENT_BUS.post(new DynamicLightsInitializerEvent());
        Platform.getMod(RyoamicLights.MODID).registerConfigurationScreen(SettingsScreen::new);
    }

    public void renderWorldLastEvent(@NotNull RenderWorldLastEvent event) {
        MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
        RyoamicLights.get().updateAll(event.getContext());
    }
}
