package org.thinkingstudio.ryoamiclights.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class RyoamicLightsCompatImpl {
    public static boolean isCanvasInstalled() {
        return FabricLoader.getInstance().isModLoaded("canvas");
    }

    public static boolean isLilTaterReloadedInstalled() {
        return FabricLoader.getInstance().isModLoaded("ltr");
    }

    public static boolean isDevEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
