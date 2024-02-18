package com.bradburzon.a2dayslist;

import java.util.UUID;

public class TaskManager {

    private TaskStorage storage;

    public TaskManager(TaskStorage storage) {
        this.storage = storage;
    }

    public void markAsComplete(String taskId) {
        storage.getById(taskId).setTaskStatus(TaskStatus.COMPLETED);
    }

    public void addTask(String taskName) {
        String taskId = UUID.randomUUID().toString();
        storage.add(new Task(taskId, taskName, storage.list().size(), TaskStatus.CREATED));
    }
}
