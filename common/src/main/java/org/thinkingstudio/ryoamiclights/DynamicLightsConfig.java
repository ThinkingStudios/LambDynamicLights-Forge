/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights;

import com.electronwill.nightconfig.core.file.FileConfig;
import me.shedaniel.architectury.platform.Platform;
import org.thinkingstudio.obsidianui.option.SpruceCyclingOption;
import org.thinkingstudio.obsidianui.option.SpruceOption;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents the mod configuration.
 *
 * @author LambdAurora
 * @version 1.3.4
 * @since 1.0.0
 */
public class DynamicLightsConfig {
    private static final DynamicLightsMode DEFAULT_DYNAMIC_LIGHTS_MODE = DynamicLightsMode.FANCY;
    private static final boolean DEFAULT_ENTITIES_LIGHT_SOURCE = true;
    private static final boolean DEFAULT_BLOCK_ENTITIES_LIGHT_SOURCE = true;
    private static final boolean DEFAULT_WATER_SENSITIVE_CHECK = true;
    private static final ExplosiveLightingMode DEFAULT_CREEPER_LIGHTING_MODE = ExplosiveLightingMode.SIMPLE;
    private static final ExplosiveLightingMode DEFAULT_TNT_LIGHTING_MODE = ExplosiveLightingMode.OFF;

    public static final Path CONFIG_FILE_PATH = Platform.getConfigFolder().resolve("ryoamiclights.toml");
    protected final FileConfig config;
    private final RyoamicLights mod;
    private DynamicLightsMode dynamicLightsMode;
    private ExplosiveLightingMode creeperLightingMode;
    private ExplosiveLightingMode tntLightingMode;

    public final SpruceOption dynamicLightsModeOption = new SpruceCyclingOption("ryoamiclights.option.mode",
            amount -> this.setDynamicLightsMode(this.dynamicLightsMode.next()),
            option -> option.getDisplayText(this.dynamicLightsMode.getTranslatedText()),
            new TranslatableText("ryoamiclights.tooltip.mode.1")
                    .append(new LiteralText("\n"))
                    .append(new TranslatableText("ryoamiclights.tooltip.mode.2", DynamicLightsMode.FASTEST.getTranslatedText(), DynamicLightsMode.FAST.getTranslatedText()))
                    .append(new LiteralText("\n"))
                    .append(new TranslatableText("ryoamiclights.tooltip.mode.3", DynamicLightsMode.FANCY.getTranslatedText())));

    public DynamicLightsConfig(@NotNull RyoamicLights mod) {
        this.mod = mod;

        this.config = FileConfig.builder(CONFIG_FILE_PATH).concurrent()/*.defaultResource("/ryoamiclights.toml")*/.autosave().build();
    }

    /**
     * Loads the configuration.
     */
    public void load() {
        this.config.load();

        String dynamicLightsModeValue = this.config.getOrElse("mode", DEFAULT_DYNAMIC_LIGHTS_MODE.getName());
        this.dynamicLightsMode = DynamicLightsMode.byId(dynamicLightsModeValue)
                .orElse(DEFAULT_DYNAMIC_LIGHTS_MODE);
        this.creeperLightingMode = ExplosiveLightingMode.byId(this.config.getOrElse("light_sources.creeper", DEFAULT_CREEPER_LIGHTING_MODE.getName()))
                .orElse(DEFAULT_CREEPER_LIGHTING_MODE);
        this.tntLightingMode = ExplosiveLightingMode.byId(this.config.getOrElse("light_sources.tnt", DEFAULT_TNT_LIGHTING_MODE.getName()))
                .orElse(DEFAULT_TNT_LIGHTING_MODE);

        this.mod.log("Configuration loaded.");
    }

    /**
     * Saves the configuration.
     */
    public void save() {
        this.config.save();
    }

    /**
     * Resets the configuration.
     */
    public void reset() {
        this.setDynamicLightsMode(DEFAULT_DYNAMIC_LIGHTS_MODE);
        this.setEntitiesLightSource(DEFAULT_ENTITIES_LIGHT_SOURCE);
        this.setBlockEntitiesLightSource(DEFAULT_BLOCK_ENTITIES_LIGHT_SOURCE);
        this.setWaterSensitiveCheck(DEFAULT_WATER_SENSITIVE_CHECK);
        this.setCreeperLightingMode(DEFAULT_CREEPER_LIGHTING_MODE);
        this.setTntLightingMode(DEFAULT_TNT_LIGHTING_MODE);
    }

