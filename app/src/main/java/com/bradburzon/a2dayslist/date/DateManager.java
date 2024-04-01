package com.bradburzon.a2dayslist.date;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DateManager {
    private final File dateFile;
    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM:dd:yyyy", Locale.getDefault());


    public DateManager(String filePath) {
        this.dateFile = new File(filePath);
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!dateFile.exists()) {
                System.out.println("Creating new date file at " + dateFile.getPath());
                Objects.requireNonNull(dateFile.getParentFile()).mkdirs();
                dateFile.createNewFile();
                setLastActiveDate(Calendar.getInstance()); // Initialize with current date
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create new date file at " + dateFile.getPath(), e);
        }
    }

    public Calendar getLastActiveDate() {
        try (BufferedReader reader = new BufferedReader(new FileReader(dateFile))) {
            String line = reader.readLine();
            if (line != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Objects.requireNonNull(DATE_FORMATTER.parse(line)));
                return calendar;
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Failed to read last active date from file", e);
        }
        return null; // Or Calendar.getInstance() if you want a default value
    }

    public void setLastActiveDate(Calendar date) {
        try (FileWriter fileWriter = new FileWriter(dateFile, false)) {
            fileWriter.write(DATE_FORMATTER.format(date.getTime()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to update last active date in file", e);
        }
    }

    // In DateManager class (hypothetical example, not actual recommendation)
    public void setCustomDateFormat(String format) {
        this.DATE_FORMATTER.applyPattern(format);
    }

    public Calendar removeTimeComponents(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}