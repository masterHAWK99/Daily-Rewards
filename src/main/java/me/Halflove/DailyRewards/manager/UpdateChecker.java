package me.Halflove.DailyRewards.manager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Collectors;
import me.Halflove.DailyRewards.Main;
import me.Halflove.DailyRewards.util.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UpdateChecker {

    public static final int UPDATE_INTERVAL_IN_SECS = 21600;
    private static final String GIT_REPO_LINK = "https://api.github.com/repos/masterHAWK99/Daily-Rewards/releases/latest";
    private final Main plugin;
    private final String currentVersion;
    private String newVersion;

    public UpdateChecker(Main plugin) {
        this.plugin = plugin;
        this.currentVersion = this.plugin.getDescription().getVersion();
    }

    public void runCheckVersionTask() {
        if (!plugin.getSettings().getConfiguration().updateCheck) {
            return;
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            if (!isVersionSame()) {
                this.plugin.getLogger().warning("*** Daily Rewards is Outdated! ***");
                this.plugin.getLogger().warning(String.format("*** You're on %s while %s is available! ***",
                    currentVersion,
                    newVersion));
                this.plugin.getLogger().warning("*** Update here: https://bit.ly/3tlaJdr ***");
            }
        }, 0, 20 * UPDATE_INTERVAL_IN_SECS);
    }

    public void checkVersion(Player player) {
        if (!plugin.getSettings().getConfiguration().updateCheck) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (!isVersionSame()) {
                MessageUtils.sendMessage(player, "&6*** Daily Rewards is Outdated! ***");
                MessageUtils.sendMessage(player, String.format("&eYou're on &f%s &ewhile &f%s &eis available!",
                    currentVersion,
                    newVersion));
                MessageUtils.sendMessage(player, "&eUpdate here: &fhttps://bit.ly/3tlaJdr");
            }
        });
    }

    private boolean isVersionSame() {
        try {
            URL url = new URL(GIT_REPO_LINK);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();

            // TODO: use gson
            String jsonString;
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()))) {
                jsonString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }

            JsonElement jsonElement = JsonParser.parseString(jsonString);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String tag = jsonObject.get("tag_name").getAsString();

            this.newVersion = tag;

            return tag.equals(this.currentVersion);
        } catch (IOException e) {
            this.plugin.getLogger().severe("Something went wrong while checking for new version.");
        }
        return false;
    }
}

