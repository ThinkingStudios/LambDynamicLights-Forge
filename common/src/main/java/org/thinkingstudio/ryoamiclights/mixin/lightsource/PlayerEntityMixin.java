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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource
{
    @Shadow
    public abstract boolean isSpectator();

    @Unique
    private int   ryoamiclights_luminance;
    @Unique
    private World ryoamiclights_lastWorld;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
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

        if (this.isSpectator())
            this.ryoamiclights_luminance = 0;

        if (this.ryoamiclights_lastWorld != this.getEntityWorld()) {
            this.ryoamiclights_lastWorld = this.getEntityWorld();
            this.ryoamiclights_luminance = 0;
        }
    }

    @Override
    public int ryoamicLights$getLuminance()
    {
        return this.ryoamiclights_luminance;
    }
}
