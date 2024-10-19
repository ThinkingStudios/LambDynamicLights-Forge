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
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExplosiveProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin extends Entity implements DynamicLightSource {
	public ExplosiveProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public void ryoamicLights$dynamicLightTick() {
		if (!this.ryoamicLights$isDynamicLightEnabled())
			this.ryoamicLights$setDynamicLightEnabled(true);
	}

	@Override
	public int ryoamicLights$getLuminance() {
		if (RyoamicLights.get().config.getEntitiesLightSource().get() && DynamicLightHandlers.canLightUp(this))
			return 14;
		return 0;
	}
}
