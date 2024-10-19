package org.thinkingstudio.ryoamiclights.fabric;

import com.google.auto.service.AutoService;
import net.fabricmc.loader.api.FabricLoader;
import org.thinkingstudio.ryoamiclights.services.interfaces.ModPlatformService;

import java.nio.file.Path;

@AutoService(ModPlatformService.class)
public class ModPlatformImpl implements ModPlatformService {
    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
