/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.mixin;

import net.minecraft.text.LiteralText;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.accessor.DynamicLightHandlerHolder;
import org.thinkingstudio.ryoamiclights.api.DynamicLightHandler;
import org.thinkingstudio.ryoamiclights.config.LightSourceSettingEntry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> implements DynamicLightHandlerHolder<T> {
	@Unique
	private DynamicLightHandler<T> ryoamiclights$lightHandler;
	@Unique
	private LightSourceSettingEntry ryoamiclights$setting;

	@Override
	public @Nullable DynamicLightHandler<T> ryoamiclights$getDynamicLightHandler() {
		return this.ryoamiclights$lightHandler;
	}

	@Override
	public void ryoamiclights$setDynamicLightHandler(DynamicLightHandler<T> handler) {
		this.ryoamiclights$lightHandler = handler;
	}

	@Override
	public LightSourceSettingEntry ryoamiclights$getSetting() {
		if (this.ryoamiclights$setting == null) {
			var self = (BlockEntityType<?>) (Object) this;
			var id = Registry.BLOCK_ENTITY_TYPE.getId(self);
			if (id == null) {
				return null;
			}

			this.ryoamiclights$setting = new LightSourceSettingEntry("light_sources.settings.block_entities."
					+ id.getNamespace() + '.' + id.getPath().replace('/', '.'),
					true, null, null);
			RyoamicLights.get().config.load(this.ryoamiclights$setting);
		}

		return this.ryoamiclights$setting;
	}

	@Override
	public Text ryoamiclights$getName() {
		var self = (BlockEntityType<?>) (Object) this;
		var id = Registry.BLOCK_ENTITY_TYPE.getId(self);
		if (id == null) {
			return LiteralText.EMPTY;
		}
		return new LiteralText(id.getNamespace() + ':' + id.getPath());
	}
}
