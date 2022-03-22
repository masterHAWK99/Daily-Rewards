package me.Halflove.DailyRewards.data;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.config.DefaultConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MysqlData extends Data {

    private final Main plugin;

    private final HikariDataSource hikariDataSource;

    public MysqlData(Main plugin) {
        this.plugin = plugin;

        DefaultConfig config = plugin.getSettings().getConfiguration();

        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl("jdbc:mysql://" + config.mysql.host + ":" + config.mysql.port + "/" + config.mysql.database);
        hikariDataSource.addDataSourceProperty("useSSL", config.mysql.useSsl);
        hikariDataSource.setUsername(config.mysql.user);
        hikariDataSource.setPassword(config.mysql.password);
        hikariDataSource.setMaxLifetime(config.mysql.maxLifetime * 1000L);

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(
                 "CREATE TABLE IF NOT EXISTS users(UUID VARCHAR(100), cooldown BIGINT(100), currentIp VARCHAR(100), cooldownOnIp BIGINT(100), streak MEDIUMINT, PRIMARY KEY(UUID))")) {
            ps.executeUpdate();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Daily Rewards MySQL: Data Tables Generated");
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Daily Rewards MySQL: Tables Failed To Generate");
        }

        boolean isStreakColumnExists = false;
        try (ResultSet resultSet = getConnection().getMetaData().getColumns(null, null, "users", "streak")) {
            isStreakColumnExists = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isStreakColumnExists) {
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("ALTER TABLE users "
                     + "ADD COLUMN `streak` MEDIUMINT;")) {
                preparedStatement.executeUpdate();
                plugin.getLogger().info("Missing streak column added.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        getUsers().clear();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                User user = createUser(uuid);

                if (resultSet.getString("currentIp") != null) {
                    user.setCurrentIp(resultSet.getString("currentIp"));
                    user.setCooldownOnIp(resultSet.getLong("cooldownOnIp"));
                }

                user.setCooldown(resultSet.getLong("cooldown"));
                user.setStreak(resultSet.getInt("streak"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
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

    @Override
    public int getStreak(Player player) {
        return getUsers().get(player.getUniqueId()).getStreak();
    }

    // TODO: find a better way to store long SQL queries
    @Override
    public void saveTime(Player player, long millis) {
        User user = getUsers().get(player.getUniqueId());
        try {
            if (plugin.getSettings().getConfiguration().saveToIp) {
                String hostAddress = player.getAddress().getAddress().getHostAddress();

                try (Connection connection = getConnection();
                     PreparedStatement statement = connection.prepareStatement("INSERT INTO users "
                         + "SET uuid = ?, cooldownOnIp = ?, currentIp = ? "
                         + "ON DUPLICATE KEY UPDATE currentIp = VALUES(currentIp), "
                         + "cooldownOnIp = VALUES(cooldownOnIp)")) {
                    statement.setString(1, player.getUniqueId().toString());
                    statement.setLong(2, millis);
                    statement.setString(3, hostAddress);

                    statement.executeUpdate();

                    user.setCurrentIp(hostAddress);
                    user.setCooldownOnIp(millis);
                }
            } else {
                try (Connection connection = getConnection();
                     PreparedStatement statement = connection
                         .prepareStatement("INSERT INTO users "
                             + "SET uuid = ?, cooldown = ? "
                             + "ON DUPLICATE KEY UPDATE cooldown = VALUES(cooldown)")
                ) {

                    statement.setString(1, player.getUniqueId().toString());
                    statement.setLong(2, millis);

                    statement.executeUpdate();

                    user.setCooldown(millis);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "MySQL Data failed to update!");
        }
    }

    @Override
    public void saveStreak(Player player, int streak) {
        User user = getUsers().get(player.getUniqueId());

        try (Connection connection = getConnection();
             PreparedStatement statement = connection
                 .prepareStatement("INSERT INTO users "
                     + "SET uuid = ?, streak = ? "
                     + "ON DUPLICATE KEY UPDATE streak = VALUES(streak)")
        ) {

            statement.setString(1, player.getUniqueId().toString());
            statement.setInt(2, streak);

            statement.executeUpdate();

            user.setStreak(streak);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("MySQL Data failed to update!");
        }
    }
}
