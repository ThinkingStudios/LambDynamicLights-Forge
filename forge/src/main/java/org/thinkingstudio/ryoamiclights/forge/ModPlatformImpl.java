package org.thinkingstudio.ryoamiclights.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ModPlatformImpl {
    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}