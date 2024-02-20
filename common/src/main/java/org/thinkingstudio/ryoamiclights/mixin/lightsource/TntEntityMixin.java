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
import org.thinkingstudio.ryoamiclights.ExplosiveLightingMode;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
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
public abstract class TntEntityMixin extends Entity implements DynamicLightSource
{
    @Shadow
    private int fuseTimer;

    @Unique
    private double ryoamiclights_startFuseTimer = 80.0;
    @Unique
    private int ryoamiclights_luminance;

    public TntEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void onNew(EntityType<? extends TntEntity> entityType, World world, CallbackInfo ci)
    {
        this.ryoamiclights_startFuseTimer = this.fuseTimer;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci)
    {
        // We do not want to update the entity on the server.
        if (this.getEntityWorld().isClient()) {
            if (!RyoamicLights.get().config.getTntLightingMode().isEnabled())
                return;

            if (this.removed) {
                this.ryoamicLights$setDynamicLightEnabled(false);
            } else {
                this.ryoamicLights$dynamicLightTick();
                RyoamicLights.updateTracking(this);
            }
        }
    }

    @Override
    public void ryoamicLights$dynamicLightTick()
    {
        if (this.isOnFire()) {
            this.ryoamiclights_luminance = 15;
        } else {
            ExplosiveLightingMode lightingMode = RyoamicLights.get().config.getTntLightingMode();
            if (lightingMode == ExplosiveLightingMode.FANCY) {
                double fuse = this.fuseTimer / this.ryoamiclights_startFuseTimer;
                this.ryoamiclights_luminance = (int) (-(fuse * fuse) * 10.0) + 10;
            } else {
                this.ryoamiclights_luminance = 10;
            }
        }
    }

    @Override
    public int ryoamicLights$getLuminance()
    {
        return this.ryoamiclights_luminance;
    }
}
