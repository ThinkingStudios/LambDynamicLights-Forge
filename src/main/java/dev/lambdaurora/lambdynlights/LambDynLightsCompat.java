/*
 * Copyright © 2020-2022 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdynlights;

import dev.architectury.platform.Platform;

/**
 * Represents a utility class for compatibility.
 *
 * @author LambdAurora
 * @version 1.3.3
 * @since 1.0.0
 */
public final class LambDynLightsCompat {
	/**
	 * Returns whether Ars Nouveau is installed.
	 *
	 * @return {@code true} if Ars Nouveau is installed, else {@code false}
	 */
	public static boolean isArsNouveauInstalled() {
		return Platform.isModLoaded("ars_nouveau");
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
}
