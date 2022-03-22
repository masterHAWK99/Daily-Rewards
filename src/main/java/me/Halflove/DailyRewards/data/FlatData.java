package me.Halflove.DailyRewards.data;

import java.util.UUID;
import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.manager.Config;
import org.bukkit.entity.Player;

public class FlatData extends Data {

    private final Config data;

    private final Main plugin;

    public FlatData(Main plugin) {
        this.plugin = plugin;
        data = new Config(plugin.getDataFolder(), "data.yml");

        if (data.getConfigurationSection("data") == null) {
            return;
        }

        for (String uuid : data.getConfigurationSection("data").getKeys(false)) {
            User user = createUser(UUID.fromString(uuid));

            user.setCurrentIp(data.getString("data." + uuid + ".currentIp"));
            user.setCooldownOnIp(data.getLong("data." + uuid + ".cooldownOnIp"));
            user.setCooldown(data.getLong("data." + uuid + ".cooldown"));
            user.setStreak(data.getInt("data." + uuid + ".streak"));
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

    @Override
    public int getStreak(Player player) {
        return getUsers().get(player.getUniqueId()).getStreak();
    }

    @Override
    public void saveTime(Player player, long millis) {
        User user = getUsers().get(player.getUniqueId());

        if (plugin.getSettings().getConfiguration().saveToIp) {
            String hostAddress = player.getAddress().getAddress().getHostAddress();
            data.set("data." + player.getUniqueId() + ".cooldownOnIp", millis);
            data.set("data." + player.getUniqueId() + ".currentIp", hostAddress);

            user.setCurrentIp(hostAddress);
            user.setCooldownOnIp(millis);
        } else {
            data.set("data." + player.getUniqueId() + ".cooldown", millis);

            user.setCooldown(millis);
        }

        data.save();
    }

    @Override
    public void saveStreak(Player player, int streak) {
        User user = getUsers().get(player.getUniqueId());

        data.set("data." + player.getUniqueId() + ".streak", streak);
        data.save();

        user.setStreak(streak);
    }
}
