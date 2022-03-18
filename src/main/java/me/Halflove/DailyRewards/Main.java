package me.Halflove.DailyRewards;

import me.Halflove.DailyRewards.command.AdminCommands;
import me.Halflove.DailyRewards.command.RewardCommands;
import me.Halflove.DailyRewards.data.Data;
import me.Halflove.DailyRewards.listener.PlayerJoinListener;
import me.Halflove.DailyRewards.manager.PAPIExtensions;
import me.Halflove.DailyRewards.manager.SettingsManager;
import me.Halflove.DailyRewards.manager.UpdateChecker;
import me.Halflove.DailyRewards.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static boolean papi;

    private SettingsManager settings;

    private Data data;

    private UpdateChecker updateChecker;

    public void onEnable() {
        settings = new SettingsManager(this);

        data = Data.getDataProvider(this);

        getCommand("dailyrewards").setExecutor(new AdminCommands(this));
        getCommand("reward").setExecutor(new RewardCommands(this));

        registerEvents();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papi = true;
            new PAPIExtensions(this).register();
        } else {
            papi = false;
        }

        updateChecker = new UpdateChecker(this);
        updateChecker.runCheckVersionTask();

        MessageUtils.setConfig(settings);
    }

    public SettingsManager getSettings() {
        return settings;
    }

    public Data getData() {
        return data;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }
}

