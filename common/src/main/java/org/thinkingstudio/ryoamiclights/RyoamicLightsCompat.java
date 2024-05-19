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

	/**
	 * Returns whether Sodium is installed.
	 *
	 * @return {@code true} if Sodium is installed, else {@code false}
	 */
	public static boolean isSodiumInstalled() {
		return ModPlatform.isModLoaded("sodium") != ModPlatform.isModLoaded("embeddium");
	}

	/**
	 * Returns whether Fabric API or Forgified Fabric API is installed.
	 *
	 * @return {@code true} if Fabric API or Forgified Fabric API is installed, else {@code false}
	 */
	public static boolean isFabricApiInstalled() {
		return ModPlatform.isModLoaded("fabric-api") | ModPlatform.isModLoaded("fabric_api");
	}
}
