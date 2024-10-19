package org.thinkingstudio.ryoamiclights.neoforge;

import com.google.auto.service.AutoService;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import org.thinkingstudio.ryoamiclights.services.interfaces.ModPlatformService;

import java.nio.file.Path;

@AutoService(ModPlatformService.class)
public class ModPlatformImpl implements ModPlatformService {
    @Override
    public boolean isModLoaded(String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }
}
