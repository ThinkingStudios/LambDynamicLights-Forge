/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.fabric.mixin.ltr;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import org.thinkingstudio.ryoamiclights.util.LilTaterBlockEntityAccessor;

@Restriction(require = @Condition("ltr"))
@Pseudo
@Mixin(targets = "mods.ltr.registry.LilTaterBlocks", remap = false)
public class LilTaterBlocksMixin {
	@Accessor("LIL_TATER_BLOCK_ENTITY")
	private static BlockEntityType<?> getLilTaterBlockEntity() {
		throw new IllegalStateException("Accessor did not apply properly.");
	}

	@Dynamic
	@Inject(method = "init", at = @At("TAIL"))
	private static void onInit(CallbackInfo ci) {
		DynamicLightHandlers.registerDynamicLightHandler(getLilTaterBlockEntity(), entity -> {
			int luminance = 0;
			if (!((LilTaterBlockEntityAccessor) entity).ryoamiclights$isEmpty()) {
				for (ItemStack item : ((LilTaterBlockEntityAccessor) entity).ryoamiclights$getItems()) {
					luminance = Math.max(luminance, RyoamicLights.getLuminanceFromItemStack(item, !entity.getCachedState().getFluidState().isEmpty()));
				}
			}
			return luminance;
		});
	}
}
