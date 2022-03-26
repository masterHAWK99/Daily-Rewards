package me.Halflove.DailyRewards.manager;

import static me.Halflove.DailyRewards.util.MessageUtils.sendMessageWithPrefix;

import com.google.common.base.Splitter;
import java.util.List;
import java.util.Random;
import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.config.DefaultConfig;
import me.Halflove.DailyRewards.hook.PlaceholderApiHook;
import me.Halflove.DailyRewards.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RewardManager {
    private static final Random r = new Random();

    static Main plugin = Main.getPlugin(Main.class);

    public static void noReward(Player player) {
        String sound = plugin.getSettings().getConfiguration().noRewardSound.type;
        float volume = plugin.getSettings().getConfiguration().noRewardSound.volume;
        float pitch = plugin.getSettings().getConfiguration().noRewardSound.pitch;
        if (plugin.getSettings().getConfiguration().noRewardSound.enabled) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    public static void setReward(final Player player) {
        String sound = plugin.getSettings().getConfiguration().claimSound.type;
        float volume = plugin.getSettings().getConfiguration().claimSound.volume;
        float pitch = plugin.getSettings().getConfiguration().claimSound.pitch;
        if (plugin.getSettings().getConfiguration().claimSound.enabled) {
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
        for (DefaultConfig.Reward reward : plugin.getSettings().getConfiguration().rewards) {
            if (reward.permission && !player.hasPermission("dr." + reward.name)) {
                continue;
            }

            long toSet = Math.abs(System.currentTimeMillis())
                + Math.abs(plugin.getSettings().getConfiguration().cooldown);
            plugin.getData().saveTime(player, toSet);

            String claim = reward.message;
            claim = claim.replace("%name%", reward.name);
            claim = PlaceholderApiHook.replacePlaceholders(player, claim);
            sendMessageWithPrefix(player, claim);
            if (!reward.broadcastMessage.equalsIgnoreCase("")) {
                String msg = reward.broadcastMessage;
                msg = msg.replace("%player", player.getName());
                MessageUtils.sendBroadcastMessage(msg);
            }
            new BukkitRunnable() {
                public void run() {
                    if (reward.random) {
                        List<String> commandList = reward.commands;
                        int index = RewardManager.r.nextInt(commandList.size());
                        String selectedCommand = commandList.get(index);
                        selectedCommand = selectedCommand.replace("%player", player.getName());
                        selectedCommand = PlaceholderApiHook.replacePlaceholders(player, selectedCommand);
                        if (selectedCommand.contains(";")) {
                            List<String> split = Splitter.on(";").splitToList(selectedCommand);
                            for (String finalcommand : split) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalcommand);
                            }
                        } else {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), selectedCommand);
                        }
                    } else {
                        for (String selectedCommand : reward.commands) {
                            selectedCommand = selectedCommand.replace("%player", player.getName());
                            selectedCommand = PlaceholderApiHook.replacePlaceholders(player, selectedCommand);
                            if (selectedCommand.contains(";")) {
                                List<String> split = Splitter.on(";").splitToList(selectedCommand);
                                for (String finalcommand : split) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalcommand);
                                }
                                continue;
                            }
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), selectedCommand);
                        }
                    }
                }
            }.runTaskLater(plugin, 3L);
        }
    }
}

