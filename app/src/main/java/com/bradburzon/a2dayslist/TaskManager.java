package com.bradburzon.a2dayslist;

import java.util.Comparator;
import java.util.List;

public interface TaskManager {

    List<Task> listTasks();

    List<Task> listTasks(Comparator<Task> taskComparator);

    void markAsComplete(String taskId);

    void addTask(String taskName);

    Task getById(String taskId);

    void update(String taskId, Task task);

    Task delete(String taskId);
}
