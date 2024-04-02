package com.bradburzon.a2dayslist.tasks;

import static org.junit.Assert.assertEquals;

import com.bradburzon.a2dayslist.tasks.storage.MapTaskStorage;
import com.bradburzon.a2dayslist.tasks.storage.TaskStorage;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTaskStorageTest {
    private Map<String, Task> startingData;

    @Before
    public void setUp() {
        startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
    }

    @Test
    public void givenNonEmptyStartingDataWhenListIsCalledThenReturnStartingData() {
        TaskStorage storage = new MapTaskStorage(startingData);

        List<Task> list = storage.list();

        assertEquals(new ArrayList<>(startingData.values()), list);
    }

    @Test
    public void givenNewTaskWhenAddIsCalledThenStoreTheTask() {
        TaskStorage storage = new MapTaskStorage(new HashMap<>());

        storage.add(new Task("1", "name", 1, TaskStatus.CREATED));
        List<Task> list = storage.list();

        assertEquals(new ArrayList<>(startingData.values()), list);
    }

    @Test
    public void givenNonEmptyStartingDataWhenDeleteIsCalledThenRemoveTaskFromStorage() {
        TaskStorage storage = new MapTaskStorage(startingData);

        Task actual = storage.delete("1");
        List<Task> list = storage.list();

        assertEquals(0, list.size());
        assertEquals( new Task("1", "name", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenNonEmptyStartingDataWhenGetByIdIsCalledThenReturnMatchingTask() {
        TaskStorage storage = new MapTaskStorage(startingData);

        Task actual = storage.getById("1");

        assertEquals( new Task("1", "name", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenNonEmptyStartingDataWhenUpdateIsCalledThenUpdateMatchingTask() {
        TaskStorage storage = new MapTaskStorage(startingData);

        storage.update("1", new Task("1", "updatedName", 1, TaskStatus.CREATED));
        Task actual = storage.getById("1");

        assertEquals( new Task("1", "updatedName", 1, TaskStatus.CREATED), actual);
    }
}