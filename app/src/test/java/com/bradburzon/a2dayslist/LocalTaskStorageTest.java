package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class LocalTaskStorageTest {
    private Map<String, Task> startingData;

    @Before
    public void setUp() throws Exception {
        startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
    }

    @Test
    public void givenNonEmptyStartingDataWhenListIsCalledThenReturnStartingData() {
        TaskStorage storage = new LocalTaskStorage(startingData);

        List<Task> list = storage.list();

        assertEquals(new ArrayList<>(startingData.values()), list);
    }

    @Test
    public void givenNewTaskWhenAddIsCalledThenStoreTheTask() {
        TaskStorage storage = new LocalTaskStorage(new HashMap<String, Task>());

        storage.add(new Task("1", "name", 1, TaskStatus.CREATED));
        List<Task> list = storage.list();

        assertEquals(new ArrayList<>(startingData.values()), list);
    }

    @Test
    public void givenNonEmptyStartingDataWhenDeleteIsCalledThenRemoveTaskFromStorage() {
        TaskStorage storage = new LocalTaskStorage(startingData);

        storage.delete("1");
        List<Task> list = storage.list();

        assertEquals(new ArrayList<>(), list);
    }

    @Test
    public void givenNonEmptyStartingDataWhenGetByIdIsCalledThenReturnMatchingTask() {
        TaskStorage storage = new LocalTaskStorage(startingData);

        Task actual = storage.getById("1");;

        assertEquals( new Task("1", "name", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenNonEmptyStartingDataWhenUpdateIsCalledThenUpdateMatchingTask() {
        TaskStorage storage = new LocalTaskStorage(startingData);

        storage.update("1", new Task("1", "updatedName", 1, TaskStatus.CREATED));
        Task actual = storage.getById("1");

        assertEquals( new Task("1", "updatedName", 1, TaskStatus.CREATED), actual);
    }
}