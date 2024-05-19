package org.thinkingstudio.ryoamiclights;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class ModPlatform {
    @ExpectPlatform
    public static boolean isModLoaded(String modid) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path getConfigDir() {
        throw new AssertionError();
    }
}
