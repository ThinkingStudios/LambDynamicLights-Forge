/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.api.item;

import com.google.gson.JsonParser;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an item light sources manager.
 *
 * @author LambdAurora
 * @version 2.3.2
 * @since 1.3.0
 */
public final class ItemLightSources {
	private static final JsonParser JSON_PARSER = new JsonParser();
	private static final List<ItemLightSource> ITEM_LIGHT_SOURCES = new ArrayList<>();
	private static final List<ItemLightSource> STATIC_ITEM_LIGHT_SOURCES = new ArrayList<>();

	private ItemLightSources() {
		throw new UnsupportedOperationException("ItemLightSources only contains static definitions.");
	}

	/**
	 * Loads the item light source data from resource pack.
	 *
	 * @param resourceManager The resource manager.
	 */
	public static void load(ResourceManager resourceManager) {
		ITEM_LIGHT_SOURCES.clear();

		resourceManager.findResources("dynamiclights/item", path -> path.endsWith(".json")).forEach(id -> load(resourceManager, id));

		ITEM_LIGHT_SOURCES.addAll(STATIC_ITEM_LIGHT_SOURCES);
	}

	private static void load(@NotNull ResourceManager resourceManager, @NotNull Identifier resourceId) {
		var id = new Identifier(resourceId.getNamespace(), resourceId.getPath().replace(".json", ""));
		try {
			var stream = resourceManager.getResource(resourceId).getInputStream();
			var json = JSON_PARSER.parse(new InputStreamReader(stream)).getAsJsonObject();

			ItemLightSource.fromJson(id, json).ifPresent(data -> {
				if (!STATIC_ITEM_LIGHT_SOURCES.contains(data))
					register(data);
			});
		} catch (IOException | IllegalStateException e) {
			RyoamicLights.get().warn("Failed to load item light source \"" + id + "\".");
		}
	}

	/**
	 * Registers an item light source data.
	 *
	 * @param data The item light source data.
	 */
	private static void register(ItemLightSource data) {
		for (var other : ITEM_LIGHT_SOURCES) {
			if (other.item() == data.item()) {
				RyoamicLights.get().warn("Failed to register item light source \"" + data.id() + "\", duplicates item \""
						+ Registry.ITEM.getId(data.item()) + "\" found in \"" + other.id() + "\".");
				return;
			}
		}

		ITEM_LIGHT_SOURCES.add(data);
	}

	/**
	 * Registers an item light source data.
	 *
	 * @param data the item light source data
	 */
	public static void registerItemLightSource(ItemLightSource data) {
		for (var other : STATIC_ITEM_LIGHT_SOURCES) {
			if (other.item() == data.item()) {
				RyoamicLights.get().warn("Failed to register item light source \"" + data.id() + "\", duplicates item \""
						+ Registry.ITEM.getId(data.item()) + "\" found in \"" + other.id() + "\".");
				return;
			}
		}

		STATIC_ITEM_LIGHT_SOURCES.add(data);
	}

	/**
	 * Returns the luminance of the item in the stack.
	 *
	 * @param stack the item stack
	 * @param submergedInWater {@code true} if the stack is submerged in water, else {@code false}
	 * @return a luminance value
	 */
	public static int getLuminance(ItemStack stack, boolean submergedInWater) {
		for (var data : ITEM_LIGHT_SOURCES) {
			if (data.item() == stack.getItem()) {
				return data.getLuminance(stack, submergedInWater);
			}
		}
		if (stack.getItem() instanceof BlockItem blockItem)
			return ItemLightSource.BlockItemLightSource.getLuminance(stack, blockItem.getBlock().getDefaultState());
		else return 0;
	}
}
