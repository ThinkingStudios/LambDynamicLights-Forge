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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
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

import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements DynamicLightSource
{
    @Shadow
    protected BlockPos pos;

    @Shadow
    @Nullable
    protected World world;

    @Shadow
    protected boolean removed;
    @Unique
    private int ryoamiclights_luminance = 0;
    @Unique
    private int ryoamiclights_lastLuminance = 0;
    @Unique
    private long ryoamiclights_lastUpdate = 0;
    @Unique
    private final LongOpenHashSet ryoamicLights$trackedLitChunkPos = new LongOpenHashSet();

    @Override
    public double ryoamicLights$getDynamicLightX()
    {
        return this.pos.getX() + 0.5;
    }

    @Override
    public double ryoamicLights$getDynamicLightY()
    {
        return this.pos.getY() + 0.5;
    }

    @Override
    public double ryoamicLights$getDynamicLightZ()
    {
        return this.pos.getZ() + 0.5;
    }

    @Override
    public World ryoamicLights$getDynamicLightWorld()
    {
        return this.world;
    }

    @Inject(method = "markRemoved", at = @At("TAIL"))
    private void onRemoved(CallbackInfo ci)
    {
        this.ryoamicLights$setDynamicLightEnabled(false);
    }

    @Override
    public void ryoamicLights$resetDynamicLight()
    {
        this.ryoamiclights_lastLuminance = 0;
    }

    @Override
    public void ryoamicLights$dynamicLightTick()
    {
        // We do not want to update the entity on the server.
        if (this.world == null || !this.world.isClient())
            return;
        if (!this.removed) {
            this.ryoamiclights_luminance = DynamicLightHandlers.getLuminanceFrom((BlockEntity) (Object) this);
            RyoamicLights.updateTracking(this);

            if (!this.ryoamicLights$isDynamicLightEnabled()) {
                this.ryoamiclights_lastLuminance = 0;
            }
        }
    }

    @Override
    public int ryoamicLights$getLuminance()
    {
        return this.ryoamiclights_luminance;
    }

    @Override
    public boolean ryoamicLights$shouldUpdateDynamicLight()
    {
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
    public boolean ryoamicLights$updateDynamicLight(@NotNull WorldRenderer renderer)
    {
        if (!this.ryoamicLights$shouldUpdateDynamicLight())
            return false;

        int luminance = this.ryoamicLights$getLuminance();

        if (luminance != this.ryoamiclights_lastLuminance) {
            this.ryoamiclights_lastLuminance = luminance;

            if (this.ryoamicLights$trackedLitChunkPos.isEmpty()) {
                BlockPos.Mutable chunkPos = new BlockPos.Mutable(MathHelper.floorDiv(this.pos.getX(), 16),
                        MathHelper.floorDiv(this.pos.getY(), 16),
                        MathHelper.floorDiv(this.pos.getZ(), 16));

                RyoamicLights.updateTrackedChunks(chunkPos, null, this.ryoamicLights$trackedLitChunkPos);

                Direction directionX = (this.pos.getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
                Direction directionY = (this.pos.getY() & 15) >= 8 ? Direction.UP : Direction.DOWN;
                Direction directionZ = (this.pos.getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

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
                    RyoamicLights.updateTrackedChunks(chunkPos, null, this.ryoamicLights$trackedLitChunkPos);
                }
            }

            // Schedules the rebuild of chunks.
            this.ryoamicLights$scheduleTrackedChunksRebuild(renderer);
            return true;
        }
        return false;
    }

    @Override
    public void ryoamicLights$scheduleTrackedChunksRebuild(@NotNull WorldRenderer renderer)
    {
        if (this.world == MinecraftClient.getInstance().world) {
            for (long pos : this.ryoamicLights$trackedLitChunkPos) {
                RyoamicLights.scheduleChunkRebuild(renderer, pos);
            }
        }
    }
}
