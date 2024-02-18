package com.bradburzon.a2dayslist;

public class TaskManager {

    private TaskStorage storage;

    public TaskManager(TaskStorage storage) {
        this.storage = storage;
    }

    public void markAsComplete(String taskId) {
        storage.getById(taskId).setTaskStatus(TaskStatus.COMPLETED);
    }
}
