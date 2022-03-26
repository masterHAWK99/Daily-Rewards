package me.Halflove.DailyRewards.command;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.hook.PlaceholderApiHook;
import me.Halflove.DailyRewards.manager.RewardManager;
import me.Halflove.DailyRewards.util.DateUtils;
import me.Halflove.DailyRewards.util.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardCommands implements CommandExecutor {

    private final Main plugin;

    public RewardCommands(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        RewardManager rewardManager = plugin.getRewardManager();
        if (player.hasPermission("dr.claim")) {
            if (System.currentTimeMillis() > plugin.getData().getTime(player)) {
                rewardManager.setReward(player);
                return true;
            }
            noRewardsMessage(player, plugin.getData().getTime(player) - System.currentTimeMillis());
            rewardManager.noReward(player);
        } else {
            String msg = plugin.getSettings().getMessagesConfig().noPermission;
            msg = PlaceholderApiHook.replacePlaceholders(player, msg);
            msg = msg.replace("%player", player.getName());
            MessageUtils.sendMessageWithPrefix(player, msg);
        }

        return true;
    }

    private void noRewardsMessage(Player player, long millis) {
        String norewards = plugin.getSettings().getMessagesConfig().noRewards;
        norewards = PlaceholderApiHook.replacePlaceholders(player, norewards);
        MessageUtils.sendMessageWithPrefix(player, norewards);

        String cdmsg = plugin.getSettings().getMessagesConfig().cooldown;
        cdmsg = cdmsg.replace("%time%", DateUtils.getRemainingTime(millis));
        cdmsg = cdmsg.replace("%s%", DateUtils.getRemainingSec(millis));
        cdmsg = cdmsg.replace("%m%", DateUtils.getRemainingMin(millis));
        cdmsg = cdmsg.replace("%h%", DateUtils.getRemainingHour(millis));
        cdmsg = PlaceholderApiHook.replacePlaceholders(player, cdmsg);
        MessageUtils.sendMessageWithPrefix(player, cdmsg);
    }
}

