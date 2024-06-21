/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights.fabric;

import net.fabricmc.loader.api.FabricLoader;
import org.thinkingstudio.ryoamiclights.RyoamicLightsCompat;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class RyoamicLightsFabricMixinPlugin  implements IMixinConfigPlugin {
    private final Object2BooleanMap<String> conditionalMixins = new Object2BooleanOpenHashMap<>();

    public RyoamicLightsFabricMixinPlugin() {
        boolean ltrInstalled = RyoamicLightsCompat.isLilTaterReloadedInstalled();
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.fabric.mixin.ltr.LilTaterBlocksMixin", ltrInstalled);
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.fabric.mixin.ltr.LilTaterBlockEntityMixin", ltrInstalled);

        boolean sodiumInstalled = RyoamicLightsCompat.isSodiumInstalled();
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.fabric.mixin.sodium.ArrayLightDataCacheMixin", sodiumInstalled);
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.fabric.mixin.sodium.FlatLightPipelineMixin", sodiumInstalled);
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.fabric.mixin.sodium.LightDataAccessMixin", sodiumInstalled);

        boolean fabricApiInstalled = RyoamicLightsCompat.isFabricApiInstalled();
        this.conditionalMixins.put("org.thinkingstudio.ryoamiclights.fabric.mixin.fabricapi.AoCalculatorMixin", fabricApiInstalled);

        boolean indiumInstalled = FabricLoader.getInstance().isModLoaded("indium") != FabricLoader.getInstance().isModLoaded("embeddium");
        this.conditionalMixins.put("dev.lambdaurora.lambdynlights.mixin.indium.TerrainRenderContextMixin", indiumInstalled);
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
