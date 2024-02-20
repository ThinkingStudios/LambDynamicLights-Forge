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
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ExplosiveProjectileEntity.class)
public abstract class ExplosiveProjectileEntityMixin implements DynamicLightSource
{
    @Override
    public void ryoamicLights$dynamicLightTick()
    {
        if (!this.ryoamicLights$isDynamicLightEnabled())
            this.ryoamicLights$setDynamicLightEnabled(true);
    }

    @Override
    public int ryoamicLights$getLuminance()
    {
        if (RyoamicLights.get().config.hasEntitiesLightSource())
            return 14;
        return 0;
    }
}
