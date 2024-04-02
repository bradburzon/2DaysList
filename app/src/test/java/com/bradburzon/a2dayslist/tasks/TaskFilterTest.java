package com.bradburzon.a2dayslist.tasks;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TaskFilterTest {

    Task CREATED = new Task("0", "task", 0, TaskStatus.CREATED);
    Task MODIFIED = new Task("0", "task", 0, TaskStatus.MODIFIED);
    Task UNCHANGED = new Task("0", "task", 0, TaskStatus.UNCHANGED);
    Task DELETED = new Task("0", "task", 0, TaskStatus.DELETED);
    Task ARCHIVED = new Task("0", "task", 0, TaskStatus.ARCHIVED);
    Task COMPLETED = new Task("0", "task", 0, TaskStatus.COMPLETED);

    @Test
    public void filterByStatus_ListWithOneOfEachStatus_ReturnOne(){
        List<Task> list = Arrays.asList(CREATED, MODIFIED, UNCHANGED, DELETED, ARCHIVED, COMPLETED);

        List<Task> actual = TaskFilter.filterByStatus(list, TaskStatus.CREATED);

        assertEquals(1, actual.size());
        assertEquals(TaskStatus.CREATED, actual.get(0).getTaskStatus());
    }

    @Test
    public void filterByStatus_ListFilledWithOtherStatus_ReturnZero(){
        List<Task> list = Arrays.asList( MODIFIED, UNCHANGED, DELETED, ARCHIVED, COMPLETED);

        List<Task> actual = TaskFilter.filterByStatus(list, TaskStatus.CREATED);

        assertEquals(0, actual.size());
    }

    @Test
    public void filterByStatus_ListFilledWithMatchingStatusOnly_ReturnLength(){
        List<Task> list = Arrays.asList( CREATED, CREATED, CREATED, CREATED, CREATED, CREATED);

        List<Task> actual = TaskFilter.filterByStatus(list, TaskStatus.CREATED);

        assertEquals(6, actual.size());
    }
}