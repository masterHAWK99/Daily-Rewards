package me.Halflove.DailyRewards.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.BiConsumer;
import me.Halflove.DailyRewards.Main;
import org.bukkit.Bukkit;

public class UpdateChecker {

    private final Main plugin;
    private final int resourceId;

    public UpdateChecker(Main plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void checkVersion(BiConsumer<String, String> consumer) {
        if (!plugin.getSettings().getConfiguration().updateCheck) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL(
                "https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next(), this.plugin.getDescription().getVersion());
                }
            } catch (IOException exception) {
                this.plugin.getLogger().info("Update checker encountered an error: " + exception.getMessage());
            }
        });
    }

}

