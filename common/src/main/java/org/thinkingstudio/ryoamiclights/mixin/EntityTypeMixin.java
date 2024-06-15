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

import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.accessor.DynamicLightHandlerHolder;
import org.thinkingstudio.ryoamiclights.api.DynamicLightHandler;
import org.thinkingstudio.ryoamiclights.config.LightSourceSettingEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements DynamicLightHandlerHolder<T> {
	@Shadow
	public abstract Text getName();

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
			var self = (EntityType<?>) (Object) this;
			var id = Registries.ENTITY_TYPE.getId(self);
			if (id.getNamespace().equals("minecraft") && id.getPath().equals("pig") && self != EntityType.PIG) {
				return null;
			}

			this.ryoamiclights$setting = new LightSourceSettingEntry("light_sources.settings.entities."
					+ id.getNamespace() + '.' + id.getPath().replace('/', '.'),
					true, null, null);
			RyoamicLights.get().config.load(this.ryoamiclights$setting);
		}

		return this.ryoamiclights$setting;
	}

	@Override
	public Text ryoamiclights$getName() {
		var name = this.getName();
		if (name == null) {
			return Text.translatable("ryoamiclights.dummy");
		}
		return name;
	}
}
