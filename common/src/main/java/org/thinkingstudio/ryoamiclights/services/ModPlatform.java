package org.thinkingstudio.ryoamiclights.services;

import org.thinkingstudio.ryoamiclights.services.interfaces.ModPlatformService;

public class ModPlatform {
    private static final ModPlatformService PLATFORM = ServicesUtils.loadService(ModPlatformService.class);

    public static ModPlatformService getInstance() {
        return PLATFORM;
    }
}
