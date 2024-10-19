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

import org.thinkingstudio.ryoamiclights.services.ModPlatform;

/**
 * Represents a utility class for compatibility.
 *
 * @author TexTrue
 * @version 0.2.12
 * @since 0.1.0
 */
public final class RyoamicLightsCompat {
	/**
	 * Returns whether Canvas is installed.
	 *
	 * @return {@code true} if Canvas is installed, else {@code false}
	 */
	public static boolean isCanvasInstalled() {
		return ModPlatform.getInstance().isModLoaded("canvas");
	}
}
