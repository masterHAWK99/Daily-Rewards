package me.Halflove.DailyRewards.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagesConfig {

    public String noRewards = "&fYou do not have any available rewards at the moment.";
    public String cooldown = "&fTime until next reward: %time%.";
    public String noPermission = "&fYou do not have permission to do this.";
    public String rewardAvailable = "&fYou have unclaimed daily rewards, do &e/reward &fto claim!";

    public PapiPlaceholders papiPlaceholders = new PapiPlaceholders();

    public static class PapiPlaceholders {

        public String rewardAvailable = "Unclaimed Rewards Available!";
        public String noRewards = "No Rewards Available";
    }
}



