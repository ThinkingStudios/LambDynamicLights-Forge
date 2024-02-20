/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.api;

import org.thinkingstudio.ryoamiclights.RyoamicLights;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a dynamic light handler.
 *
 * @param <T> The type of the light source.
 * @author LambdAurora
 * @version 1.3.0
 * @since 1.1.0
 */
public interface DynamicLightHandler<T>
{
    /**
     * Returns the luminance of the light source.
     *
     * @param lightSource The light source.
     * @return The luminance.
     */
    int getLuminance(T lightSource);

    /**
     * Returns whether the light source is water-sensitive or not.
     *
     * @param lightSource The light source.
     * @return True if the light source is water-sensitive, else false.
     */
    default boolean isWaterSensitive(T lightSource)
    {
        return false;
    }

    /**
     * Returns a dynamic light handler.
     *
     * @param luminance      The luminance function.
     * @param waterSensitive The water sensitive function.
     * @param <T>            The type of the entity.
     * @return The completed handler.
     */
    static <T extends LivingEntity> @NotNull DynamicLightHandler<T> makeHandler(Function<T, Integer> luminance, Function<T, Boolean> waterSensitive)
    {
        return new DynamicLightHandler<T>()
        {
            @Override
            public int getLuminance(T lightSource)
            {
                return luminance.apply(lightSource);
            }

            @Override
            public boolean isWaterSensitive(T lightSource)
            {
                return waterSensitive.apply(lightSource);
            }
        };
    }

    /**
     * Returns a living entity dynamic light handler.
     *
     * @param handler The handler.
     * @param <T>     The type of the entity.
     * @return The completed handler.
     */
    static <T extends LivingEntity> @NotNull DynamicLightHandler<T> makeLivingEntityHandler(@NotNull DynamicLightHandler<T> handler)
    {
        return entity -> {
            int luminance = 0;
            for (ItemStack equipped : entity.getItemsEquipped()) {
                luminance = Math.max(luminance, RyoamicLights.getLuminanceFromItemStack(equipped, entity.isSubmergedInWater()));
            }
            return Math.max(luminance, handler.getLuminance(entity));
        };
    }

    /**
     * Returns a Creeper dynamic light handler.
     *
     * @param handler Extra handler.
     * @param <T>     The type of Creeper entity.
     * @return The completed handler.
     */
    static <T extends CreeperEntity> @NotNull DynamicLightHandler<T> makeCreeperEntityHandler(@Nullable DynamicLightHandler<T> handler)
    {
        return new DynamicLightHandler<T>()
        {
            @Override
            public int getLuminance(T entity)
            {
                int luminance = 0;

                if (entity.getClientFuseTime(0.0F) > 0.001D) {
                    switch (RyoamicLights.get().config.getCreeperLightingMode()) {
                        case OFF:
                            return 0;
                        case SIMPLE:
                            luminance = 10;
                            break;
                        case FANCY:
                            luminance = (int) (entity.getClientFuseTime(0.0F) * 10.0);
                            break;
                    }
                }

                if (handler != null)
                    luminance = Math.max(luminance, handler.getLuminance(entity));

                return luminance;
            }

            @Override
            public boolean isWaterSensitive(T lightSource)
            {
                return true;
            }
        };
    }
}
