package me.Halflove.DailyRewards.util;

public final class DateUtils {

    public static String formatTime(long secs) {
        String str;
        long seconds = secs;
        long minutes = 0L;
        while (seconds >= 60L) {
            seconds -= 60L;
            minutes++;
        }
        long hours = 0L;
        while (minutes >= 60L) {
            minutes -= 60L;
            hours++;
        }
        if (hours != 0L) {
            if (hours > 1L) {
                str = hours + " Hours";
            } else if (minutes > 61L) {
                str = hours + " Hour " + minutes + " Minutes";
            } else if (minutes == 61L) {
                str = hours + " Hour " + minutes + " Minute";
            } else {
                str = hours + " Hour";
            }
        } else if (minutes != 0L) {
            if (seconds == 0L) {
                if (minutes == 1L) {
                    str = minutes + " Minute";
                } else {
                    str = minutes + " Minutes";
                }
            } else if (minutes == 1L) {
                if (seconds == 1L) {
                    str = minutes + " Minute " + seconds + " Second";
                } else {
                    str = minutes + " Minute " + seconds + " Seconds";
                }
            } else if (seconds == 1L) {
                str = minutes + " Minutes " + seconds + " Second";
            } else {
                str = minutes + " Minutes " + seconds + " Seconds";
            }
        } else if (seconds == 1L) {
            str = seconds + " Second";
        } else {
            str = seconds + " Seconds";
        }
        if (secs <= 0L) {
            str = "0 Seconds";
        }
        return str;
    }

    public static String getRemainingTime(long millis) {
        long seconds = millis / 1000L;
        return formatTime(seconds);
    }

    public static String getRemainingSec(long millis) {
        long seconds = millis / 1000L;
        long minutes = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            minutes++;
        }
        while (minutes > 60L) {
            minutes -= 60L;
        }
        return (new StringBuilder(String.valueOf(seconds))).toString();
    }

    public static String getRemainingMin(long millis) {
        long seconds = millis / 1000L;
        long minutes = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            minutes++;
        }
        while (minutes > 60L) {
            minutes -= 60L;
        }
        return (new StringBuilder(String.valueOf(minutes))).toString();
    }

    public static String getRemainingHour(long millis) {
        long seconds = millis / 1000L;
        long minutes = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            minutes++;
        }
        long hours = 0L;
        while (minutes > 60L) {
            minutes -= 60L;
            hours++;
        }
        return (new StringBuilder(String.valueOf(hours))).toString();
    }
}
