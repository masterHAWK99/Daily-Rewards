package me.Halflove.DailyRewards.util;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

    @Test
    void testRemainingSec() {
        int remainingSec = Integer.parseInt(DateUtils.getRemainingSec(System.currentTimeMillis()));
        assertFalse(remainingSec < 0, "The number of seconds in a minute is less than zero");
        assertFalse(remainingSec > 60, "The number of seconds in a minute is more than sixty");
    }

    @Test
    void testRemainingMin() {
        int remainingMin = Integer.parseInt(DateUtils.getRemainingMin(System.currentTimeMillis()));
        assertFalse(remainingMin < 0, "The minutes in an hour are less than zero");
        assertFalse(remainingMin > 60, "The number of minutes in an hour is greater than sixty");
    }

    @Test
    void testRemainingHour() {
        int remainingHour = Integer.parseInt(DateUtils.getRemainingHour(System.currentTimeMillis()));
        assertFalse(remainingHour < 0, "The number of hours is less than zero");
    }
}