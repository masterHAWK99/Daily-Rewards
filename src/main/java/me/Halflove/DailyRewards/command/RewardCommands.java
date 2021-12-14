package me.Halflove.DailyRewards.command;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.manager.CooldownManager;
import me.Halflove.DailyRewards.manager.RewardManager;
import me.Halflove.DailyRewards.manager.SettingsManager;
import me.Halflove.DailyRewards.util.DateUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardCommands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if (player.hasPermission("dr.claim")) {
            if (SettingsManager.getConfig().getBoolean("savetoip")) {
                if (!CooldownManager.getAllowRewardip(player)) {
                    long millis = CooldownManager.getTimeIp(player) - System.currentTimeMillis();
                    noRewardsMessage(player, millis);
                    RewardManager.noReward(player);
                } else {
                    RewardManager.setReward(player);
                }
            } else if (!CooldownManager.getAllowRewardUUID(player)) {
                long millis = CooldownManager.getTimeUuid(player) - System.currentTimeMillis();
                noRewardsMessage(player, millis);
                RewardManager.noReward(player);
            } else {
                RewardManager.setReward(player);
            }
        } else {
            String msg = SettingsManager.getMsg().getString("no-permission");
            if (!msg.equalsIgnoreCase("")) {
                if (Main.papi) {
                    msg = PlaceholderAPI.setPlaceholders(player, msg);
                }
                msg = msg.replace("%player", player.getName());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        }

        return true;
    }

    private void noRewardsMessage(Player player, long millis) {
        String norewards = SettingsManager.getMsg().getString("no-rewards");
        if (!norewards.equalsIgnoreCase("")) {
            if (Main.papi) {
                norewards = PlaceholderAPI.setPlaceholders(player, norewards);
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', norewards));
        }

        String cdmsg = SettingsManager.getMsg().getString("cooldown-msg");
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
    }
}

