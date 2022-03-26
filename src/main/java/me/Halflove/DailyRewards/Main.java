package me.Halflove.DailyRewards;

import me.Halflove.DailyRewards.command.AdminCommands;
import me.Halflove.DailyRewards.command.RewardCommands;
import me.Halflove.DailyRewards.data.Data;
import me.Halflove.DailyRewards.hook.PlaceholderApiHook;
import me.Halflove.DailyRewards.listener.PlayerJoinListener;
import me.Halflove.DailyRewards.manager.SettingsManager;
import me.Halflove.DailyRewards.manager.UpdateChecker;
import me.Halflove.DailyRewards.util.MessageUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private SettingsManager settings;

    private Data data;

    private UpdateChecker updateChecker;

    public void onEnable() {
        settings = new SettingsManager(this);

        data = Data.getDataProvider(this);

        getCommand("dailyrewards").setExecutor(new AdminCommands(this));
        getCommand("reward").setExecutor(new RewardCommands(this));

        registerEvents();

        PlaceholderApiHook.initialize(this);

        updateChecker = new UpdateChecker(this);
        updateChecker.runCheckVersionTask();

        MessageUtils.setConfig(settings);

        new Metrics(this, 14675);
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

