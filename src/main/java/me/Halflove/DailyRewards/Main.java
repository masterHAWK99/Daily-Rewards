package me.Halflove.DailyRewards;

import me.Halflove.DailyRewards.command.AdminCommands;
import me.Halflove.DailyRewards.command.RewardCommands;
import me.Halflove.DailyRewards.listener.PlayerJoinListener;
import me.Halflove.DailyRewards.manager.MySQLManager;
import me.Halflove.DailyRewards.manager.PAPIExtensions;
import me.Halflove.DailyRewards.manager.SettingsManager;
import me.Halflove.DailyRewards.manager.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static boolean papi;

    private SettingsManager settings;

    private UpdateChecker updateChecker;

    public void onEnable() {
        getCommand("dailyrewards").setExecutor(new AdminCommands(this));
        getCommand("reward").setExecutor(new RewardCommands(this));

        settings = new SettingsManager(this);

        registerEvents();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            MySQLManager.mysqlSetup();
            MySQLManager.createTable();
            for (Player player : Bukkit.getOnlinePlayers()) {
                MySQLManager.createPlayer(player);
            }
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papi = true;
            new PAPIExtensions().register();
        } else {
            papi = false;
        }

        updateChecker = new UpdateChecker(this, 16708);
        updateChecker.checkVersion((version, pluginVersion) -> {
            if (pluginVersion.equalsIgnoreCase(version)) {
                getLogger().info("Plugin is up to date.");
            } else {
                getLogger().severe("*** Daily Rewards is Outdated! ***");
                getLogger().severe("*** You're on " + pluginVersion + " while " + version + " is available! ***");
                getLogger().severe("*** Update Here: https://www.spigotmc.org/resources/daily-rewards.16708/ ***");
            }
        });
    }

    public SettingsManager getSettings() {
        return settings;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }
}

