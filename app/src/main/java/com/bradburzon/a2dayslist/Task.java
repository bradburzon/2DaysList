package com.bradburzon.a2dayslist;

import java.util.Date;
import java.util.Objects;

public class Task {

    private String taskId;
    private String name;
    private Date expiresIn;
    private Date remindDate;
    private int index;
    private TaskStatus taskStatus;

    public Task() {
    }

    public Task(String taskId, String name, Date expiresIn, Date remindDate, int index, TaskStatus taskStatus) {
        this.taskId = taskId;
        this.name = name;
        this.expiresIn = expiresIn;
        this.remindDate = remindDate;
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

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Date expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
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
        return index == task.index && Objects.equals(taskId, task.taskId) && Objects.equals(name, task.name) && Objects.equals(expiresIn, task.expiresIn) && Objects.equals(remindDate, task.remindDate) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, name, expiresIn, remindDate, index, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", name='" + name + '\'' +
                ", expiresIn=" + expiresIn +
                ", remindDate=" + remindDate +
                ", index=" + index +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
