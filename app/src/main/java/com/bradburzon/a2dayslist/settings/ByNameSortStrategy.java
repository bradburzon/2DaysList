package com.bradburzon.a2dayslist.settings;

import com.bradburzon.a2dayslist.tasks.Task;

import java.util.Comparator;

public class ByNameSortStrategy implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        return task1.getName().compareToIgnoreCase(task2.getName());
    }
}
