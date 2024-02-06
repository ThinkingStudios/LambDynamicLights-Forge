package org.thinkingstudio.ryoamiclights.forge;

import net.minecraftforge.fml.loading.FMLLoader;

public class RyoamicLightsExpectPlatformImpl {
    public static boolean isDevEnvironment() {
        return !FMLLoader.isProduction();
    }
}
