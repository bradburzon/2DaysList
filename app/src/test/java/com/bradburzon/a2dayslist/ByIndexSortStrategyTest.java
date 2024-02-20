package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ByIndexSortStrategyTest {
    private static final Task TASK_1 = new Task("1", "Task 1", 1, TaskStatus.CREATED);
    private static final Task TASK_2 = new Task("2", "Task 2", 2, TaskStatus.MODIFIED);
    private static final Task TASK_3 = new Task("3", "Task 3", 3, TaskStatus.COMPLETED);
    private ByIndexSortStrategy byIndexSortStrategy;
    @Before
    public void setUp() {
        byIndexSortStrategy =new ByIndexSortStrategy();
    }

    @Test
    public void givenOneTaskWhenCompareToIsCalledThenReturnZero() {
        int actual = byIndexSortStrategy.compare(TASK_1, TASK_1);

        assertEquals(0, actual);
    }

    @Test
    public void givenTwoTaskWithAscendingNameWhenCompareToIsCalledThenReturnNegative() {
        int actual = byIndexSortStrategy.compare(TASK_1, TASK_2);

        assertTrue(actual < 0);
    }

    @Test
    public void givenTwoTaskWithDescendingNameWhenCompareToIsCalledThenReturnPositive() {
        int actual = byIndexSortStrategy.compare(TASK_3, TASK_2);

        assertTrue(actual > 0);
    }

    @Test
    public void givenThreeTaskWithAscendingNameWhenCompareToIsCalledThenReturnNegative() {
        int x = byIndexSortStrategy.compare(TASK_1, TASK_2);
        int y =  byIndexSortStrategy.compare(TASK_2, TASK_3);
        int z = byIndexSortStrategy.compare(TASK_1, TASK_3);

        //transitive property if (X > Y) and (Y > Z), then (X > Z)
        assertTrue(x < 0);
        assertTrue(y < 0);
        assertTrue(z < 0);
    }
    @Test
    public void givenThreeTaskWithDescendingNameWhenCompareToIsCalledThenReturnPositive() {
        int x = byIndexSortStrategy.compare(TASK_3, TASK_2);
        int y =  byIndexSortStrategy.compare(TASK_2, TASK_1);
        int z = byIndexSortStrategy.compare(TASK_3, TASK_1);

        assertTrue(x > 0);
        assertTrue(y > 0);
        assertTrue(z > 0);
    }
}