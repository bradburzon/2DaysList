package com.bradburzon.a2dayslist;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Task {
    private final String taskId;
    private String name;
    private Date expiresIn;
    private boolean isCompleted;

    public Task(String name) {
        this.taskId = UUID.randomUUID().toString();
        this.name = name;
        this.expiresIn = calculateExpiryDate(2); // Sets expiresIn to 2 days from now.
        this.isCompleted = false;
    }

    private Date calculateExpiryDate(int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar.getTime();
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getExpiresIn() {
        return expiresIn;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isExpired() {
        return new Date().after(this.expiresIn);
    }
}
