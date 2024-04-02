package com.bradburzon.a2dayslist.tasks.archiver;

import com.bradburzon.a2dayslist.tasks.manager.TaskManager;

public interface TaskArchivingStrategy {
    void archiveTasks(TaskManager taskManager);
}

