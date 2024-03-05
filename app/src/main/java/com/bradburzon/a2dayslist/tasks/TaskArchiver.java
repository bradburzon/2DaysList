package com.bradburzon.a2dayslist.tasks;

public class TaskArchiver {

    private final TaskArchivingStrategy archivingStrategy;

    public TaskArchiver(TaskArchivingStrategy strategy) {
        this.archivingStrategy = strategy;
    }

    public void archiveTasks(TaskManager taskManager) {
        archivingStrategy.archiveTasks(taskManager);
    }
}