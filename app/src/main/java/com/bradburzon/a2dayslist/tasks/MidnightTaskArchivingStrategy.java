package com.bradburzon.a2dayslist.tasks;

import java.util.List;

public class MidnightTaskArchivingStrategy implements TaskArchivingStrategy {

    @Override
    public void archiveTasks(TaskManager taskManager) {
        List<Task> tasks = taskManager.listTasks();
        for (Task task : tasks) {
            TaskStatus newStatus;

            switch (task.getTaskStatus()) {
                case COMPLETED:
                    newStatus = TaskStatus.CREATED; // Completed tasks get reset to Created
                    break;
                case ARCHIVED:
                    newStatus = TaskStatus.DELETED; // Archived tasks get marked as Deleted
                    break;
                case CREATED:
                    newStatus = TaskStatus.ARCHIVED; // Created tasks get Archived
                    break;
                case DELETED:
                    continue; // Skip any tasks that are already Deleted
                default:
                    throw new IllegalStateException("Unexpected value: " + task.getTaskStatus());
            }

            Task updatedTask = new Task(task.getTaskId(), task.getName(), task.getIndex(), newStatus);
            taskManager.update(task.getTaskId(), updatedTask);
        }
    }
}
