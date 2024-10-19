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

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;
import org.thinkingstudio.ryoamiclights.RyoamicLights;

import java.util.List;
import java.util.Set;

public class RyoamicLightsFabricMixinPlugin extends RestrictiveMixinConfigPlugin {
    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    protected void onRestrictionCheckFailed(String mixinClassName, String reason) {
        RyoamicLights.get().warn("Apply mixin " + mixinClassName + " is disabled, " + reason);
    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }
}
