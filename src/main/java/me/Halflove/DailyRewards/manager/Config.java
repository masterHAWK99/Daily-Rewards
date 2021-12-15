package me.Halflove.DailyRewards.manager;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config extends YamlConfiguration {

    private final File file;

    public Config(File directory, String path) {
        this.file = new File(directory, path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().severe(String.format(ChatColor.RED + "Could not create %s!", path));
            }
        }

        reload();
    }

    public void reload() {
        try {
            this.load(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().severe(String.format(ChatColor.RED + "Could not save %s!", file.getName()));
        }
    }
}
