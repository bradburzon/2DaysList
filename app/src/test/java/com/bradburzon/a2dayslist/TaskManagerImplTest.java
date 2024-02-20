package com.bradburzon.a2dayslist;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagerImplTest {

    private TaskStorage storage;
    private TaskManager taskManager;

    @Before
    public void setUp() {
        storage = new LocalTaskStorage(new HashMap<>());
        taskManager = new TaskManagerImpl(storage);
    }

    @Test
    public void givenNoStorageWhenCreateTaskManagerIsCalledThenReturnTaskManager() {
        taskManager = TaskManagerImpl.createTaskManager();

        assertNotNull(taskManager);
        assertEquals(TaskManagerImpl.class, taskManager.getClass());
    }

    @Test
    public void givenTaskIdWhenMarkAsCompleteIsCalledThenMarkTheTaskCompleteInStorage() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
        storage = new LocalTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

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

    @Test
    public void givenListOfTaskWhenListTaskIsCalledThenReturnListOfTask() {
        taskManager.addTask("Task 0");
        taskManager.addTask("Task 1");

        assertEquals(2, taskManager.listTasks().size());
    }

    @Test
    public void givenTaskIdWhenGetByIdIsCalledThenReturnMatchingTask() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
        storage = new LocalTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        Task actual = taskManager.getById("1");

        assertEquals(new Task("1", "name", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenTaskIdAndUpdatedTaskWhenUpdateIsCalledThenUpdateTheMatchingTask() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
        storage = new LocalTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        taskManager.update("1",  new Task("1", "new Name", 1, TaskStatus.CREATED));
        Task actual = taskManager.getById("1");

        assertEquals(new Task("1", "new Name", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenTaskIdWhenDeleteIsCalledThenDeleteTheMatchingTaskFromList() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "name", 1, TaskStatus.CREATED));
        storage = new LocalTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        Task actual =  taskManager.delete("1");

        assertEquals(0, taskManager.listTasks().size());
        assertEquals(new Task("1", "name", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenComparatorWhenSortIsCalledThenSortTheListOfTask() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", new Task("1", "Zask 1", 1, TaskStatus.CREATED));
        startingData.put("3", new Task("3", "Aask 1", 2, TaskStatus.CREATED));
        startingData.put("2", new Task("2", "Biking", 3, TaskStatus.CREATED));
        storage = new LocalTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);
        List<Task> tasks = taskManager.listTasks();

        taskManager.sort(new TaskSorter.NameSort());
        List<Task> actual = taskManager.listTasks();

        assertNotEquals(tasks, actual);
    }
}