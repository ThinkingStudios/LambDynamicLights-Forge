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
import org.thinkingstudio.ryoamiclights.ExplosiveLightingMode;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity implements DynamicLightSource {
	@Shadow
	public abstract int getFuse();

	@Unique
	private int startFuseTimer = 80;
	@Unique
	private int ryoamiclights$luminance;

	public TntEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
	private void onNew(EntityType<? extends TntEntity> entityType, World world, CallbackInfo ci) {
		this.startFuseTimer = this.getFuse();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getWorld().isClient()) {
			if (!RyoamicLights.get().config.getTntLightingMode().isEnabled())
				return;

			if (this.isRemoved()) {
				this.ryoamicLights$setDynamicLightEnabled(false);
			} else {
				if (!RyoamicLights.get().config.getEntitiesLightSource().get() || !DynamicLightHandlers.canLightUp(this))
					this.ryoamicLights$resetDynamicLight();
				else
					this.ryoamicLights$dynamicLightTick();
				RyoamicLights.updateTracking(this);
			}
		}
	}

	@Override
	public void ryoamicLights$dynamicLightTick() {
		if (this.isOnFire()) {
			this.ryoamiclights$luminance = 15;
		} else {
			ExplosiveLightingMode lightingMode = RyoamicLights.get().config.getTntLightingMode();
			if (lightingMode == ExplosiveLightingMode.FANCY) {
				var fuse = this.getFuse() / this.startFuseTimer;
				this.ryoamiclights$luminance = (int) (-(fuse * fuse) * 10.0) + 10;
			} else {
				this.ryoamiclights$luminance = 10;
			}
		}
	}

	@Override
	public int ryoamicLights$getLuminance() {
		return this.ryoamiclights$luminance;
	}
}
