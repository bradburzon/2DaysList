package com.bradburzon.a2dayslist.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class LocalTaskStorageTest {

    private static final Task TASK_1 = new Task("1", "Task 1", 1, TaskStatus.CREATED);
    private static final Task TASK_2 = new Task("2", "Task 2", 2, TaskStatus.CREATED);
    private static File tempFile;
    private static LocalTaskStorage localTaskStorage;

    @BeforeClass
    public static void setUpClass() throws IOException {
        tempFile = Files.createTempFile("tasks", ".txt").toFile();
        localTaskStorage = new LocalTaskStorage(tempFile.getAbsolutePath());
    }

    @Before
    public void setUp() throws IOException {
        new FileWriter(tempFile.getAbsolutePath(), false).close();
    }

    @Test
    public void givenEmptyListOfTaskWhenListsIsCalledThenReturnEmptyList() {
        List<Task> actual = localTaskStorage.list();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void givenTaskWhenAddTaskIsCalledThenStoreTask() {
        localTaskStorage.add(TASK_1);

        List<Task> actual = localTaskStorage.list();
        assertEquals(1, actual.size());

        Task task = actual.get(0);
        assertEquals(TASK_1.getTaskId(), task.getTaskId());
        assertEquals(TASK_1.getName(), task.getName());
        assertEquals(TASK_1.getIndex(), task.getIndex());
        assertEquals(TASK_1.getTaskStatus(), task.getTaskStatus());
    }

    @Test
    public void givenMultipleTasksWhenAddTasksIsCalledThenStoreAllTasks() {
        Task task3 = new Task("3", "Task 3", 3, TaskStatus.CREATED);
        Task task4 = new Task("4", "Task 4", 4, TaskStatus.CREATED);

        localTaskStorage.add(TASK_1);
        localTaskStorage.add(TASK_2);
        localTaskStorage.add(task3);
        localTaskStorage.add(task4);

        List<Task> actual = localTaskStorage.list();
        assertEquals(4, actual.size());
        assertTrue(actual.contains(TASK_1));
        assertTrue(actual.contains(TASK_2));
        assertTrue(actual.contains(task3));
        assertTrue(actual.contains(task4));
    }

    @Test
    public void givenTaskIdWhenGetByIdIsCalledThenReturnMatchingTask() {
        localTaskStorage.add(TASK_2);

        Task actual = localTaskStorage.getById(TASK_2.getTaskId());
        assertEquals(TASK_2, actual);
    }

    @Test
    public void givenTaskIdWhenDeleteTaskIsCalledThenDeleteAndReturnTask() {
        localTaskStorage.add(TASK_1);

        Task deletedTask = localTaskStorage.delete(TASK_1.getTaskId());
        assertEquals(TASK_1, deletedTask);

        List<Task> actual = localTaskStorage.list();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void givenTaskIdAndTaskWhenUpdateIsCalledThenUpdateMatchingTask() {
        localTaskStorage.add(TASK_1);

        Task newTask = new Task(TASK_1.getTaskId(), "Task 3", 3, TaskStatus.CREATED);
        localTaskStorage.update(TASK_1.getTaskId(), newTask);

        Task actual = localTaskStorage.getById(newTask.getTaskId());
        assertEquals(newTask, actual);
    }

    @AfterClass
    public static void tearDownClass() {
        tempFile.delete();
    }
}