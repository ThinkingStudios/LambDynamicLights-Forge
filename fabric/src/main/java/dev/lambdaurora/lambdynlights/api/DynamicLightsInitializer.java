/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdynlights.api;

import org.thinkingstudio.ryoamiclights.api.DynamicLightHandler;
import org.thinkingstudio.ryoamiclights.api.DynamicLightHandlers;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSource;
import org.thinkingstudio.ryoamiclights.api.item.ItemLightSources;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;

/**
 * Represents the entrypoint for LDL API.
 *
 * @author LambdAurora
 * @version 1.3.2
 * @since 1.3.2
 */
@Deprecated
public interface DynamicLightsInitializer {
	/**
	 * Method called when RyoamicLights is initialized to register custom dynamic light handlers and item light sources.
	 *
	 * @see DynamicLightHandlers#registerDynamicLightHandler(EntityType, DynamicLightHandler)
	 * @see DynamicLightHandlers#registerDynamicLightHandler(BlockEntityType, DynamicLightHandler)
	 * @see ItemLightSources#registerItemLightSource(ItemLightSource)
	 */
	void onInitializeDynamicLights();
}
