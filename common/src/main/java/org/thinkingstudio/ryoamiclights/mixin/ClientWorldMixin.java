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

import org.thinkingstudio.ryoamiclights.DynamicLightSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
	@Shadow
	protected abstract EntityLookup<Entity> getEntityLookup();

	@Inject(method = "removeEntity(ILnet/minecraft/entity/Entity$RemovalReason;)V", at = @At("HEAD"))
	private void onFinishRemovingEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
		var entity = this.getEntityLookup().get(entityId);
		if (entity != null) {
			var dls = (DynamicLightSource) entity;
			dls.ryoamicLights$setDynamicLightEnabled(false);
		}
	}
}
