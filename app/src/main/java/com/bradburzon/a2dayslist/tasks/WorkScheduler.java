package com.bradburzon.a2dayslist.tasks;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WorkScheduler {

    public static void scheduleArchiving(Context context, WorkExecutionCallback callback) {
        long initialDelay = calculateInitialDelayToMidnight();

        Constraints constraints = new Constraints.Builder()
                .build();

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(ArchiveTaskWorker.class, 24, TimeUnit.HOURS) // Adjusted to the minimum allowed interval
                .setConstraints(constraints)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork("archive_tasks", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest);

        observeWork(context, workRequest.getId(), callback);
    }

    private static void observeWork(Context context, UUID workId, WorkExecutionCallback callback) {
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(workId)
                .observe((LifecycleOwner) context, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        if (callback != null) {
                            callback.onWorkExecuted();
                        }
                    }
                });
    }

        private static long calculateInitialDelayToMidnight() {
        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = calendar.getTimeInMillis();

        // Set the calendar instance to the next midnight
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long midnightTimeMillis = calendar.getTimeInMillis();
        return midnightTimeMillis - currentTimeMillis;
    }



public interface WorkExecutionCallback {
        void onWorkExecuted();
    }
}
