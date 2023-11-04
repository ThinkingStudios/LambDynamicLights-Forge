/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdynlights;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.maven.artifact.versioning.*;

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
		return FMLLoader.getLoadingModList().getModFileById("canvas") != null;
	}

	/**
	 * Returns whether Lil Tater Reloaded is installed.
	 *
	 * @return {@code true} if LTR is installed, else {@code false}
	 */
	public static boolean isLilTaterReloadedInstalled() {
		// Don't even think about it Yog.
		return FMLLoader.getLoadingModList().getModFileById("ltr") != null;
	}

	/**
	 * Returns whether Rubidium 0.7.x and above is installed.
	 *
	 * @return {@code true} if Rubidium 0.7.x and above is installed, else {@code false}
	 */
	public static boolean isRubidium07XInstalled() {
        return ModList.get().getModContainerById("rubidium").map(modContainer -> {
            return modContainer.getModInfo().getVersion().compareTo(new DefaultArtifactVersion("0.7.0")) >= 0;
        }).orElse(false);
	}

	public static boolean isForgifiedFabricApiInstalled() {
		return FMLLoader.getLoadingModList().getModFileById("fabric_api") != null;
	}
}
