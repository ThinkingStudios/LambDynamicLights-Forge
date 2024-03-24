package org.thinkingstudio.ryoamiclights.forge;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class RyoamicLightsCompatImpl {
    public static boolean isCanvasInstalled() {
        return FMLLoader.getLoadingModList().getModFileById("canvas") != null;
    }

    public static boolean isLilTaterReloadedInstalled() {
        return FMLLoader.getLoadingModList().getModFileById("ltr") != null;
    }

    public static boolean isSodiumInstalled() {
        return FMLLoader.getLoadingModList().getModFileById("embeddium") != null;
    }

    public static boolean isFabricApiInstalled() {
        return FMLLoader.getLoadingModList().getModFileById("fabric_api") != null;
    }

    public static boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
