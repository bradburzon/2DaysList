package com.bradburzon.a2dayslist;

import java.util.ArrayList;
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
        return new ArrayList<>(data.values());
    }
}
