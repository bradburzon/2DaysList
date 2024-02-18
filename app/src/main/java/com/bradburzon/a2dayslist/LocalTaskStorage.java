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

    }

    @Override
    public void delete(String taskId) {

    }

    @Override
    public void update(String taskID, Task task) {

    }

    @Override
    public List<Task> list() {
        return new ArrayList<Task>(data.values());
    }
}
