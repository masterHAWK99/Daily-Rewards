package me.Halflove.DailyRewards.manager;

import org.bukkit.entity.Player;

public class CooldownManager {

    public static long getTimeIp(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");

        long releaseip;
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            releaseip = MySQLManager.getCooldownIP(ip);
        } else {
            releaseip = SettingsManager.getData().getLong(ip + ".millis");
        }

        return releaseip;
    }

    public static long getTimeUuid(Player player) {
        long releaseip;
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            releaseip = MySQLManager.getCooldownUUID(player.getUniqueId());
        } else {
            releaseip = SettingsManager.getData().getLong(player.getUniqueId() + ".millis");
        }

        return releaseip;
    }

    public static boolean getAllowRewardip(Player p) {
        return System.currentTimeMillis() > getTimeIp(p);
    }

    public static boolean getAllowRewardUUID(Player p) {
        return System.currentTimeMillis() > getTimeUuid(p);
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

    public static void updateTime(Player player, long millis) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        SettingsManager.getData().set(ip + ".millis", millis);
        SettingsManager.getData().set(player.getUniqueId() + ".millis", millis);

        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            MySQLManager.updateCooldownIP(ip, millis);
            MySQLManager.updateCooldownUUID(player.getUniqueId(), millis);
        }
    }
}

