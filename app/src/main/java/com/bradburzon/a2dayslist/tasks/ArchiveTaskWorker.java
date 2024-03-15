package com.bradburzon.a2dayslist.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

public class ArchiveTaskWorker extends Worker {
    public ArchiveTaskWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        TaskManager taskManager = TaskManagerImpl.createLocalTaskManager(new File(getApplicationContext().getFilesDir(), "tasks.txt").getAbsolutePath());
        MidnightTaskArchivingStrategy archivingStrategy = new MidnightTaskArchivingStrategy();
        TaskArchiver taskArchiver = new TaskArchiver(archivingStrategy);
        taskArchiver.archiveTasks(taskManager);

        return Result.success();
    }
}
