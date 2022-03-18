package me.Halflove.DailyRewards.util;

import java.util.concurrent.TimeUnit;

public final class DateUtils {

    /**
     * Gets milliseconds in formatted manner.
     * E.g. 1 hour, 37 minutes, 21 seconds
     *
     * @param millis milliseconds to be formatted
     * @return time in formatted manner
     */
    public static String getRemainingTime(long millis) {
        StringBuilder stringBuilder = new StringBuilder();

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;

        if (hours > 0L) {
            stringBuilder.append(hours).append(hours > 1L ? " hours" : " hour");
        }

        if (minutes > 0L) {
            stringBuilder.append(" ");
            stringBuilder.append(minutes).append(minutes > 1L ? " minutes" : " minute");
        }

        if (seconds > 0L) {
            stringBuilder.append(" ");
            stringBuilder.append(seconds).append(seconds > 1L ? " seconds" : " second");
        }

        return stringBuilder.toString();
    }

    /**
     * Gets second-of-minute converted from milliseconds.
     *
     * @param millis milliseconds to be converted
     * @return the second-of-minute, from 0 to 59
     */
    public static String getRemainingSec(long millis) {
        return String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) % 60);
    }

    /**
     * Gets minute-of-hour converted from milliseconds.
     *
     * @param millis milliseconds to be converted
     * @return the minute-of-hour, from 0 to 59
     */
    public static String getRemainingMin(long millis) {
        return String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis) % 60);
    }

    /**
     * Gets hours converted from milliseconds.
     *
     * @param millis milliseconds to be converted
     * @return hours converted from milliseconds
     */
    public static String getRemainingHour(long millis) {
        return String.valueOf(TimeUnit.MILLISECONDS.toHours(millis));
    }
}
