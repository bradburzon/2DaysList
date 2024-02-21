package com.bradburzon.a2dayslist.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {

    private Task task;

    @Before
    public void setUp() {
        task = new Task();
    }

    @Test
    public void constructorTest() {
        task = new Task("id", "name", 1, TaskStatus.CREATED);

        assertEquals("id", task.getTaskId());
        assertEquals("name", task.getName());
        assertEquals(1, task.getIndex());
        assertEquals(TaskStatus.CREATED, task.getTaskStatus());
    }

    @Test
    public void setAndGetTaskId() {
        task.setTaskId("1");
        String actual = task.getTaskId();

        assertEquals("1", actual);
    }

    @Test
    public void setAndGetName() {
        task.setName("Name");
        String actual = task.getName();

        assertEquals("Name", actual);
    }

    @Test
    public void setAndGetIndex() {
        task.setIndex(1);
        int actual = task.getIndex();

        assertEquals(1, actual);
    }

    @Test
    public void setAndGetTaskStatus() {
        task.setTaskStatus(TaskStatus.DELETED);
        TaskStatus actual = task.getTaskStatus();

        assertEquals(TaskStatus.DELETED, actual);
    }

    @Test
    public void equalsReturnTrue() {
        task = new Task("id", "name", 1, TaskStatus.CREATED);
        Task otherTask = new Task("id", "name", 1, TaskStatus.CREATED);

        assertEquals(task, otherTask);
    }

    @Test
    public void equalsReturnFalse() {
        task = new Task("id", "name", 1, TaskStatus.CREATED);
        Task otherTask = new Task("id", "name", 2, TaskStatus.CREATED);

        assertNotEquals(task, otherTask);
    }

    @Test
    public void equalHashCode() {
        task = new Task("id", "name", 1, TaskStatus.CREATED);
        Task otherTask = new Task("id", "name", 1, TaskStatus.CREATED);

        assertEquals(task.hashCode(), otherTask.hashCode());
    }

    @Test
    public void toStringTest() {
        task = new Task("id", "name", 1, TaskStatus.CREATED);

        assertEquals("Task{taskId='id', name='name', index=1, taskStatus=CREATED}", task.toString());
    }
}