package me.Halflove.DailyRewards.data;

public class User {

    private long cooldown;
    private long cooldownOnIp;

    private String currentIp;

    private int streak;

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }

    public long getCooldownOnIp() {
        return cooldownOnIp;
    }

    public void setCooldownOnIp(long cooldownOnIp) {
        this.cooldownOnIp = cooldownOnIp;
    }

    public String getCurrentIp() {
        return currentIp;
    }

    public void setCurrentIp(String currentIp) {
        this.currentIp = currentIp;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }
}
