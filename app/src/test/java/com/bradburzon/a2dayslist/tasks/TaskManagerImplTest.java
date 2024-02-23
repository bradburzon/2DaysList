package com.bradburzon.a2dayslist.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.bradburzon.a2dayslist.settings.SortStrategyFactory;
import com.bradburzon.a2dayslist.settings.SortStrategyType;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagerImplTest {

    private TaskStorage storage;
    private TaskManager taskManager;
    private static final Task TASK_1= new Task("1", "Task 1", 1, TaskStatus.CREATED);
    private static final Task TASK_2= new Task("2", "Task 2", 2, TaskStatus.CREATED);

    @Before
    public void setUp() {
        storage = new MapTaskStorage(new HashMap<>());
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
        startingData.put("1", TASK_1);
        storage = new MapTaskStorage(startingData);
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
        startingData.put("1", TASK_1);
        storage = new MapTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        Task actual = taskManager.getById("1");

        assertEquals(TASK_1, actual);
    }

    @Test
    public void givenTaskIdAndUpdatedTaskWhenUpdateIsCalledThenUpdateTheMatchingTask() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", TASK_1);
        storage = new MapTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        taskManager.update("1",  new Task("1", "New Task 1", 1, TaskStatus.CREATED));
        Task actual = taskManager.getById("1");

        assertEquals(new Task("1", "New Task 1", 1, TaskStatus.CREATED), actual);
    }

    @Test
    public void givenTaskIdWhenDeleteIsCalledThenDeleteTheMatchingTaskFromList() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", TASK_1);
        storage = new MapTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        Task actual =  taskManager.delete("1");

        assertEquals(0, taskManager.listTasks().size());
        assertEquals(TASK_1, actual);
    }

    @Test
    public void givenTaskComparatorWhenListTasksIsCalledThenReturnSortedList() {
        Map<String, Task> startingData = new HashMap<>();
        startingData.put("1", TASK_2);
        startingData.put("2", TASK_1);
        storage = new MapTaskStorage(startingData);
        taskManager = new TaskManagerImpl(storage);

        List<Task> list = taskManager.listTasks(SortStrategyFactory.create(SortStrategyType.BY_NAME));

        assertEquals(2, taskManager.listTasks().size());
        assertEquals(TASK_1, list.get(0));
        assertEquals( TASK_2, list.get(1));
    }
}