package org.thinkingstudio.ryoamiclights.services;

import java.util.ServiceLoader;

public class ServicesUtils {
    public static <T> T loadService(final Class<T> clazz) {
        return ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new AssertionError("No impl found for " + clazz.getPackageName()));
    }
}
