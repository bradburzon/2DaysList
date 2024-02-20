package com.bradburzon.a2dayslist;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Task {

    private String taskId;
    private String name;
    private int index;
    private TaskStatus taskStatus;

    public Task() {
    }

    public Task(String taskId, String name, int index, TaskStatus taskStatus) {
        this.taskId = taskId;
        this.name = name;
        this.index = index;
        this.taskStatus = taskStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus status) {
        taskStatus = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return index == task.index && Objects.equals(taskId, task.taskId) && Objects.equals(name, task.name) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, name, index, taskStatus);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", index=" + index +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
