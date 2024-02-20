package com.bradburzon.a2dayslist;

import java.util.Comparator;
import java.util.List;

public interface TaskStorage {

    void add(Task task);

    Task delete(String taskId);

    void update(String taskId, Task task);

    Task getById(String taskId);

    List<Task> list();

    void sort(Comparator<Task> taskComparator);
}

