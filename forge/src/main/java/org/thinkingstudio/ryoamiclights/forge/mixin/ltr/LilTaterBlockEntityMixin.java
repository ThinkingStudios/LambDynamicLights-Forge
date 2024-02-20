/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.forge.mixin.ltr;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.thinkingstudio.ryoamiclights.util.LilTaterBlockEntityAccessor;

@Pseudo
@Mixin(targets = "mods.ltr.entities.LilTaterBlockEntity")
public abstract class LilTaterBlockEntityMixin implements Inventory, LilTaterBlockEntityAccessor {
	@Override
	public boolean ryoamiclights_isEmpty() {
		return this.isEmpty();
	}

	@Accessor(value = "items", remap = false)
	public abstract DefaultedList<ItemStack> ryoamiclights_getItems();
}
