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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
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

        MinecraftForge.EVENT_BUS.addListener(this::renderWorldLastEvent);

        Platform.getMod(RyoamicLights.MODID).registerConfigurationScreen(SettingsScreen::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderWorldLastEvent(@NotNull RenderWorldLastEvent event) {
        MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
        RyoamicLights.get().updateAll(event.getContext());
    }
}
