package org.thinkingstudio.ryoamiclights;

import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.gui.SettingsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.io.SynchronousResourceReloader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = LambDynLights.NAMESPACE, dist = Dist.CLIENT)
public class RyoamicLights {
    public RyoamicLights(IEventBus modEventBus, ModContainer modContainer) {
        if (FMLLoader.getDist().isClient()) {
            LambDynLights.get().onInitializeClient();

            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, screen) -> new SettingsScreen(screen));

            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, RenderLevelStageEvent.class, event -> {
                if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                    Minecraft.getInstance().getProfiler().swap("dynamic_lighting");
                    LambDynLights.get().updateAll(event.getLevelRenderer());
                }
            });
            modEventBus.addListener(EventPriority.HIGHEST, RegisterClientReloadListenersEvent.class, event -> {
                event.registerReloadListener((SynchronousResourceReloader) resourceManager -> LambDynLights.get().itemLightSources.load(resourceManager));
            });
        }
    }
}
