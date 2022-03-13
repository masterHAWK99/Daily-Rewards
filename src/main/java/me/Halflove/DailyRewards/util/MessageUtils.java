package me.Halflove.DailyRewards.util;

import me.Halflove.DailyRewards.config.DefaultConfig;
import me.Halflove.DailyRewards.manager.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MessageUtils {

    private static DefaultConfig defaultConfig;

    public static void setConfig(SettingsManager settingsManager) {
        defaultConfig = settingsManager.getConfiguration();
    }

    /**
     * Sends message translated using alternative codes to given command sender.
     *
     * @param commandSender message receiver
     * @param message       message to be sent
     */
    public static void sendMessage(CommandSender commandSender, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.stripColor(message));
            return;
        }

        commandSender.sendMessage(message);
    }

    /**
     * Sends message translated using alternative codes to given command sender.
     *
     * @param commandSender message receiver
     * @param message       message to be sent
     */
    public static void sendMessageWithPrefix(CommandSender commandSender, String message) {
        sendMessage(commandSender, defaultConfig.prefix + message);
    }

    /**
     * Sends broadcast message translated using alternative codes.
     *
     * @param message message to be sent
     */
    public static void sendBroadcastMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
