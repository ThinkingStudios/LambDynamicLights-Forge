package org.thinkingstudio.ryoamiclights.neoforge;

import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
