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

import dev.architectury.injectables.annotations.ExpectPlatform;

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
	@ExpectPlatform
	public static boolean isCanvasInstalled() {
		throw new AssertionError();
	}

	/**
	 * Returns whether Lil Tater Reloaded is installed.
	 *
	 * @return {@code true} if LTR is installed, else {@code false}
	 */
	@ExpectPlatform
	public static boolean isLilTaterReloadedInstalled() {
		throw new AssertionError();
	}

	/**
	 * Returns whether Sodium(Fabric) or Embeddium(Forge/NeoForge) is installed.
	 *
	 * @return {@code true} if Sodium(Fabric) or Embeddium(Forge/NeoForge) is installed, else {@code false}
	 */
	@ExpectPlatform
	public static boolean isSodiumInstalled() {
		throw new AssertionError();
	}

	/**
	 * Returns whether Fabric API or Forgified Fabric API is installed.
	 *
	 * @return {@code true} if Fabric API or Forgified Fabric API is installed, else {@code false}
	 */
	@ExpectPlatform
	public static boolean isFabricApiInstalled() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static boolean isDevEnvironment() {
		throw new AssertionError();
	}
}
