package com.bradburzon.a2dayslist.date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateManagerTest {
    private DateManager dateManager;
    private File testFile;

    @Before
    public void setUp() throws Exception {
        // Create a temporary file and mark it for deletion on exit.
        testFile = File.createTempFile("test_date", ".txt");
        testFile.deleteOnExit();
        dateManager = new DateManager(testFile.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        assertTrue("Test file deletion failed", testFile.delete());
    }

    @Test
    public void testEnsureFileExists_CreatesFileWhenNotExists() {
        String nonExistentFilePath = "some/temporary/path/date.txt";
        DateManager dm = new DateManager(nonExistentFilePath);

        File file = new File(nonExistentFilePath);

        assertTrue("File should have been created", file.exists());
        file.delete();
    }


    @Test
    public void setLastActiveDate_DateSet_DateMatchesExpected() {
        Calendar expectedDate = Calendar.getInstance();
        expectedDate.set(2022, Calendar.MARCH, 24, 0, 0, 0);
        expectedDate.set(Calendar.MILLISECOND, 0);

        dateManager.setLastActiveDate(expectedDate);
        Calendar actualDate = dateManager.getLastActiveDate();
        actualDate.set(Calendar.MILLISECOND, 0);

        assertEquals("The dates should match", expectedDate.getTime(), actualDate.getTime());
    }


    @Test
    public void getLastActiveDate_AfterSettingDate_ReturnsCorrectDate() {
        // Arrange
        Calendar setDate = Calendar.getInstance();
        setDate.set(2022, Calendar.APRIL, 15,  0, 0, 0); // Set to April 15, 2022
        dateManager.setLastActiveDate(setDate);
        setDate.set(Calendar.MILLISECOND, 0);

        Calendar retrievedDate = dateManager.getLastActiveDate();
        retrievedDate.set(Calendar.MILLISECOND, 0);

        assertEquals("The retrieved date should match the set date", setDate.getTime(), retrievedDate.getTime());
    }


    @Test(expected = RuntimeException.class)
    public void setLastActiveDate_InvalidDateFormat_ThrowsRuntimeException() {
        dateManager.setCustomDateFormat("invalid format");

        Calendar date = Calendar.getInstance();
        dateManager.setLastActiveDate(date);
    }

    @Test
    public void setLastActiveDate_ToFutureDate_DateIsCorrectlySet() {
        Calendar futureDate = Calendar.getInstance();
        futureDate.add(Calendar.DAY_OF_YEAR, 10);
        futureDate = dateManager.removeTimeComponents(futureDate);

        dateManager.setLastActiveDate(futureDate);
        Calendar retrievedDate = dateManager.getLastActiveDate();

        assertEquals("Future date should be correctly set and retrieved",
                futureDate.getTime(), retrievedDate.getTime());
    }
}