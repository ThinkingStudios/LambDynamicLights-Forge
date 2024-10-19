package org.thinkingstudio.ryoamiclights.services.interfaces;

import java.nio.file.Path;

public interface ModPlatformService {

    boolean isModLoaded(String modid);

    Path getConfigDir();
}
