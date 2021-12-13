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

    public SettingsManager settings = SettingsManager.getInstance();

    public void onEnable() {
        getCommand("dailyrewards").setExecutor(new AdminCommands(this));
        getCommand("reward").setExecutor(new RewardCommands());
        this.settings.setup(this);
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

        new UpdateChecker(this, 16708).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("Plugin is up to date.");
            } else {
                getLogger().severe("*** Daily Rewards is Outdated! ***");
                getLogger().severe("*** You're on " + this.getDescription().getVersion() + " while " + version + " is available! ***");
                getLogger().severe("*** Update Here: https://www.spigotmc.org/resources/daily-rewards.16708/ ***");
            }
        });

    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }
}

