package org.thinkingstudio.ryoamiclights.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    public static Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
