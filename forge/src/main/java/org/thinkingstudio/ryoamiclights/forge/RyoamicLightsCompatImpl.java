package org.thinkingstudio.ryoamiclights.forge;

import net.minecraftforge.fml.loading.FMLLoader;

public class RyoamicLightsCompatImpl {
    public static boolean isCanvasInstalled() {
        return FMLLoader.getLoadingModList().getModFileById("canvas") != null;
    }

    public static boolean isLilTaterReloadedInstalled() {
        return FMLLoader.getLoadingModList().getModFileById("ltr") != null;
    }

    public static boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }
}
