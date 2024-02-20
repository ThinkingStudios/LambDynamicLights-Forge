/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.mixin.lightsource;

import org.thinkingstudio.ryoamiclights.DynamicLightSource;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.api.DynamicLightHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource
{
    @Unique
    private int ryoamiclights_luminance;

    public LivingEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Override
    public void ryoamicLights$dynamicLightTick()
    {
        if (this.isOnFire() || this.isGlowing()) {
            this.ryoamiclights_luminance = 15;
        } else {
            int luminance = 0;
            BlockPos eyePos = new BlockPos(this.getX(), this.getEyeY(), this.getZ());
            boolean submergedInFluid = !this.world.getFluidState(eyePos).isEmpty();
            for (ItemStack equipped : this.getItemsEquipped()) {
                if (!equipped.isEmpty())
                    luminance = Math.max(luminance, RyoamicLights.getLuminanceFromItemStack(equipped, submergedInFluid));
            }

            this.ryoamiclights_luminance = luminance;
        }

        int luminance = DynamicLightHandlers.getLuminanceFrom(this);
        if (luminance > this.ryoamiclights_luminance)
            this.ryoamiclights_luminance = luminance;

        if (!RyoamicLights.get().config.hasEntitiesLightSource() && this.getType() != EntityType.PLAYER)
            this.ryoamiclights_luminance = 0;
    }

    @Override
    public int ryoamicLights$getLuminance()
    {
        return this.ryoamiclights_luminance;
    }
}
