package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class LocalTaskStorageTest {

    @Test
    public void testList() {
        Map<String, Task> startingData = new HashMap<>();
        Date date = new Date();
        startingData.put("1", new Task("1", "name", date, date, 1, TaskStatus.CREATED));
        TaskStorage storage = new LocalTaskStorage(startingData);

        List<Task> list = storage.list();

        assertEquals(new ArrayList<>(startingData.values()), list);
    }
}