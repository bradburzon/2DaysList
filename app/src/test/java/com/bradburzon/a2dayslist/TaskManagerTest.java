package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class TaskManagerTest {

    private TaskStorage storage;
    private TaskManager taskManager;

    @Before
    public void setUp() throws Exception {
        storage = new LocalTaskStorage(new HashMap<String, Task>());
        taskManager = new TaskManager(storage);
    }

    @Test
    public void givenTaskIdWhenMarkAsCompleteIsCalledThenMarkTheTaskCompleteInStorage() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));

        taskManager.markAsComplete("1");

        assertEquals(TaskStatus.COMPLETED, storage.getById("1").getTaskStatus());
    }

    @Test
    public void givenTaskNameWhenAddTaskIsCalledThenAddTaskToStorage() {
        taskManager.addTask("Task");
        Task task = storage.list().get(0);

        assertEquals(1, storage.list().size());
        assertNotNull(task.getTaskId());
        assertEquals(0, task.getIndex());
        assertEquals("Task", task.getName());
        assertEquals(TaskStatus.CREATED, task.getTaskStatus());
    }

    @Test
    public void givenTwoTaskNamesWhenAddTaskIsCalledTwiceThenAddTasksToStorage() {
        taskManager.addTask("Task 0");
        taskManager.addTask("Task 1");
        Task task0 = storage.list().get(0);
        Task task1 = storage.list().get(1);

        assertEquals(2, storage.list().size());
        assertNotEquals(task0.getTaskId(), task1.getTaskId());
        assertNotEquals(task0.getIndex(), task1.getIndex());
        assertEquals(1, Math.abs(task0.getIndex() - task1.getIndex()));
        assertNotEquals(task0.getName(), task1.getName());
        assertEquals(TaskStatus.CREATED, task0.getTaskStatus());
        assertEquals(TaskStatus.CREATED, task1.getTaskStatus());
    }
}