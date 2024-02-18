package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class TaskTest {

    private Task task;

    @Before
    public void setUp() throws Exception {
        task = new Task();
    }

    @Test
    public void constructorTest() {
        Date date = new Date();
        task = new Task("id", "name", date, date, 1, TaskStatus.CREATED);

        assertEquals("id", task.getTaskId());
        assertEquals("name", task.getName());
        assertEquals(date, task.getExpiresIn());
        assertEquals(date, task.getRemindDate());
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
    public void setAndGetExpiresIn() {
        Date date = new Date();
        task.setExpiresIn(date);
        Date actual = task.getExpiresIn();

        assertEquals(date, actual);
    }

    @Test
    public void setAndGetRemindDate() {
        Date date = new Date();
        task.setRemindDate(date);
        Date actual = task.getRemindDate();
        
        assertEquals(date, actual);
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
        Date date = new Date();
        task = new Task("id", "name", date, date, 1, TaskStatus.CREATED);
        Task otherTask = new Task("id", "name", date, date, 1, TaskStatus.CREATED);

        assertEquals(task, otherTask);
    }

    @Test
    public void equalsReturnFalse() {
        Date date = new Date();
        task = new Task("id", "name", date, date, 1, TaskStatus.CREATED);
        Task otherTask = new Task("id", "name", date, date, 2, TaskStatus.CREATED);

        assertNotEquals(task, otherTask);
    }

    @Test
    public void equalHashCode() {
        Date date = new Date();
        task = new Task("id", "name", date, date, 1, TaskStatus.CREATED);
        Task otherTask = new Task("id", "name", date, date, 1, TaskStatus.CREATED);

        assertEquals(task.hashCode(), otherTask.hashCode());
    }

    @Test
    public void toStringTest() {
        Date date = new Date(2015);
        task = new Task("id", "name", date, date, 1, TaskStatus.CREATED);

        assertEquals("Task{taskId='id', name='name', expiresIn=Wed Dec 31 16:00:02 PST 1969, remindDate=Wed Dec 31 16:00:02 PST 1969, index=1, taskStatus=CREATED}", task.toString());
    }
}