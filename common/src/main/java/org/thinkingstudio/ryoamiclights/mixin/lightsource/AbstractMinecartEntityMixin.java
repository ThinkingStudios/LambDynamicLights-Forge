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
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds the tick method for dynamic light source tracking in minecart entities.
 *
 * @author LambdAurora
 * @version 2.0.2
 * @since 1.3.2
 */
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements DynamicLightSource {
	@Shadow
	public abstract BlockState getContainedBlock();

	@Unique
	private int ryoamiclights$luminance;

	public AbstractMinecartEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getWorld().isClient()) {
			if (this.isRemoved()) {
				this.ryoamicLights$setDynamicLightEnabled(false);
			} else {
				if (!RyoamicLights.get().config.getEntitiesLightSource().get() || !DynamicLightHandlers.canLightUp(this))
					this.ryoamiclights$luminance = 0;
				else
					this.ryoamicLights$dynamicLightTick();
				RyoamicLights.updateTracking(this);
			}
		}
	}

	@Override
	public void ryoamicLights$dynamicLightTick() {
		this.ryoamiclights$luminance = Math.max(
				Math.max(
						this.isOnFire() ? 15 : 0,
						this.getContainedBlock().getLuminance()
				),
				DynamicLightHandlers.getLuminanceFrom(this)
		);
	}

	@Override
	public int ryoamicLights$getLuminance() {
		return this.ryoamiclights$luminance;
	}
}