    /**
     * Returns the dynamic lights mode.
     *
     * @return the dynamic lights mode
     */
    public DynamicLightsMode getDynamicLightsMode() {
        return this.dynamicLightsMode;
    }

    /**
     * Sets the dynamic lights mode.
     *
     * @param mode the dynamic lights mode
     */
    public void setDynamicLightsMode(@NotNull DynamicLightsMode mode) {
        if (!mode.isEnabled()) {
            this.mod.clearLightSources();
        }

        this.dynamicLightsMode = mode;
        this.config.set("mode", mode.getName());
    }

    /**
     * Returns whether block entities as light source is enabled.
     *
     * @return {@code true} if block entities as light source is enabled, else {@code false}
     */
    public boolean hasEntitiesLightSource() {
        return this.config.getOrElse("light_sources.entities", DEFAULT_ENTITIES_LIGHT_SOURCE);
    }

    /**
     * Sets whether block entities as light source is enabled.
     *
     * @param enabled {@code true} if block entities as light source is enabled, else {@code false}
     */
    public void setEntitiesLightSource(boolean enabled) {
        if (!enabled)
            this.mod.removeEntitiesLightSource();
        this.config.set("light_sources.entities", enabled);
    }

    /**
     * Returns whether block entities as light source is enabled.
     *
     * @return {@code true} if block entities as light source is enabled, else {@code false}.
     */
    public boolean hasBlockEntitiesLightSource() {
        return this.config.getOrElse("light_sources.block_entities", DEFAULT_BLOCK_ENTITIES_LIGHT_SOURCE);
    }

    /**
     * Sets whether block entities as light source is enabled.
     *
     * @param enabled {@code true} if block entities as light source is enabled, else {@code false}.
     */
    public void setBlockEntitiesLightSource(boolean enabled) {
        if (!enabled)
            this.mod.removeBlockEntitiesLightSource();
        this.config.set("light_sources.block_entities", enabled);
    }

    /**
     * Returns whether water sensitive check is enabled or not.
     *
     * @return {@code true} if water sensitive check is enabled, else {@code false}
     */
    public boolean hasWaterSensitiveCheck() {
        return this.config.getOrElse("light_sources.water_sensitive_check", DEFAULT_WATER_SENSITIVE_CHECK);
    }

    /**
     * Sets whether water sensitive check is enabled or not.
     *
     * @param waterSensitive {@code true} if water sensitive check is enabled, else {@code false}
     */
    public void setWaterSensitiveCheck(boolean waterSensitive) {
        this.config.set("light_sources.water_sensitive_check", waterSensitive);
    }

    /**
     * Returns the Creeper dynamic lighting mode.
     *
     * @return the Creeper dynamic lighting mode
     */
    public ExplosiveLightingMode getCreeperLightingMode() {
        return this.creeperLightingMode;
    }

    /**
     * Sets the Creeper dynamic lighting mode.
     *
     * @param lightingMode the Creeper dynamic lighting mode
     */
    public void setCreeperLightingMode(@NotNull ExplosiveLightingMode lightingMode) {
        this.creeperLightingMode = lightingMode;

        if (!lightingMode.isEnabled())
            this.mod.removeCreeperLightSources();
        this.config.set("light_sources.creeper", lightingMode.getName());
    }

    /**
     * Returns the TNT dynamic lighting mode.
     *
     * @return the TNT dynamic lighting mode
     */
    public ExplosiveLightingMode getTntLightingMode() {
        return this.tntLightingMode;
    }

    /**
     * Sets the TNT dynamic lighting mode.
     *
     * @param lightingMode the TNT dynamic lighting mode
     */
    public void setTntLightingMode(@NotNull ExplosiveLightingMode lightingMode) {
        this.tntLightingMode = lightingMode;

        if (!lightingMode.isEnabled())
            this.mod.removeTntLightSources();
        this.config.set("light_sources.tnt", lightingMode.getName());
    }
}
