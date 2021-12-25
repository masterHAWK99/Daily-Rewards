package me.Halflove.DailyRewards.command;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {
    private final Main plugin;

    public AdminCommands(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dailyrewards")) {
            if (sender.isOp() || sender.hasPermission("dr.admin")) {
                if (args.length == 0 || args[0].equalsIgnoreCase("help") || args.length > 2
                    || (!args[0].equalsIgnoreCase("reset") && !args[0].equalsIgnoreCase("reload"))) {
                    sender.sendMessage(ChatColor.BOLD + "DailyRewards Admin Help");
                    sender.sendMessage(ChatColor.YELLOW + "/dr reload" + ChatColor.WHITE + ChatColor.ITALIC
                        + " Reload all DR files.");
                    sender.sendMessage(ChatColor.YELLOW + "/dr reset" + ChatColor.WHITE + ChatColor.ITALIC
                        + " Reset your cooldown.");
                    sender.sendMessage(ChatColor.YELLOW + "/dr reset (player)" + ChatColor.WHITE + ChatColor.ITALIC
                        + " Reset a player's cooldown.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ChatColor.YELLOW + "DailyRewards is reloading...");
                    // TODO: Move loadConfigs to the main class and add a data provider
                    plugin.getSettings().loadConfigs();
                    sender.sendMessage(ChatColor.GREEN + "DailyRewards has been successfully reloaded.");
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Player player = (Player) sender;
                            plugin.getData().saveTime(player, 0L);
                            sender.sendMessage(ChatColor.GREEN + "You reset your cooldown.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Oops, you can't do this in console");
                            sender.sendMessage(ChatColor.RED + "Try '/dr reset (player)' instead");
                        }

                        return true;
                    }

                    Player target = Bukkit.getServer().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "The specified player is offline.");
                        return true;
                    }
                    plugin.getData().saveTime(target, 0L);
                    sender.sendMessage(ChatColor.GREEN + "You reset " + target.getName() + "'s cooldown.");
                }
                return true;
            }
            String msg = plugin.getSettings().getMessagesConfig().noPermission;
            msg = msg.replace("%player", sender.getName());
            MessageUtils.sendMessage(sender, msg);
        }
        return true;
    }
}
