/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import org.thinkingstudio.ryoamiclights.fabric.api.DynamicLightsInitializer;

public class RyoamicLightsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new RyoamicLights().clientInit();

        FabricLoader.getInstance().getEntrypointContainers("dynamiclights", DynamicLightsInitializer.class)
                .stream().map(EntrypointContainer::getEntrypoint)
                .forEach(DynamicLightsInitializer::onInitializeDynamicLights);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier(RyoamicLights.NAMESPACE, "dynamiclights_resources");
            }

            @Override
            public void reload(ResourceManager manager) {
                ItemLightSources.load(manager);
            }
        });

        WorldRenderEvents.START.register(context -> {
            MinecraftClient.getInstance().getProfiler().swap("dynamic_lighting");
            RyoamicLights.get().updateAll(context.worldRenderer());
        });
    }
}
