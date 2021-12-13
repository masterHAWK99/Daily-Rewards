package me.Halflove.DailyRewards.command;

import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.manager.MySQLManager;
import me.Halflove.DailyRewards.manager.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
                    final boolean startmysql;
                    startmysql = !SettingsManager.getConfig().getBoolean("mysql.enabled");
                    this.plugin.settings.reloadData();
                    this.plugin.settings.reloadConfig();
                    this.plugin.settings.reloadMsg();
                    SettingsManager.saveData();
                    this.plugin.settings.saveConfig();
                    this.plugin.settings.saveMsg();
                    sender.sendMessage(ChatColor.YELLOW + "DailyRewards is reloading...");
                    (new BukkitRunnable() {
                        public void run() {
                            if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                                if (startmysql) {
                                    MySQLManager.mysqlSetup();
                                    MySQLManager.createTable();
                                } else {
                                    MySQLManager.createTable();
                                }
                            }
                            sender.sendMessage(ChatColor.GREEN + "DailyRewards has been successfully reloaded.");
                        }
                    }).runTaskLater(this.plugin, 20L);
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (sender instanceof Player) {
                        if (args.length == 1) {
                            Player player = (Player) sender;
                            String ip = player.getAddress().getAddress().getHostAddress();
                            ip = ip.replace(".", "-");
                            SettingsManager.getData().set(ip + ".millis", Integer.valueOf(0));
                            SettingsManager.getData().set(player.getUniqueId() + ".millis", Integer.valueOf(0));
                            if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
                                MySQLManager.updateCooldownIP(ip, 0L);
                                MySQLManager.updateCooldownUUID(player.getUniqueId(), 0L);
                            }
                            sender.sendMessage(ChatColor.GREEN + "You reset your cooldown.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Oops, you can't do this in console");
                        sender.sendMessage(ChatColor.RED + "Try '/dr reset (player)' instead");
                    }
                    if (args.length == 2) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "The specified player is offline.");
                            return true;
                        }
                        String ip = target.getAddress().getAddress().getHostAddress();
                        ip = ip.replace(".", "-");
                        SettingsManager.getData().set(ip + ".millis", Integer.valueOf(0));
                        SettingsManager.getData().set(target.getUniqueId() + ".millis", Integer.valueOf(0));
                        sender.sendMessage(ChatColor.GREEN + "You reset " + target.getName() + "'s cooldown.");
                    }
                }
                return true;
            }
            String msg = SettingsManager.getMsg().getString("no-permission");
            msg = msg.replace("%player", sender.getName());
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return true;
    }
}
