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

import com.llamalad7.mixinextras.sugar.Local;
import org.thinkingstudio.ryoamiclights.DynamicLightSource;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin {
	@Shadow
	public abstract boolean isClient();

	@Shadow
	public abstract @Nullable BlockEntity getBlockEntity(BlockPos pos);

	@Inject(
			method = "tickBlockEntities",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/chunk/BlockEntityTickInvoker;tick()V",
					shift = At.Shift.BEFORE
			)
	)
	private void onBlockEntityTick(CallbackInfo ci, @Local boolean isRemoved, @Local BlockEntityTickInvoker blockEntityTickInvoker) {
		if (this.isClient() && RyoamicLights.get().config.getBlockEntitiesLightSource().get() && !isRemoved) {
			var blockEntity = this.getBlockEntity(blockEntityTickInvoker.getPos());
			if (blockEntity != null)
				((DynamicLightSource) blockEntity).ryoamicLights$dynamicLightTick();
		}
	}
}
