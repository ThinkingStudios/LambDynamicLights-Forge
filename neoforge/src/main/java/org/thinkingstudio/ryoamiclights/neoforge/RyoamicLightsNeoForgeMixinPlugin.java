/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.neoforge;

import org.thinkingstudio.ryoamiclights.RyoamicLightsCompat;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class RyoamicLightsNeoForgeMixinPlugin implements IMixinConfigPlugin {
    private final Object2BooleanMap<String> conditionalMixins = new Object2BooleanOpenHashMap<>();

    public RyoamicLightsNeoForgeMixinPlugin() {
        boolean ltrInstalled = RyoamicLightsCompat.isLilTaterReloadedInstalled();
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.neoforge.mixin.ltr.LilTaterBlocksMixin", ltrInstalled);
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.neoforge.mixin.ltr.LilTaterBlockEntityMixin", ltrInstalled);

        boolean forgifiedFabricApiInstalled = RyoamicLightsCompat.isFabricApiInstalled();
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.neoforge.mixin.fabricapi.AoCalculatorMixin", forgifiedFabricApiInstalled);
    }

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.conditionalMixins.getOrDefault(mixinClassName, true);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
