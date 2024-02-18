package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TaskManagerTest {
    @Test
    public void givenTaskIdWhenMarkAsCompleteIsCalledThenMarkTheTaskCompleteInStorage() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
        TaskStorage storage = new LocalTaskStorage(startingData);
        TaskManager taskManager = new TaskManager(storage);

        taskManager.markAsComplete("1");

        assertEquals(TaskStatus.COMPLETED, storage.getById("1").getTaskStatus());
    }
}