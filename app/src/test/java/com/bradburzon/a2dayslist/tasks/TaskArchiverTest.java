package com.bradburzon.a2dayslist.tasks;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

import com.bradburzon.a2dayslist.tasks.archiver.TaskArchiver;
import com.bradburzon.a2dayslist.tasks.archiver.TaskArchivingStrategy;
import com.bradburzon.a2dayslist.tasks.manager.TaskManager;

public class TaskArchiverTest {

    @Test
    public void archiveTasks_delegatesToStrategy() {
        TaskArchivingStrategy mockStrategy = Mockito.mock(TaskArchivingStrategy.class);
        TaskManager mockTaskManager = Mockito.mock(TaskManager.class);
        TaskArchiver taskArchiver = new TaskArchiver(mockStrategy);

        taskArchiver.archiveTasks(mockTaskManager);

        verify(mockStrategy).archiveTasks(mockTaskManager);
    }
}