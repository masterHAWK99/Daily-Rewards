package me.Halflove.DailyRewards.listener;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.manager.CooldownManager;
import me.Halflove.DailyRewards.manager.MySQLManager;
import me.Halflove.DailyRewards.manager.RewardManager;
import me.Halflove.DailyRewards.manager.SettingsManager;
import me.Halflove.DailyRewards.util.DateUtils;
import me.Halflove.DailyRewards.util.MessageUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            MySQLManager.createPlayer(player);
        }
        (new BukkitRunnable() {
            public void run() {
                if (player.getName().equalsIgnoreCase("halflove")) {
                    player.sendMessage(ChatColor.GREEN + "Hey that's cool, they use DailyRewards! :) v"
                        + plugin.getDescription().getVersion());
                }
                if (player.isOp()) {
                    plugin.getUpdateChecker().checkVersion((version, pluginVersion) -> {
                        if (!pluginVersion.equalsIgnoreCase(version)) {
                            player.sendMessage(ChatColor.GOLD + "*** Daily Rewards is Outdated! ***");
                            player.sendMessage(
                                ChatColor.YELLOW + "You're on " + ChatColor.WHITE + pluginVersion + ChatColor.YELLOW + " while " + ChatColor.WHITE + version + ChatColor.YELLOW
                                    + " is available!");
                            player.sendMessage(ChatColor.YELLOW + "Update Here: " + ChatColor.WHITE + "https://bit.ly/3x2Ma4S");
                        }
                    });
                }
            }
        }).runTaskLater(plugin, 50L);
        if (SettingsManager.getConfig().getBoolean("loginclaim.enabled") && player.hasPermission("dr.claim")) {
            (new BukkitRunnable() {
                public void run() {
                    if (player.hasPermission("dr.claim")) {
                        String ip = player.getAddress().getAddress().getHostAddress();
                        ip = ip.replace(".", "-");
                        if (SettingsManager.getConfig().getBoolean("savetoip")) {
                            if (!CooldownManager.getAllowRewardip(player)) {
                                long releaseip;
                                String noreward = plugin.getSettings().getMessagesConfig().noRewards;
                                if (Main.papi) {
                                    noreward = PlaceholderAPI.setPlaceholders(player, noreward);
                                }
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noreward));
                                long current = System.currentTimeMillis();
                                if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                                    releaseip = MySQLManager.getCooldownIP(ip);
                                } else {
                                    releaseip = SettingsManager.getData().getLong(ip + ".millis");
                                }
                                long millis = releaseip - current;
                                String cdmsg = plugin.getSettings().getMessagesConfig().cooldown;
                                cdmsg = cdmsg.replace("%time%", DateUtils.getRemainingTime(millis));
                                cdmsg = cdmsg.replace("%s%", DateUtils.getRemainingSec(millis));
                                cdmsg = cdmsg.replace("%m%", DateUtils.getRemainingMin(millis));
                                cdmsg = cdmsg.replace("%h%", DateUtils.getRemainingHour(millis));
                                cdmsg = cdmsg.replace("%time", DateUtils.getRemainingTime(millis));
                                cdmsg = cdmsg.replace("%s", DateUtils.getRemainingSec(millis));
                                cdmsg = cdmsg.replace("%m", DateUtils.getRemainingMin(millis));
                                cdmsg = cdmsg.replace("%h", DateUtils.getRemainingHour(millis));
                                if (!cdmsg.equalsIgnoreCase("")) {
                                    if (Main.papi) {
                                        cdmsg = PlaceholderAPI.setPlaceholders(player, cdmsg);
                                    }
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', cdmsg));
                                }
                                RewardManager.noReward(player);
                            } else {
                                RewardManager.setReward(player);
                            }
                        } else if (!CooldownManager.getAllowRewardUUID(player)) {
                            long releaseip;
                            String noreward = plugin.getSettings().getMessagesConfig().noRewards;
                            if (Main.papi) {
                                noreward = PlaceholderAPI.setPlaceholders(player, noreward);
                            }
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noreward));
                            long current = System.currentTimeMillis();
                            if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                                releaseip = MySQLManager.getCooldownUUID(player.getUniqueId());
                            } else {
                                releaseip = SettingsManager.getData().getLong(player.getUniqueId() + ".millis");
                            }
                            long millis = releaseip - current;
                            String cdmsg = plugin.getSettings().getMessagesConfig().cooldown;
                            cdmsg = cdmsg.replace("%time%", DateUtils.getRemainingTime(millis));
                            cdmsg = cdmsg.replace("%s%", DateUtils.getRemainingSec(millis));
                            cdmsg = cdmsg.replace("%m%", DateUtils.getRemainingMin(millis));
                            cdmsg = cdmsg.replace("%h%", DateUtils.getRemainingHour(millis));
                            cdmsg = cdmsg.replace("%time", DateUtils.getRemainingTime(millis));
                            cdmsg = cdmsg.replace("%s", DateUtils.getRemainingSec(millis));
                            cdmsg = cdmsg.replace("%m", DateUtils.getRemainingMin(millis));
                            cdmsg = cdmsg.replace("%h", DateUtils.getRemainingHour(millis));
                            if (Main.papi) {
                                cdmsg = PlaceholderAPI.setPlaceholders(player, cdmsg);
                            }
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', cdmsg));
                            RewardManager.noReward(player);
                        } else {
                            RewardManager.setReward(player);
                        }
                    } else {
                        String msg = plugin.getSettings().getMessagesConfig().noPermission;
                        msg = msg.replace("%player", player.getName());
                        MessageUtils.sendMessage(player, msg);
                    }
                }
            }).runTaskLater(plugin, SettingsManager.getConfig().getInt("loginclaim.delay"));
        } else if (player.hasPermission("dr.claim")
            && (CooldownManager.getAllowRewardip(player) || CooldownManager.getAllowRewardUUID(player))) {
            (new BukkitRunnable() {
                public void run() {
                    String available = plugin.getSettings().getMessagesConfig().rewardAvailable;
                    if (Main.papi) {
                        available = PlaceholderAPI.setPlaceholders(player, available);
                    }
                    MessageUtils.sendMessage(player, available);
                }
            }).runTaskLater(plugin, 50L);
        }
    }
}
