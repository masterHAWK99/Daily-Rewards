package me.Halflove.DailyRewards.listener;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.hook.PlaceholderApiHook;
import me.Halflove.DailyRewards.manager.RewardManager;
import me.Halflove.DailyRewards.util.DateUtils;
import me.Halflove.DailyRewards.util.MessageUtils;
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
                    plugin.getUpdateChecker().checkVersion(player);
                }
            }
        }).runTaskLater(plugin, 50L);
        if (plugin.getSettings().getConfiguration().claimOnLogin.enabled && player.hasPermission("dr.claim")) {
            (new BukkitRunnable() {
                public void run() {
                    if (player.hasPermission("dr.claim")) {
                        RewardManager rewardManager = plugin.getRewardManager();
                        if (System.currentTimeMillis() > plugin.getData().getTime(player)) {
                            rewardManager.setReward(player);
                            return;
                        }
                        String norewards = plugin.getSettings().getMessagesConfig().noRewards;
                        norewards = PlaceholderApiHook.replacePlaceholders(player, norewards);
                        MessageUtils.sendMessageWithPrefix(player, norewards);

                        long millis = plugin.getData().getTime(player) - System.currentTimeMillis();
                        String cdmsg = plugin.getSettings().getMessagesConfig().cooldown;
                        cdmsg = cdmsg.replace("%time%", DateUtils.getRemainingTime(millis));
                        cdmsg = cdmsg.replace("%s%", DateUtils.getRemainingSec(millis));
                        cdmsg = cdmsg.replace("%m%", DateUtils.getRemainingMin(millis));
                        cdmsg = cdmsg.replace("%h%", DateUtils.getRemainingHour(millis));
                        cdmsg = PlaceholderApiHook.replacePlaceholders(player, cdmsg);
                        MessageUtils.sendMessageWithPrefix(player, cdmsg);
                        rewardManager.noReward(player);
                    } else {
                        String msg = plugin.getSettings().getMessagesConfig().noPermission;
                        msg = msg.replace("%player", player.getName());
                        MessageUtils.sendMessageWithPrefix(player, msg);
                    }
                }
            }).runTaskLater(plugin, plugin.getSettings().getConfiguration().claimOnLogin.delay);
        } else if (player.hasPermission("dr.claim")
            && System.currentTimeMillis() > plugin.getData().getTime(player)) {
            (new BukkitRunnable() {
                public void run() {
                    String available = plugin.getSettings().getMessagesConfig().rewardAvailable;
                    available = PlaceholderApiHook.replacePlaceholders(player, available);
                    MessageUtils.sendMessageWithPrefix(player, available);
                }
            }).runTaskLater(plugin, 50L);
        }
    }
}
