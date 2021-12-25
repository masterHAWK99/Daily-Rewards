package me.Halflove.DailyRewards.listener;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.manager.RewardManager;
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
        plugin.getData().createUser(player);
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
        if (plugin.getSettings().getConfiguration().claimOnLogin.enabled && player.hasPermission("dr.claim")) {
            (new BukkitRunnable() {
                public void run() {
                    if (player.hasPermission("dr.claim")) {
                        if (System.currentTimeMillis() > plugin.getData().getTime(player)) {
                            RewardManager.setReward(player);
                            return;
                        }
                        String norewards = plugin.getSettings().getMessagesConfig().noRewards;
                        if (Main.papi) {
                            norewards = PlaceholderAPI.setPlaceholders(player, norewards);
                        }
                        MessageUtils.sendMessage(player, norewards);

                        long millis = plugin.getData().getTime(player) - System.currentTimeMillis();
                        String cdmsg = plugin.getSettings().getMessagesConfig().cooldown;
                        cdmsg = cdmsg.replace("%time%", DateUtils.getRemainingTime(millis));
                        cdmsg = cdmsg.replace("%s%", DateUtils.getRemainingSec(millis));
                        cdmsg = cdmsg.replace("%m%", DateUtils.getRemainingMin(millis));
                        cdmsg = cdmsg.replace("%h%", DateUtils.getRemainingHour(millis));

                        if (Main.papi) {
                            cdmsg = PlaceholderAPI.setPlaceholders(player, cdmsg);
                        }
                        MessageUtils.sendMessage(player, cdmsg);
                        RewardManager.noReward(player);
                    } else {
                        String msg = plugin.getSettings().getMessagesConfig().noPermission;
                        msg = msg.replace("%player", player.getName());
                        MessageUtils.sendMessage(player, msg);
                    }
                }
            }).runTaskLater(plugin, plugin.getSettings().getConfiguration().claimOnLogin.delay);
        } else if (player.hasPermission("dr.claim")
            && System.currentTimeMillis() > plugin.getData().getTime(player)) {
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
