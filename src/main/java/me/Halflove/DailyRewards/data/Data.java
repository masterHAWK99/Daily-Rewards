package me.Halflove.DailyRewards.data;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import me.Halflove.DailyRewards.Main;
import org.bukkit.entity.Player;

public abstract class Data {

    private static final Map<UUID, User> USERS = Maps.newHashMap();

    /**
     * Gets the correct data model based on the plugin configuration.
     *
     * @param plugin an instance of the main plugin class
     * @return data model based on the plugin configuration
     */
    public static Data getDataProvider(Main plugin) {
        String dataModel = plugin.getSettings().getConfiguration().data.toLowerCase();
        switch (dataModel) {
            case "flat":
                return new FlatData(plugin);
            case "mysql":
                return new MysqlData(plugin);
            default:
                throw new RuntimeException(String.format("Data model %s does not exist!", dataModel));
        }
    }

    /**
     * @return list of user objects created
     */
    public static Map<UUID, User> getUsers() {
        return USERS;
    }

    /**
     * @param player to get his UUID
     * @return remaining time until the next reward in milliseconds
     */
    public abstract long getTime(Player player);

    /**
     * Returns player's reward streak.
     *
     * @param player to find its streak
     * @return number of reward streak
     */
    public abstract int getStreak(Player player);

    /**
     * Sets new remaining time until the next reward.
     *
     * @param player to get his UUID
     * @param millis remaining time in milliseconds
     */
    public abstract void saveTime(Player player, long millis);

    /**
     * Sets new reward streak for player.
     *
     * @param player player to assign streak to
     * @param streak new streak number
     */
    public abstract void saveStreak(Player player, int streak);

    /**
     * Creates User and saves in memory.
     *
     * @param player player to built User with his UUID
     * @return User if exists otherwise creates new User
     */
    public User createUser(Player player) {
        return createUser(player.getUniqueId());
    }

    /**
     * Creates User and saves in memory.
     *
     * @param uuid to built User with
     * @return User if exists otherwise creates new User
     */
    public User createUser(UUID uuid) {
        if (USERS.containsKey(uuid)) {
            return USERS.get(uuid);
        }
        User user = new User();
        USERS.put(uuid, user);
        return user;
    }
}
