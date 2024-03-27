package org.thinkingstudio.ryoamiclights.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

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

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
