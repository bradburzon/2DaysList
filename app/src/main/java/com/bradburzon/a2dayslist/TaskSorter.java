package com.bradburzon.a2dayslist;

import java.util.Comparator;

public class TaskSorter {
    public static final class NameSort implements Comparator<Task> {

        @Override
        public int compare(Task task1, Task task2) {
            return task1.getName().compareTo(task2.getName());
        }
    }
}
