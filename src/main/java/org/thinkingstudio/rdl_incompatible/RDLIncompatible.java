package org.thinkingstudio.rdl_incompatible;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.lambdaurora.lambdynlights.LambDynLightsCompat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class RDLIncompatible {
    public static void onInitializeClient() {
        ClientLifecycleEvent.CLIENT_STARTED.register(client -> {
            if((HAS_AN && CONFIG.showArsNouveauScreen)) {
                client.setScreen(new IncompatibleWarnScreen());
            }
        });
    }

    private static Config createConfig() {
        Config finalConfig;
        LOGGER.info("Trying to read config file...");
        try {
            if(CONFIG_FILE.createNewFile()) {
                LOGGER.info("No config file found, creating a new one...");
                writeConfig(GSON.toJson(JsonParser.parseString(GSON.toJson(new Config()))));
                finalConfig = new Config();
                LOGGER.info("Successfully created default config file.");
            } else {
                LOGGER.info("A config file was found, loading it..");
                finalConfig = GSON.fromJson(new String(Files.readAllBytes(CONFIG_FILE.toPath())), Config.class);
                if(finalConfig == null) {
                    throw new NullPointerException("The config file was empty.");
                } else {
                    LOGGER.info("Successfully loaded config file.");
                }
            }
        } catch(Exception e) {
            LOGGER.error("There was an error creating/loading the config file!", e);
            finalConfig = new Config();
            LOGGER.warn("Defaulting to original config.");
        }
        return finalConfig;
    }

    public static void saveConfig(Config modConfig) {
        try {
            writeConfig(GSON.toJson(JsonParser.parseString(GSON.toJson(modConfig))));
            LOGGER.info("Saved new config file.");
        } catch(Exception e) {
            LOGGER.error("There was an error saving the config file!", e);
        }
    }

    private static void writeConfig(String json) {
        try(PrintWriter printWriter = new PrintWriter(CONFIG_FILE)) {
            printWriter.write(json);
            printWriter.flush();
        } catch(IOException e) {
            LOGGER.error("Failed to write config file", e);
        }
    }

    public static final boolean HAS_AN;

    public static final Logger LOGGER;

    private static final Gson GSON;
    private static final File CONFIG_FILE;
    public static final Config CONFIG;


    static {
        HAS_AN = LambDynLightsCompat.isArsNouveauInstalled();

        LOGGER = LoggerFactory.getLogger("RDLIncompatibleCheck");;

        GSON = new GsonBuilder().setPrettyPrinting().create();
        CONFIG_FILE = new File(String.format("%s%sRDLIncompatibleCheck.json", Platform.getConfigFolder(), File.separator));
        CONFIG = createConfig();
    }
}
