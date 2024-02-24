package com.bradburzon.a2dayslist.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocalTaskStorage implements TaskStorage {

    private final File data;

    public LocalTaskStorage(String filePath) {
        this.data = new File(filePath);
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            if (!data.exists()) {
                System.out.println("Creating new file at " + data.getPath());
                data.getParentFile().mkdirs();
                data.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create new file at " + data.getPath(), e);
        }
    }

    @Override
    public void add(Task task) {
        try (FileWriter fileWriter = new FileWriter(data, true)) {
            fileWriter.append(formatTaskForStorage(task)).append("\n");
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add task to file", e);
        }
    }

    @Override
    public Task delete(String taskId) {
        List<Task> tasks = list();
        Task deletedTask = tasks.stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task ID " + taskId + " does not exist."));

        tasks.remove(deletedTask);
        storeListOfTasks(tasks);

        return deletedTask;
    }

    private void storeListOfTasks(List<Task> tasks) {
        try (FileWriter fileWriter = new FileWriter(data, false)) {
            for (Task task : tasks) {
                fileWriter.append(formatTaskForStorage(task)).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store tasks to file", e);
        }
    }

    private String formatTaskForStorage(Task task) {
        return task.getTaskId() + ":" +
                task.getName() + ":" +
                task.getIndex() + ":" +
                task.getTaskStatus().ordinal();
    }

    @Override
    public void update(String taskId, Task task) {
        delete(taskId);
        add(task);
    }

    @Override
    public Task getById(String taskId) {
        return list().stream()
                .filter(task -> task.getTaskId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task ID " + taskId + " does not exist."));
    }

    @Override
    public List<Task> list() {
        List<Task> tasks = new ArrayList<>();
        try (Scanner scanner = new Scanner(data)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                tasks.add(parseTaskFromStorage(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read tasks from file", e);
        }
        return tasks;
    }

    private Task parseTaskFromStorage(String storageLine) {
        String[] parts = storageLine.split(":");
        return new Task(parts[0], parts[1], Integer.parseInt(parts[2]), TaskStatus.values()[Integer.parseInt(parts[3])]);
    }
}