package me.Halflove.DailyRewards.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MessageUtils {

    /**
     * Sends message translated using alternative codes to given command sender.
     *
     * @param commandSender message receiver
     * @param message message to be sent
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
     * Sends broadcast message translated using alternative codes.
     *
     * @param message message to be sent
     */
    public static void sendBroadcastMessage(String message) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
