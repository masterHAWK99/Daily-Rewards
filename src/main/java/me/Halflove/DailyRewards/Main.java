package me.Halflove.DailyRewards;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import me.Halflove.DailyRewards.command.AdminCommands;
import me.Halflove.DailyRewards.command.RewardCommands;
import me.Halflove.DailyRewards.listener.PlayerJoinListener;
import me.Halflove.DailyRewards.manager.MySQLManager;
import me.Halflove.DailyRewards.manager.PAPIExtensions;
import me.Halflove.DailyRewards.manager.SettingsManager;
import me.Halflove.DailyRewards.manager.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static boolean papi;
    public static Connection connection;

    public SettingsManager settings = SettingsManager.getInstance();

    public void onEnable() {
        getCommand("dailyrewards").setExecutor(new AdminCommands(this));
        getCommand("reward").setExecutor(new RewardCommands());
        this.settings.setup(this);
        registerEvents();
        if (SettingsManager.getConfig().getBoolean("mysql.enabled")) {
            mysqlSetup();
            MySQLManager.createTable();
            for (Player player : Bukkit.getOnlinePlayers()) {
                MySQLManager.createPlayer(player);
            }
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            papi = true;
            new PAPIExtensions().register();
        } else {
            papi = false;
        }

        new UpdateChecker(this, 16708).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                getLogger().info("Plugin is up to date.");
            } else {
                getLogger().severe("*** Daily Rewards is Outdated! ***");
                getLogger().severe("*** You're on " + this.getDescription().getVersion() + " while " + version + " is available! ***");
                getLogger().severe("*** Update Here: https://www.spigotmc.org/resources/daily-rewards.16708/ ***");
            }
        });

    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }


    public void mysqlSetup() {
        String host = SettingsManager.getConfig().getString("mysql.host-name");
        int port = SettingsManager.getConfig().getInt("mysql.port");
        String database = SettingsManager.getConfig().getString("mysql.database");
        String username = SettingsManager.getConfig().getString("mysql.username");
        String password = SettingsManager.getConfig().getString("mysql.password");

        try {
            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,
                    username, password));
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Daily Rewards MySQL: Successfully Connected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Failed To Connected");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Error 'SQLException'");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Your MySQL Configuration Information Is Invalid");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Failed To Connect");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Error 'ClassNotFoundException'");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        Main.connection = connection;
    }

}

