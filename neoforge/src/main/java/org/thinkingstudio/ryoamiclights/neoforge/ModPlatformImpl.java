package org.thinkingstudio.ryoamiclights.neoforge;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
