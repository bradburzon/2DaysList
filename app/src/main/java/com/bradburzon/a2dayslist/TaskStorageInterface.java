package com.bradburzon.a2dayslist;

import java.util.List;

public interface TaskStorageInterface {
    void addTask(Task task);
    void deleteTask(String taskId);
    List<Task> getTasks();
}