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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource {
	@Shadow
	public abstract boolean isSpectator();

	@Unique
	protected int ryoamiclights$luminance;
	@Unique
	private World ryoamiclights$lastWorld;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void ryoamicLights$dynamicLightTick() {
		if (!DynamicLightHandlers.canLightUp(this)) {
			this.ryoamiclights$luminance = 0;
			return;
		}

		if (this.isOnFire() || this.isGlowing()) {
			this.ryoamiclights$luminance = 15;
		} else {
			this.ryoamiclights$luminance = Math.max(
					DynamicLightHandlers.getLuminanceFrom(this),
					RyoamicLights.getLivingEntityLuminanceFromItems(this)
			);
		}

		if (this.isSpectator())
			this.ryoamiclights$luminance = 0;

		if (this.ryoamiclights$lastWorld != this.getWorld()) {
			this.ryoamiclights$lastWorld = this.getWorld();
			this.ryoamiclights$luminance = 0;
		}
	}

	@Override
	public int ryoamicLights$getLuminance() {
		return this.ryoamiclights$luminance;
	}
}
