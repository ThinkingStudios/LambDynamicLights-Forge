/*
 * Copyright © 2020~2024 LambdAurora <email@lambdaurora.dev>
 * Copyright © 2024 ThinkingStudio
 *
 * This file is part of RyoamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package org.thinkingstudio.ryoamiclights;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;
import org.thinkingstudio.obsidianui.option.SpruceCyclingOption;
import org.thinkingstudio.obsidianui.option.SpruceOption;
import org.thinkingstudio.ryoamiclights.config.BooleanSettingEntry;
import org.thinkingstudio.ryoamiclights.config.SettingEntry;
import org.thinkingstudio.ryoamiclights.services.ModPlatform;
import org.thinkingstudio.ryoamiclights.services.interfaces.ModPlatformService;

import java.nio.file.Path;

/**
 * Represents the mod configuration.
 *
 * @author TexTrue
 * @version 0.2.12
 * @since 0.2.12
 */
public class DynamicLightsConfig {
    private static final DynamicLightsMode DEFAULT_DYNAMIC_LIGHTS_MODE = DynamicLightsMode.FANCY;
    private static final boolean DEFAULT_ENTITIES_LIGHT_SOURCE = true;
    private static final boolean DEFAULT_SELF_LIGHT_SOURCE = true;
    private static final boolean DEFAULT_BLOCK_ENTITIES_LIGHT_SOURCE = true;
    private static final boolean DEFAULT_WATER_SENSITIVE_CHECK = true;
    private static final ExplosiveLightingMode DEFAULT_CREEPER_LIGHTING_MODE = ExplosiveLightingMode.SIMPLE;
    private static final ExplosiveLightingMode DEFAULT_TNT_LIGHTING_MODE = ExplosiveLightingMode.OFF;

    public static final Path CONFIG_FILE_PATH = ModPlatform.getInstance().getConfigDir().resolve("ryoamiclights.toml");
    protected final CommentedFileConfig config;
    private final RyoamicLights mod;
    private static BooleanSettingEntry entitiesLightSourceSettingEntry;
    private static BooleanSettingEntry selfLightSourceSettingEntry;
    private static BooleanSettingEntry blockEntitiesLightSourceSettingEntry;
    private static BooleanSettingEntry waterSensitiveCheckSettingEntry;

    public final SpruceOption dynamicLightsModeOption = new SpruceCyclingOption("ryoamiclights.option.mode",
            amount -> this.setDynamicLightsMode(DYNAMIC_LIGHTS_MODE.get().next()),
            option -> option.getDisplayText(DYNAMIC_LIGHTS_MODE.get().next().getTranslatedText()),
            Text.translatable("ryoamiclights.tooltip.mode.1")
                    .append(Text.literal("\n"))
                    .append(Text.translatable("ryoamiclights.tooltip.mode.2", DynamicLightsMode.FASTEST.getTranslatedText(), DynamicLightsMode.FAST.getTranslatedText()))
                    .append(Text.literal("\n"))
                    .append(Text.translatable("ryoamiclights.tooltip.mode.3", DynamicLightsMode.FANCY.getTranslatedText())));

    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.EnumValue<DynamicLightsMode> DYNAMIC_LIGHTS_MODE;

    public static final ModConfigSpec.ConfigValue<BooleanSettingEntry> ENTITIES_LIGHT_SOURCE;
    public static final ModConfigSpec.ConfigValue<BooleanSettingEntry> SELF_LIGHT_SOURCE;
    public static final ModConfigSpec.ConfigValue<BooleanSettingEntry> BLOCK_ENTITIES_LIGHT_SOURCE;
    public static final ModConfigSpec.ConfigValue<BooleanSettingEntry> WATER_SENSITIVE_CHECK;

    public static final ModConfigSpec.EnumValue<ExplosiveLightingMode> CREEPER_LIGHTING_MODE;
    public static final ModConfigSpec.EnumValue<ExplosiveLightingMode> TNT_LIGHTING_MODE;

    static {
        var builder = new ModConfigSpec.Builder();

        builder.comment("RyoamicLights configuration.");

        DYNAMIC_LIGHTS_MODE = builder.comment("The dynamic lights mode")
                .defineEnum("mode", DEFAULT_DYNAMIC_LIGHTS_MODE);

        builder.comment("Light sources settings.").push("light_sources");
        ENTITIES_LIGHT_SOURCE = builder.comment("Enable entities light source.")
                .define("entities", entitiesLightSourceSettingEntry);
        SELF_LIGHT_SOURCE = builder.comment("Enable first-person player light source.")
                .define("self", selfLightSourceSettingEntry);
        BLOCK_ENTITIES_LIGHT_SOURCE = builder.comment("Enable block entities light source.")
                .define("block_entities", blockEntitiesLightSourceSettingEntry);
        WATER_SENSITIVE_CHECK = builder.comment("Enables the water-sensitive light sources check. This means that some items will not emit light while being submerged in water.")
                .define("water_sensitive_check", waterSensitiveCheckSettingEntry);
        CREEPER_LIGHTING_MODE = builder.comment("Creeper lighting mode. May be off, simple or fancy.")
                .defineEnum("creeper", DEFAULT_CREEPER_LIGHTING_MODE);
        TNT_LIGHTING_MODE = builder.comment("TNT lighting mode. May be off, simple or fancy.")
                .defineEnum("tnt", DEFAULT_TNT_LIGHTING_MODE);

        builder.pop();
        SPEC = builder.build();
    }

