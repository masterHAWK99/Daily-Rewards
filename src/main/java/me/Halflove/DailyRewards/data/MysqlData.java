package me.Halflove.DailyRewards.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import me.Halflove.DailyRewards.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MysqlData extends Data {

    private final Main plugin;

    private Connection connection;

    public MysqlData(Main plugin) {
        this.plugin = plugin;

        setup();

        try {
            PreparedStatement ps = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS users(UUID VARCHAR(100), cooldown BIGINT(100), currentIp VARCHAR(100), cooldownOnIp BIGINT(100), PRIMARY KEY(UUID))");
            ps.executeUpdate();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Daily Rewards MySQL: Data Tables Generated");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Tables Failed To Generate");
        }

        getUsers().clear();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                User user = createUser(uuid);

                if (resultSet.getString("currentIp") != null) {
                    user.setCurrentIp(resultSet.getString("currentIp"));
                    user.setCooldownOnIp(resultSet.getLong("cooldownOnIp"));
                }

                user.setCooldown(resultSet.getLong("cooldown"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        String host = plugin.getSettings().getConfiguration().mysql.host;
        int port = plugin.getSettings().getConfiguration().mysql.port;
        String database = plugin.getSettings().getConfiguration().mysql.database;
        String user = plugin.getSettings().getConfiguration().mysql.user;
        String password = plugin.getSettings().getConfiguration().mysql.password;

        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,
                user, password);
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

    @Override
    public long getTime(Player player) {
        User user = getUsers().get(player.getUniqueId());

        if (plugin.getSettings().getConfiguration().saveToIp) {
            if (!player.getAddress().getAddress().getHostAddress().equals(user.getCurrentIp())) {
                return 0L;
            }
            return user.getCooldownOnIp();
        }
        return user.getCooldown();
    }

    // TODO: find a better way to store long SQL queries
    @Override
    public void saveTime(Player player, long millis) {
        setup();

        User user = getUsers().get(player.getUniqueId());
        try {
            if (plugin.getSettings().getConfiguration().saveToIp) {
                String hostAddress = player.getAddress().getAddress().getHostAddress();

                PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO users SET uuid = ?, cooldownOnIp = ?, currentIp = ? ON DUPLICATE KEY UPDATE currentIp = VALUES(currentIp), cooldownOnIp = VALUES(cooldownOnIp)");
                statement.setString(1, player.getUniqueId().toString());
                statement.setLong(2, millis);
                statement.setString(3, hostAddress);

                statement.executeUpdate();

                user.setCurrentIp(hostAddress);
                user.setCooldownOnIp(millis);
            } else {
                PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO users SET uuid = ?, cooldown = ? ON DUPLICATE KEY UPDATE cooldown = VALUES(cooldown)");
                statement.setString(1, player.getUniqueId().toString());
                statement.setLong(2, millis);

                statement.executeUpdate();

                user.setCooldown(millis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MySQL Data failed to update!");
        }
    }
}
