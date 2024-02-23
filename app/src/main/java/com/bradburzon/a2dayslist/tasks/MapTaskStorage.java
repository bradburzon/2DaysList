package com.bradburzon.a2dayslist.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapTaskStorage implements TaskStorage {

    private final Map<String, Task> data;

    public MapTaskStorage(Map<String, Task> startingData) {
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
