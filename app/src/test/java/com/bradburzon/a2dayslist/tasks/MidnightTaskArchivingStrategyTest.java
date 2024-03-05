package com.bradburzon.a2dayslist.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MidnightTaskArchivingStrategyTest {

    @Mock
    private TaskManager taskManager;

    private MidnightTaskArchivingStrategy strategy;

    @Before
    public void setUp() {
        strategy = new MidnightTaskArchivingStrategy();
    }

    @Test
    public void testArchiveTasks_CompletedTasks() {
        Task completedTask = new Task("1", "Complete Task", 0, TaskStatus.COMPLETED);
        when(taskManager.listTasks()).thenReturn(Collections.singletonList(completedTask));

        strategy.archiveTasks(taskManager);

        verify(taskManager).update(eq("1"), argThat(task -> task.getTaskStatus() == TaskStatus.CREATED));
    }

    @Test
    public void testArchiveTasks_ArchivedTasks() {
        Task archivedTask = new Task("2", "Archived Task", 1, TaskStatus.ARCHIVED);
        when(taskManager.listTasks()).thenReturn(Collections.singletonList(archivedTask));

        strategy.archiveTasks(taskManager);

        verify(taskManager).update(eq("2"), argThat(task -> task.getTaskStatus() == TaskStatus.DELETED));
    }

    @Test
    public void testArchiveTasks_CreatedTasks() {
        Task createdTask = new Task("3", "Created Task", 2, TaskStatus.CREATED);
        when(taskManager.listTasks()).thenReturn(Collections.singletonList(createdTask));

        strategy.archiveTasks(taskManager);

        verify(taskManager).update(eq("3"), argThat(task -> task.getTaskStatus() == TaskStatus.ARCHIVED));
    }

    @Test
    public void testArchiveTasks_DeletedTasks() {
        Task deletedTask = new Task("4", "Deleted Task", 3, TaskStatus.DELETED);
        when(taskManager.listTasks()).thenReturn(Collections.singletonList(deletedTask));

        strategy.archiveTasks(taskManager);

        verify(taskManager, never()).update(eq("4"), any(Task.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testArchiveTasks_UnexpectedStatus() {
        Task unexpectedTask = new Task("5", "Unexpected Task", 4, TaskStatus.MODIFIED);
        when(taskManager.listTasks()).thenReturn(Collections.singletonList(unexpectedTask));

        strategy.archiveTasks(taskManager);
    }
}
