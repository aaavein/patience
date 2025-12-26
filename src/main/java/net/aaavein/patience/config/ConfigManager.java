package net.aaavein.patience.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.neoforged.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.aaavein.patience.registry.DefaultContainers;
import net.aaavein.patience.util.SlotRange;
import net.aaavein.patience.util.SlotRangeSerializer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ConfigManager {
    private static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE = "patience.json";
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(SlotRange.class, new SlotRangeSerializer())
            .setPrettyPrinting()
            .create();

    private ConfigManager() {}

    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE);
    }

    public static PatienceConfig load() {
        Path configPath = getConfigPath();
        File configFile = configPath.toFile();

        if (!configFile.exists()) {
            createDefault();
        }

        try {
            byte[] bytes = Files.readAllBytes(configPath);
            PatienceConfig config = GSON.fromJson(new String(bytes), PatienceConfig.class);
            updateWithNewContainers(config);
            return config;
        } catch (Exception e) {
            LOGGER.error("Failed to load config, using defaults", e);
            return null;
        }
    }

    public static void save(PatienceConfig config) {
        try {
            Files.write(getConfigPath(), GSON.toJson(config).getBytes());
        } catch (Exception e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    private static void createDefault() {
        Path configPath = getConfigPath();
        File configDir = configPath.getParent().toFile();

        if (!configDir.exists() && !configDir.mkdirs()) {
            LOGGER.error("Failed to create config directory");
            return;
        }

        List<ContainerSettings> defaultContainers = DefaultContainers.getAll();

        PatienceConfig config = new PatienceConfig();
        config.setContainers(defaultContainers);

        config.setIngredientMultipliers(ItemSettings.builder()
                .byMod(new HashMap<>())
                .byItem(new HashMap<>())
                .byTag(new HashMap<>())
                .build());
        config.setOutputMultipliers(ItemSettings.builder()
                .byMod(new HashMap<>())
                .byItem(new HashMap<>())
                .byTag(new HashMap<>())
                .build());

        config.getIngredientMultipliers().getByMod().put("minecraft", 1.0F);
        config.getIngredientMultipliers().getByItem().put("minecraft:stick", 1.0F);

        config.getOutputMultipliers().getByMod().put("minecraft", 1.0F);
        config.getOutputMultipliers().getByItem().put("minecraft:stick", 1.0F);

        save(config);
    }

    private static void updateWithNewContainers(PatienceConfig config) {
        Set<String> existingScreens = config.getContainers().stream()
                .map(ContainerSettings::getScreenClass)
                .collect(Collectors.toSet());

        List<ContainerSettings> defaults = DefaultContainers.getAll();
        boolean updated = false;

        for (ContainerSettings defaultContainer : defaults) {
            if (!existingScreens.contains(defaultContainer.getScreenClass())) {
                LOGGER.info("Adding new container: {}", defaultContainer.getScreenClass());
                config.getContainers().add(defaultContainer);
                updated = true;
            }
        }

        if (updated) {
            save(config);
        }
    }
}