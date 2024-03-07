package com.bradburzon.a2dayslist.tasks;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TaskCategorizerTest {

    private TaskCategorizer taskCategorizer;

    @Before
    public void setUp() {
        taskCategorizer = new TaskCategorizer();
    }

    @Test
    public void categorizeTasks_CorrectlyCategorizesTasks() {
        List<Task> tasks = Arrays.asList(
                new Task("1", "Task 1", 1, TaskStatus.CREATED),
                new Task("2", "Task 2", 2, TaskStatus.COMPLETED),
                new Task("3", "Task 3", 3, TaskStatus.ARCHIVED),
                new Task("4", "Task 4", 4, TaskStatus.DELETED)
        );

        Map<TaskStatus, List<Task>> categorizedTasks = taskCategorizer.categorizeTasks(tasks);

        assertEquals("Incorrect number of CREATED tasks", 1, categorizedTasks.get(TaskStatus.CREATED).size());
        assertEquals("Incorrect number of COMPLETED tasks", 1, categorizedTasks.get(TaskStatus.COMPLETED).size());
        assertEquals("Incorrect number of ARCHIVED tasks", 1, categorizedTasks.get(TaskStatus.ARCHIVED).size());
        assertEquals("Incorrect number of DELETED tasks", 1, categorizedTasks.get(TaskStatus.DELETED).size());
    }
}
