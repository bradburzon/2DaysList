package com.bradburzon.a2dayslist.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Calendar;

public class SetupAlarmManagerTest {

    @Test
    public void testGetNextMidnightMillis() {
        long nextMidnightMillis = SetupAlarmManager.getNextMidnightMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nextMidnightMillis);

        assertEquals( 0, calendar.get(Calendar.HOUR_OF_DAY));
        assertEquals( 0, calendar.get(Calendar.MINUTE));
        assertEquals( 0, calendar.get(Calendar.SECOND));
        assertEquals( 0, calendar.get(Calendar.MILLISECOND));

        // Additionally, you might want to check that the day has indeed advanced.
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        assertTrue( calendar.get(Calendar.DAY_OF_YEAR) > now.get(Calendar.DAY_OF_YEAR) || calendar.get(Calendar.YEAR) > now.get(Calendar.YEAR));
    }
}
