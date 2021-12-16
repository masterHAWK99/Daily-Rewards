package me.Halflove.DailyRewards.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagesConfig {

    public String noRewards = "&aRewards&f: &fYou do not have any available rewards at the moment.";
    public String cooldown = "&aRewards&f: &fTime until next reward: %time%.";
    public String noPermission = "&aRewards&f: &fYou do not have permission to do this.";
    public String rewardAvailable = "&aRewards&f: &fYou have unclaimed daily rewards, do &e/reward &fto claim!";

    public PapiPlaceholders papiPlaceholders = new PapiPlaceholders();

    public static class PapiPlaceholders {

        public String rewardAvailable = "Unclaimed Rewards Available!";
        public String noRewards = "No Rewards Available";
    }
}



