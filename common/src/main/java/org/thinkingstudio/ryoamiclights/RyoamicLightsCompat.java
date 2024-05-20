/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights;

/**
 * Represents a utility class for compatibility.
 *
 * @author LambdAurora
 * @version 1.3.3
 * @since 1.0.0
 */
public final class RyoamicLightsCompat {
    /**
     * Returns whether Canvas is installed.
     *
     * @return {@code true} if Canvas is installed, else {@code false}
     */
    public static boolean isCanvasInstalled() {
        return ModPlatform.isModLoaded("canvas");
    }

    /**
     * Returns whether Lil Tater Reloaded is installed.
     *
     * @return {@code true} if LTR is installed, else {@code false}
     */
    public static boolean isLilTaterReloadedInstalled() {
        return ModPlatform.isModLoaded("ltr");
    }
}