package me.Halflove.DailyRewards.hook;

import me.Halflove.DailyRewards.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlaceholderApiHook {

    public static void initialize(Main plugin) {
        if (isEnabled()) {
            new PlaceholderApiExpansion(plugin).register();
        }
    }

    public static boolean isEnabled() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static String replacePlaceholders(Player player, String text) {
        return isEnabled() ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }
}