    public DynamicLightsConfig(@NotNull RyoamicLights mod) {
        this.mod = mod;

        this.config = CommentedFileConfig.builder(CONFIG_FILE_PATH)
                .autosave()
                .writingMode(WritingMode.REPLACE_ATOMIC)
                .build();

        entitiesLightSourceSettingEntry = new BooleanSettingEntry("light_sources.entities", DEFAULT_ENTITIES_LIGHT_SOURCE, this.config,
                Text.translatable("ryoamiclights.tooltip.entities"))
                .withOnSet(value -> {
                    if (!value) this.mod.removeEntitiesLightSource();
                });
        selfLightSourceSettingEntry = new BooleanSettingEntry("light_sources.self", DEFAULT_SELF_LIGHT_SOURCE, this.config,
                Text.translatable("ryoamiclights.tooltip.self_light_source"))
                .withOnSet(value -> {
                    if (!value) this.mod.removeLightSources(source ->
                            source instanceof ClientPlayerEntity && source == MinecraftClient.getInstance().player
                    );
                });
        blockEntitiesLightSourceSettingEntry = new BooleanSettingEntry("light_sources.block_entities", DEFAULT_BLOCK_ENTITIES_LIGHT_SOURCE, this.config,
                Text.translatable("ryoamiclights.tooltip.block_entities"))
                .withOnSet(value -> {
                    if (!value) this.mod.removeBlockEntitiesLightSource();
                });
        waterSensitiveCheckSettingEntry = new BooleanSettingEntry("light_sources.water_sensitive_check", DEFAULT_WATER_SENSITIVE_CHECK, this.config,
                Text.translatable("ryoamiclights.tooltip.water_sensitive"));

    }

    /**
     * Loads the configuration.
     */
    public void load() {
        this.config.load();


        //SPEC.correct(this.config);

        this.mod.log("Configuration loaded.");
    }

    /**
     * Loads the setting.
     *
     * @param settingEntry the setting to load
     */
    public void load(SettingEntry<?> settingEntry) {
        settingEntry.load(this.config);
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
        this.getEntitiesLightSource().set(DEFAULT_ENTITIES_LIGHT_SOURCE);
        this.getSelfLightSource().set(DEFAULT_SELF_LIGHT_SOURCE);
        this.getBlockEntitiesLightSource().set(DEFAULT_BLOCK_ENTITIES_LIGHT_SOURCE);
        this.getWaterSensitiveCheck().set(DEFAULT_WATER_SENSITIVE_CHECK);
        this.setCreeperLightingMode(DEFAULT_CREEPER_LIGHTING_MODE);
        this.setTntLightingMode(DEFAULT_TNT_LIGHTING_MODE);
    }

    /**
     * Returns the dynamic lights mode.
     *
     * @return the dynamic lights mode
     */
    public DynamicLightsMode getDynamicLightsMode() {
        return DYNAMIC_LIGHTS_MODE.get();
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

        DYNAMIC_LIGHTS_MODE.set(mode);
    }

    /**
     * {@return the entities as light source setting holder}
     */
    public BooleanSettingEntry getEntitiesLightSource() {
        return ENTITIES_LIGHT_SOURCE.get();
    }

    /**
     * {@return the first-person player as light source setting holder}
     */
    public BooleanSettingEntry getSelfLightSource() {
        return SELF_LIGHT_SOURCE.get();
    }

    /**
     * {@return the block entities as light source setting holder}
     */
    public BooleanSettingEntry getBlockEntitiesLightSource() {
        return BLOCK_ENTITIES_LIGHT_SOURCE.get();
    }

    /**
     * {@return the water sensitive check setting holder}
     */
    public BooleanSettingEntry getWaterSensitiveCheck() {
        return WATER_SENSITIVE_CHECK.get();
    }

    /**
     * Returns the Creeper dynamic lighting mode.
     *
     * @return the Creeper dynamic lighting mode
     */
    public ExplosiveLightingMode getCreeperLightingMode() {
        return CREEPER_LIGHTING_MODE.get();
    }

    /**
     * Sets the Creeper dynamic lighting mode.
     *
     * @param lightingMode the Creeper dynamic lighting mode
     */
    public void setCreeperLightingMode(@NotNull ExplosiveLightingMode lightingMode) {
        if (!lightingMode.isEnabled())
            this.mod.removeCreeperLightSources();

        CREEPER_LIGHTING_MODE.set(lightingMode);
    }

    /**
     * Returns the TNT dynamic lighting mode.
     *
     * @return the TNT dynamic lighting mode
     */
    public ExplosiveLightingMode getTntLightingMode() {
        return TNT_LIGHTING_MODE.get();
    }

    /**
     * Sets the TNT dynamic lighting mode.
     *
     * @param lightingMode the TNT dynamic lighting mode
     */
    public void setTntLightingMode(@NotNull ExplosiveLightingMode lightingMode) {
        if (!lightingMode.isEnabled())
            this.mod.removeTntLightSources();

        TNT_LIGHTING_MODE.set(lightingMode);
    }
}
