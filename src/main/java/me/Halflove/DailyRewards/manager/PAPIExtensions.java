package me.Halflove.DailyRewards.manager;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.util.DateUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PAPIExtensions extends PlaceholderExpansion {
    private Main plugin;

    public PAPIExtensions(Main plugin) {
        this.plugin = plugin;
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "dailyrewards";
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        long millis = plugin.getData().getTime(player) - System.currentTimeMillis();
        if (identifier.equals("remaining_time")) {
            return DateUtils.getRemainingTime(millis);
        }
        if (identifier.equals("remaining_hours")) {
            return DateUtils.getRemainingHour(millis);
        }
        if (identifier.equals("remaining_minutes")) {
            return DateUtils.getRemainingMin(millis);
        }
        if (identifier.equals("remaining_seconds")) {
            return DateUtils.getRemainingSec(millis);
        }
        if (identifier.equals("player_test_qualification")) {
            if (System.currentTimeMillis() > plugin.getData().getTime(player)) {
                return plugin.getSettings().getMessagesConfig().papiPlaceholders.rewardAvailable;
            }
            return plugin.getSettings().getMessagesConfig().papiPlaceholders.noRewards;
        }
        if (identifier.equals("player_reward_available")) {
            return plugin.getSettings().getMessagesConfig().papiPlaceholders.rewardAvailable;
        }
        if (identifier.equals("player_no_rewards")) {
            return plugin.getSettings().getMessagesConfig().papiPlaceholders.noRewards;
        }
        return null;
    }
}

