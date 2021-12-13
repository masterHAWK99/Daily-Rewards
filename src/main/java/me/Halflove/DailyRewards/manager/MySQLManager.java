package me.Halflove.DailyRewards.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MySQLManager {

    private static Connection connection;

    public static void mysqlSetup() {
        String host = SettingsManager.getConfig().getString("mysql.host-name");
        int port = SettingsManager.getConfig().getInt("mysql.port");
        String database = SettingsManager.getConfig().getString("mysql.database");
        String username = SettingsManager.getConfig().getString("mysql.username");
        String password = SettingsManager.getConfig().getString("mysql.password");

        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,
                username, password);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Daily Rewards MySQL: Successfully Connected");
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

    public static boolean playerExistsUUID(UUID uuid, boolean notify) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM druuid WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                if (notify) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Daily Rewards MySQL: Player UUID Found");
                }
                return true;
            }
            if (notify) {
                Bukkit.getConsoleSender()
                    .sendMessage(ChatColor.YELLOW + "Daily Rewards MySQL: New Player UUID Detected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean playerExistsIP(String ip, boolean notify) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM drip WHERE IP=?");
            statement.setString(1, ip);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                if (notify) {
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Daily Rewards MySQL: Player IP Found");
                }
                return true;
            }
            if (notify) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Daily Rewards MySQL: New Player IP Detected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean playerExists(Player player) {
        String ip = player.getAddress().getAddress().getHostAddress();
        ip = ip.replace(".", "-");
        return playerExistsIP(ip, true) && playerExistsUUID(player.getUniqueId(), true);
    }

    public static void createPlayer(Player player) {
        try {
            String ip = player.getAddress().getAddress().getHostAddress();
            ip = ip.replace(".", "-");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM druuid WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM drip WHERE IP=?");
            statement2.setString(1, ip);
            ResultSet results2 = statement2.executeQuery();
            results2.next();
            System.out.print(1);
            if (!playerExists(player)) {
                if (!playerExistsUUID(player.getUniqueId(), false)) {
                    PreparedStatement insert = connection
                        .prepareStatement("INSERT INTO druuid (UUID,Cooldown) VALUES (?,?)");
                    insert.setString(1, player.getUniqueId().toString());
                    insert.setLong(2, 0L);
                    insert.executeUpdate();
                }
                if (!playerExistsIP(ip, false)) {
                    PreparedStatement insert2 = connection
                        .prepareStatement("INSERT INTO drip (IP,Cooldown) VALUES (?,?)");
                    insert2.setString(1, ip);
                    insert2.setLong(2, 0L);
                    insert2.executeUpdate();
                }
                Bukkit.getConsoleSender().sendMessage(
                    ChatColor.GREEN + "Daily Rewards MySQL: New Player Default Data Successfully Inserted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCooldownUUID(UUID uuid, long cooldown) {
        try {
            PreparedStatement statement = connection
                .prepareStatement("UPDATE druuid SET Cooldown=? WHERE UUID=?");
            statement.setLong(1, cooldown);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MySQL UUID data failed to update!");
        }
    }

    public static void updateCooldownIP(String ip, long cooldown) {
        try {
            PreparedStatement statement = connection
                .prepareStatement("UPDATE drip SET Cooldown=? WHERE IP=?");
            statement.setLong(1, cooldown);
            statement.setString(2, ip);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MySQL IP data failed to update!");
        }
    }

    public static long getCooldownUUID(UUID uuid) {
        long output = 0L;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM druuid WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            results.next();
            output = results.getLong("Cooldown");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static long getCooldownIP(String ip) {
        long output = 0L;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM drip WHERE IP=?");
            statement.setString(1, ip);
            ResultSet results = statement.executeQuery();
            results.next();
            output = results.getLong("Cooldown");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void createTable() {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS druuid (UUID VARCHAR(100),Cooldown BIGINT(100),PRIMARY KEY (UUID))");
            ps.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS drip (IP VARCHAR(100),Cooldown BIGINT(100),PRIMARY KEY (IP))");
            ps2.executeUpdate();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Daily Rewards MySQL: Data Tables Generated");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Tables Failed To Generate");
        }
    }
}

