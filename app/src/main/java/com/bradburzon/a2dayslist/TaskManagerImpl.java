package com.bradburzon.a2dayslist;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TaskManagerImpl implements TaskManager {

    private final TaskStorage storage;

    public static TaskManager createTaskManager() {
        return new TaskManagerImpl(new LocalTaskStorage(new HashMap<>()));
    }

    public TaskManagerImpl(TaskStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Task> listTasks() {
        return storage.list();
    }

    @Override
    public List<Task> listTasks(Comparator<Task> taskComparator) {
        List<Task> list = storage.list();
        list.sort(taskComparator);
        return list;
    }

    @Override
    public void markAsComplete(String taskId) {
        storage.getById(taskId).setTaskStatus(TaskStatus.COMPLETED);
    }

    @Override
    public void addTask(String taskName) {
        String taskId = UUID.randomUUID().toString();
        storage.add(new Task(taskId, taskName, storage.list().size(), TaskStatus.CREATED));
    }

    @Override
    public Task getById(String taskId) {
        return storage.getById(taskId);
    }

    @Override
    public void update(String taskId, Task task) {
        storage.update(taskId, task);
    }

    @Override
    public Task delete(String taskId) {
        return storage.delete(taskId);
    }
}
