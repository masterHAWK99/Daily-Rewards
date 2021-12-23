package me.Halflove.DailyRewards.manager;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.config.DefaultConfig;
import me.Halflove.DailyRewards.config.MessagesConfig;
import org.bukkit.plugin.Plugin;

public class SettingsManager {

    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
        .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
        .setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
    private final Plugin plugin;
    private MessagesConfig messagesConfig;
    private DefaultConfig defaultConfig;

    public SettingsManager(Main plugin) {
        this.plugin = plugin;

        setup();
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public DefaultConfig getConfiguration() {
        return defaultConfig;
    }

    /**
     * Maps given yml file content into given Java type.
     *
     * @return object with mapped fields or new object with default values if file doesn't exist
     */
    private <T> T loadConfig(File file, Class<T> clazz) {
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, clazz);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Serializes given Java type into yml file.
     */
    private void saveConfig(File file, Object object) {
        try {
            objectMapper.writeValue(file, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfigs() {
        messagesConfig = loadConfig(new File(plugin.getDataFolder(), "messages.yml"), MessagesConfig.class);
        defaultConfig = loadConfig(new File(plugin.getDataFolder(), "config.yml"), DefaultConfig.class);
    }

    public void saveConfigs() {
        saveConfig(new File(plugin.getDataFolder(), "messages.yml"), messagesConfig);
        saveConfig(new File(plugin.getDataFolder(), "config.yml"), defaultConfig);
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        loadConfigs();
        saveConfigs();
    }
}
