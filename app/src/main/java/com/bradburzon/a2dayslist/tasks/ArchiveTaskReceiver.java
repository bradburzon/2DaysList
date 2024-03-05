package com.bradburzon.a2dayslist.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;

public class ArchiveTaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TaskManager taskManager = TaskManagerImpl.createLocalTaskManager(new File(context.getFilesDir(), "tasks.txt").getAbsolutePath());

        MidnightTaskArchivingStrategy archivingStrategy = new MidnightTaskArchivingStrategy();
        TaskArchiver taskArchiver = new TaskArchiver(archivingStrategy);

        // Perform the task archiving
        taskArchiver.archiveTasks(taskManager);

        // Reschedule the archiving for the next day, if necessary
        SetupAlarmManager.scheduleArchiving(context);
        Intent completedIntent = new Intent(Constants.ACTION_ARCHIVING_COMPLETED);
        context.sendBroadcast(completedIntent);
    }
}