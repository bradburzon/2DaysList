package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskSorterTest {

    private static final Task TASK_1 = new Task("1", "Name", 1, TaskStatus.CREATED) ;
    private static final Task TASK_2 = new Task("2", "Name", 2, TaskStatus.CREATED);
    private static final Task TASK_3 = new Task("3", "Zed", 3, TaskStatus.CREATED);

    @Test
    public void givenTwoTaskWithSameNameWhenCompareIsCalledShouldReturn0() {
        TaskSorter.NameSort nameSorter = new TaskSorter.NameSort();

        int actual = nameSorter.compare(TASK_1, TASK_2);

        assertEquals(0, actual);
    }

    @Test
    public void givenTwoTaskWithDifferentNameWhenCompareIsCalledShouldReturnNonZero() {
        TaskSorter.NameSort nameSorter = new TaskSorter.NameSort();

        int actual = nameSorter.compare(TASK_1, TASK_3);

        assertNotEquals(0, actual);
    }
}