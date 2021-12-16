package me.Halflove.DailyRewards.manager;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import me.Halflove.DailyRewards.Main;
import org.bukkit.plugin.Plugin;

public class SettingsManager {

    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory()).configure(JsonParser.Feature.IGNORE_UNDEFINED, true);

    private static Plugin plugin;

    private static Config config;

    private static Config data;

    private static Config msg;

    public SettingsManager(Main plugin) {
        this.plugin = plugin;

        setup();
    }

    public static Config getMsg() {
        return msg;
    }

    public static Config getData() {
        return data;
    }

    public static Config getConfig() {
        return config;
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

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        config = new Config(plugin.getDataFolder(), "config.yml");
        config.options().copyDefaults(true);
        config.addDefault("cooldown", 86400000);
        config.addDefault("savetoip", Boolean.FALSE);
        config.addDefault("regenerate-default-rewards", true);
        config.addDefault("mysql.enabled", false);
        config.addDefault("mysql.host-name", "localhost");
        config.addDefault("mysql.port", 3306);
        config.addDefault("mysql.database", "example");
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "password");
        config.addDefault("loginclaim.enabled", false);
        config.addDefault("loginclaim.delay", 3);
        config.addDefault("claim.sound", "");
        config.addDefault("claim.sound.enabled", true);
        config.addDefault("claim.sound.type", "ENTITY_PLAYER_LEVELUP");
        config.addDefault("claim.sound.volume", 1);
        config.addDefault("claim.sound.pitch", 1);
        config.addDefault("noreward.sound", "");
        config.addDefault("noreward.sound.enabled", true);
        config.addDefault("noreward.sound.type", "BLOCK_ANVIL_LAND");
        config.addDefault("noreward.sound.volume", 1);
        config.addDefault("noreward.sound.pitch", 1);
        List<String> command = new ArrayList<>();
        command.add("give %player minecraft:diamond 1");
        List<String> bworld = new ArrayList<>();
        bworld.add("example_world");
        bworld.add("example_world2");
        config.addDefault("rewards.basic.name", "Basic");
        config.addDefault("rewards.basic.permission", false);
        config.addDefault("rewards.basic.random", false);
        config.addDefault("rewards.basic.claim-message", "&aRewards&f: You claimed the &7Basic&f Daily Reward!");
        config.addDefault("rewards.basic.broadcast", "");
        config.addDefault("rewards.basic.commands", command);
        List<String> command2 = new ArrayList<>();
        command2.add("give %player minecraft:diamond 1;say %player earned a common diamond");
        command2.add("give %player minecraft:diamond 1;say %player earned a common diamond");
        command2.add("give %player minecraft:emerald 1;say %player earned a rare emerald");
        List<String> bworld2 = new ArrayList<>();
        bworld2.add("example_world3");
        bworld2.add("example_world4");
        config.addDefault("rewards.advanced.name", "Advanced");
        config.addDefault("rewards.advanced.permission", true);
        config.addDefault("rewards.advanced.random", true);
        config.addDefault("rewards.advanced.claim-message", "");
        config.addDefault("rewards.advanced.broadcast", "&aRewards&f: %player claimed the &eAdvanced&f Daily Reward!");
        config.addDefault("rewards.advanced.commands", command2);
        config.save();

        data = new Config(plugin.getDataFolder(), "data.yml");

        msg = new Config(plugin.getDataFolder(), "messages.yml");
        msg.options().copyDefaults(true);
        msg.addDefault("no-rewards", "&aRewards&f: &fYou do not have any available rewards at the moment.");
        msg.addDefault("cooldown-msg", "&aRewards&f: &fTime until next reward: %time%");
        msg.addDefault("no-permission", "&aRewards&f: &fYou do not have permission to do ");
        msg.addDefault("reward-available", "&aRewards&f: &fYou have unclaimed daily rewards, do &e/reward &fto claim!");
        msg.addDefault("PlaceholderAPI.reward-available", "Unclaimed Rewards Available!");
        msg.addDefault("PlaceholderAPI.no-rewards", "No Rewards Available");
        msg.save();
    }
}
