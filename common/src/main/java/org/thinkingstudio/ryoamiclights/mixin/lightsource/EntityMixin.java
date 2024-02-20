/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.mixin.lightsource;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.thinkingstudio.ryoamiclights.DynamicLightSource;
import org.thinkingstudio.ryoamiclights.DynamicLightsMode;
import org.thinkingstudio.ryoamiclights.RyoamicLights;
import org.thinkingstudio.ryoamiclights.api.DynamicLightHandlers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements DynamicLightSource {
    @Shadow
    public World world;

    @Shadow
    public boolean removed;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getEyeY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Shadow
    public int chunkX;
    @Shadow
    public int chunkZ;

    @Shadow
    public abstract boolean isOnFire();

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Unique
    private int ryoamiclights_luminance = 0;
    @Unique
    private int ryoamiclights_lastLuminance = 0;
    @Unique
    private long ryoamiclights_lastUpdate = 0;
    @Unique
    private double ryoamiclights_prevX;
    @Unique
    private double ryoamiclights_prevY;
    @Unique
    private double ryoamiclights_prevZ;
    @Unique
    private LongOpenHashSet ryoamicLights$trackedLitChunkPos = new LongOpenHashSet();

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci) {
        // We do not want to update the entity on the server.
        if (this.world.isClient()) {
            if (this.removed) {
                this.ryoamicLights$setDynamicLightEnabled(false);
            } else {
                this.ryoamicLights$dynamicLightTick();
                if (!RyoamicLights.get().config.hasEntitiesLightSource() && this.getType() != EntityType.PLAYER)
                    this.ryoamiclights_luminance = 0;
                RyoamicLights.updateTracking(this);
            }
        }
    }

    @Inject(method = "remove", at = @At("TAIL"))
    public void onRemove(CallbackInfo ci) {
        if (this.world.isClient())
            this.ryoamicLights$setDynamicLightEnabled(false);
    }

    @Override
    public double ryoamicLights$getDynamicLightX() {
        return this.getX();
    }

    @Override
    public double ryoamicLights$getDynamicLightY() {
        return this.getEyeY();
    }

    @Override
    public double ryoamicLights$getDynamicLightZ() {
        return this.getZ();
    }

    @Override
    public World ryoamicLights$getDynamicLightWorld() {
        return this.world;
    }

    @Override
    public void ryoamicLights$resetDynamicLight() {
        this.ryoamiclights_lastLuminance = 0;
    }

    @Override
    public boolean ryoamicLights$shouldUpdateDynamicLight() {
        DynamicLightsMode mode = RyoamicLights.get().config.getDynamicLightsMode();
        if (!mode.isEnabled())
            return false;
        if (mode.hasDelay()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime < this.ryoamiclights_lastUpdate + mode.getDelay()) {
                return false;
            }

            this.ryoamiclights_lastUpdate = currentTime;
        }
        return true;
    }

    @Override
    public void ryoamicLights$dynamicLightTick() {
        this.ryoamiclights_luminance = this.isOnFire() ? 15 : 0;

        int luminance = DynamicLightHandlers.getLuminanceFrom((Entity) (Object) this);
        if (luminance > this.ryoamiclights_luminance)
            this.ryoamiclights_luminance = luminance;
    }

    @Override
    public int ryoamicLights$getLuminance() {
        return this.ryoamiclights_luminance;
    }

    @Override
    public boolean ryoamicLights$updateDynamicLight(@NotNull WorldRenderer renderer) {
        if (!this.ryoamicLights$shouldUpdateDynamicLight())
            return false;
        double deltaX = this.getX() - this.ryoamiclights_prevX;
        double deltaY = this.getY() - this.ryoamiclights_prevY;
        double deltaZ = this.getZ() - this.ryoamiclights_prevZ;

        int luminance = this.ryoamicLights$getLuminance();

        if (Math.abs(deltaX) > 0.1D || Math.abs(deltaY) > 0.1D || Math.abs(deltaZ) > 0.1D || luminance != this.ryoamiclights_lastLuminance) {
            this.ryoamiclights_prevX = this.getX();
            this.ryoamiclights_prevY = this.getY();
            this.ryoamiclights_prevZ = this.getZ();
            this.ryoamiclights_lastLuminance = luminance;

            LongOpenHashSet newPos = new LongOpenHashSet();

            if (luminance > 0) {
                BlockPos.Mutable chunkPos = new BlockPos.Mutable(this.chunkX, MathHelper.floorDiv((int) this.getEyeY(), 16), this.chunkZ);

                RyoamicLights.scheduleChunkRebuild(renderer, chunkPos);
                RyoamicLights.updateTrackedChunks(chunkPos, this.ryoamicLights$trackedLitChunkPos, newPos);

                Direction directionX = (this.getBlockPos().getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
                Direction directionY = (MathHelper.fastFloor(this.getEyeY()) & 15) >= 8 ? Direction.UP : Direction.DOWN;
                Direction directionZ = (this.getBlockPos().getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

                for (int i = 0; i < 7; i++) {
                    if (i % 4 == 0) {
                        chunkPos.move(directionX); // X
                    } else if (i % 4 == 1) {
                        chunkPos.move(directionZ); // XZ
                    } else if (i % 4 == 2) {
                        chunkPos.move(directionX.getOpposite()); // Z
                    } else {
                        chunkPos.move(directionZ.getOpposite()); // origin
                        chunkPos.move(directionY); // Y
                    }
                    RyoamicLights.scheduleChunkRebuild(renderer, chunkPos);
                    RyoamicLights.updateTrackedChunks(chunkPos, this.ryoamicLights$trackedLitChunkPos, newPos);
                }
            }

            // Schedules the rebuild of removed chunks.
            this.ryoamicLights$scheduleTrackedChunksRebuild(renderer);
            // Update tracked lit chunks.
            this.ryoamicLights$trackedLitChunkPos = newPos;
            return true;
        }
        return false;
    }

    @Override
    public void ryoamicLights$scheduleTrackedChunksRebuild(@NotNull WorldRenderer renderer) {
        if (MinecraftClient.getInstance().world == this.world)
            for (long pos : this.ryoamicLights$trackedLitChunkPos) {
                RyoamicLights.scheduleChunkRebuild(renderer, pos);
            }
    }
}
