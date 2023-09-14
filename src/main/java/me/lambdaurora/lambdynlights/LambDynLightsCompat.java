/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.lambdaurora.lambdynlights;

import me.shedaniel.architectury.platform.Platform;

/**
 * Represents a utility class for compatibility.
 *
 * @author LambdAurora
 * @version 1.3.3
 * @since 1.0.0
 */
public final class LambDynLightsCompat {
    /**
     * Returns whether Canvas is installed.
     *
     * @return {@code true} if Canvas is installed, else {@code false}
     */
    public static boolean isCanvasInstalled() {
        return Platform.isModLoaded("canvas");
    }

    /**
     * Returns whether Lil Tater Reloaded is installed.
     *
     * @return {@code true} if LTR is installed, else {@code false}
     */
    public static boolean isLilTaterReloadedInstalled() {
        // Don't even think about it Yog.
        return Platform.isModLoaded("ltr");
    }

    /**
     * Returns whether Sodium 0.1.0 is installed.
     *
     * @return {@code true} if Sodium 0.1.0 is installed, else {@code false}
     */
    public static boolean isSodium010Installed() {
        return Platform.getOptionalMod("sodium").map(mod -> mod.getVersion().startsWith("0.1.0"))
                .orElse(false);
    }
}
