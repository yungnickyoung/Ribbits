package com.yungnickyoung.minecraft.ribbits.client.supporters;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.network.ClientNetworkHandler;
import com.yungnickyoung.minecraft.ribbits.platform.PlatformHelper;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the JSON options stored in config/ribbits-options.json.
 */
public class RibbitOptionsJSON {
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Ribbits Options I/O Thread");
        t.setDaemon(true);
        return t;
    });

    private static volatile RibbitOptionsJSON instance;

    public static RibbitOptionsJSON get() {
        if (instance == null) {
            synchronized (RibbitOptionsJSON.class) {
                if (instance == null) {
                    instance = new RibbitOptionsJSON(false);
                }
            }
        }
        return instance;
    }

    private boolean enableSupporterHat;

    private RibbitOptionsJSON(boolean enableSupporterHat) {
        this.enableSupporterHat = enableSupporterHat;
    }

    public boolean isSupporterHatEnabled() {
        return enableSupporterHat;
    }

    public void setSupporterHatEnabled(boolean enabled) {
        this.enableSupporterHat = enabled;
        ClientNetworkHandler.notifyServerOfSupporterHatState(enabled);
        saveToFileAsync();
    }

    private void saveToFileAsync() {
        IO_EXECUTOR.execute(() -> {
            try (Writer writer = new FileWriter(getOptionsFilePath().toFile())) {
                JSON.gson.toJson(instance, writer);
            } catch (IOException e) {
                RibbitsCommon.LOGGER.error("Error saving ribbits-options.json file: {}", e.toString());
            }
        });
    }

    public static void loadFromFile() {
        Path path = getOptionsFilePath();
        File file = path.toFile();

        if (!file.exists()) {
            try {
                JSON.createJsonFileFromObject(path, RibbitOptionsJSON.get());
            } catch (IOException e) {
                RibbitsCommon.LOGGER.error("Unable to create ribbits-options.json file: {}", e.toString());
            }
            return;
        }

        if (!file.canRead()) {
            RibbitsCommon.LOGGER.error("ribbits-options.json file not readable! Using default configuration...");
            return;
        }

        try {
            instance = JSON.loadObjectFromJsonFile(path, RibbitOptionsJSON.class);
        } catch (IOException e) {
            RibbitsCommon.LOGGER.error("Error loading ribbits-options.json file: {}", e.toString());
            RibbitsCommon.LOGGER.error("Using default configuration...");
        }
    }

    private static Path getOptionsFilePath() {
        return PlatformHelper.getConfigFolder().resolve("ribbits-options.json");
    }
}
