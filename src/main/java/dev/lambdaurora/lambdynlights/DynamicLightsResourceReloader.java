package dev.lambdaurora.lambdynlights;

import dev.lambdaurora.lambdynlights.api.item.ItemLightSources;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;

public class DynamicLightsResourceReloader implements SynchronousResourceReloader {
    @Override
    public void reload(ResourceManager manager) {
        ItemLightSources.load(manager);
    }
}
