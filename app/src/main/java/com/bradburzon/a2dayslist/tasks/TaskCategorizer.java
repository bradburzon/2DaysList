package com.bradburzon.a2dayslist.tasks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskCategorizer {
    public Map<TaskStatus, List<Task>> categorizeTasks(List<Task> tasks) {
        return tasks.stream().collect(Collectors.groupingBy(Task::getTaskStatus));
    }
}