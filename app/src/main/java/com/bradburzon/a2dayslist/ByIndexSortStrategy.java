package com.bradburzon.a2dayslist;

import java.util.Comparator;

public class ByIndexSortStrategy implements Comparator<Task> {

    @Override
    public int compare(Task task1, Task task2) {
        return task1.getIndex() - task2.getIndex();
    }
}
