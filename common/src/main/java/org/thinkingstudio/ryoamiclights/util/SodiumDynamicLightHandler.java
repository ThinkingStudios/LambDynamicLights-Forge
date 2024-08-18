/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.util;

import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface SodiumDynamicLightHandler {
	// Stores the current light position being used by ArrayLightDataCache#get
	// We use ThreadLocal because Sodium's chunk builder is multithreaded, otherwise it will break
	// catastrophically.
	ThreadLocal<BlockPos.Mutable> pos = ThreadLocal.withInitial(BlockPos.Mutable::new);

	static int getLightmap(BlockPos pos, int word, int lightmap) {
		if (!RyoamicLights.get().config.getDynamicLightsMode().isEnabled())
			return lightmap;

		// Equivalent to world.getBlockState(pos).isOpaqueFullCube(world, pos)
		if ((word >>> 30 & 1) != 0)
			return lightmap;

		double dynamic = RyoamicLights.get().getDynamicLightLevel(pos);
		return RyoamicLights.get().getLightmapWithDynamicLight(dynamic, lightmap);
	}
}
