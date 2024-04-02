package com.bradburzon.a2dayslist.tasks;

import java.util.List;
import java.util.stream.Collectors;

public class TaskFilter {

    public static List<Task> filterByStatus(List<Task> tasks, TaskStatus status){
        return tasks.stream().filter(task -> task.getTaskStatus() == status).collect(Collectors.toList());
    }

    public static List<Task> filterByStatus(List<Task> tasks, TaskStatus status1, TaskStatus status2){
        return tasks.stream().filter(task -> task.getTaskStatus() == status1 || task.getTaskStatus() == status2).collect(Collectors.toList());
    }
}
