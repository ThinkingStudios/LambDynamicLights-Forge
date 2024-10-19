/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.mixin.lightsource;

import org.thinkingstudio.ryoamiclights.DynamicLightSource;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource {
	@Unique
	protected int ryoamiclights$luminance;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public void ryoamicLights$dynamicLightTick() {
		if (!RyoamicLights.get().config.getEntitiesLightSource().get() || !DynamicLightHandlers.canLightUp(this)) {
			this.ryoamiclights$luminance = 0;
			return;
		}

		if (this.isOnFire() || this.isGlowing()) {
			this.ryoamiclights$luminance = 15;
		} else {
			this.ryoamiclights$luminance = RyoamicLights.getLivingEntityLuminanceFromItems((LivingEntity) (Object) this);
		}

		int luminance = DynamicLightHandlers.getLuminanceFrom(this);
		if (luminance > this.ryoamiclights$luminance)
			this.ryoamiclights$luminance = luminance;
	}

	@Override
	public int ryoamicLights$getLuminance() {
		return this.ryoamiclights$luminance;
	}
}
