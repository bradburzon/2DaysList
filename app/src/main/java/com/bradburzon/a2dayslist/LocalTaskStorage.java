package com.bradburzon.a2dayslist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalTaskStorage implements TaskStorage {

    private Map<String,Task> data;

    public LocalTaskStorage(Map<String, Task> startingData) {
        this.data = startingData;
    }

    @Override
    public void add(Task task) {
        data.put(task.getTaskId(), task);
    }

    @Override
    public Task delete(String taskId) {
        return data.remove(taskId);
    }

    @Override
    public void update(String taskId, Task task) {
        data.put(taskId, task);
    }

    @Override
    public Task getById(String taskId) {
        return data.get(taskId);
    }

    @Override
    public List<Task> list() {
        return new ArrayList<Task>(data.values());
    }

    @Override
    public void sort(Comparator taskComparator) {
        List<Task> sortedTasks = new ArrayList<>(data.values());
        System.out.println(sortedTasks);
        Collections.sort(sortedTasks, taskComparator);
        System.out.println(sortedTasks);
        HashMap<String, Task> sortedData = new HashMap<>();
        int i = 0;
        for(Task task : sortedTasks){
            sortedData.put(i + "", task);
            i++;
        }
        data = sortedData;
    }
}
