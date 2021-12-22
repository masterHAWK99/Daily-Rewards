package me.Halflove.DailyRewards.config;

import java.util.Arrays;
import java.util.List;

public class DefaultConfig {

    public boolean updateCheck = true;

    public long cooldown = 846000;

    public boolean saveToIp = false;

    public String data = "flat";

    public Mysql mysql = new Mysql();

    public Sound claimSound = new Sound();
    public Sound noRewardSound = new Sound("BLOCK_ANVIL_LAND");

    public Login claimOnLogin = new Login();

    public List<Reward> rewards = Arrays.asList(new Reward(),
        new Reward("Advanced",
            true,
            true,
            "&aRewards&f: You claimed the &7%name%&f daily reward!",
            "",
            Arrays.asList("give %player minecraft:diamond 1;say %player earned a common diamond",
                "give %player minecraft:emerald 1;say %player earned a rare emerald")));


    public static class Mysql {

        public String host = "localhost";
        public int port = 3306;
        public String database = "db";
        public String user = "root";
        public String password = "pass";
    }

    public static class Sound {

        public boolean enabled = true;
        public String type = "ENTITY_PLAYER_LEVELUP";
        public float volume = 1;
        public float pitch = 1;

        public Sound() {
        }

        public Sound(String type) {
            this.type = type;
        }
    }

    public static class Login {

        public boolean enabled = false;
        public int delay = 3;
    }

    public static class Reward {

        public String name = "Basic";
        public boolean random = false;
        public boolean permission = false;
        public String message = "&aRewards&f: You claimed the &7%name%&f daily reward!";
        public String broadcastMessage = "";
        public List<String> commands = Arrays.asList("give %player minecraft:diamond 1");

        public Reward() {
        }

        public Reward(String name,
                      boolean random,
                      boolean permission,
                      String message,
                      String broadcastMessage,
                      List<String> commands) {
            this.name = name;
            this.random = random;
            this.permission = permission;
            this.message = message;
            this.broadcastMessage = broadcastMessage;
            this.commands = commands;
        }
    }
}

