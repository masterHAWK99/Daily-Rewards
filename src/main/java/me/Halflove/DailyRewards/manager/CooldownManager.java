package me.Halflove.DailyRewards.manager;

import org.bukkit.entity.Player;

public class CooldownManager {
    public static boolean getAllowRewardip(Player p) {
        long millis;
        String ip = p.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        long current = System.currentTimeMillis();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            millis = MySQLManager.getCooldownIP(ip);
        } else {
            millis = SettingsManager.getData().getLong(ip + ".millis");
        }
        return (current > millis);
    }

    public static boolean getAllowRewardUUID(Player p) {
        long millis, current = System.currentTimeMillis();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            millis = MySQLManager.getCooldownUUID(p.getUniqueId());
        } else {
            millis = SettingsManager.getData().getLong(p.getUniqueId() + ".millis");
        }
        return (current > millis);
    }

    public static boolean getAllowStreakUUID(Player p) {
        long current = System.currentTimeMillis();
        long millis = getStreakUUID(p);
        return (current > millis);
    }

    public static boolean getAllowStreakIP(Player p) {
        long current = System.currentTimeMillis();
        long millis = getStreakIP(p);
        return (current > millis);
    }

    public static long getStreakUUID(Player player) {
        return SettingsManager.getData().getLong(player.getUniqueId() + ".reset");
    }

    public static long getStreakIP(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        return SettingsManager.getData().getLong(ip + ".reset");
    }

    public static long getTime(Player player) {
        return SettingsManager.getData().getLong(player.getUniqueId() + ".millis");
    }

    public static long getTimeip(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        return SettingsManager.getData().getLong(ip + ".millis");
    }
}

