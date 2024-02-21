package com.bradburzon.a2dayslist.tasks;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.settings.SettingManager;

public class TaskActivity extends AppCompatActivity {

    private final TaskManager taskManager;
    private final SettingManager settingManager;

    public TaskActivity() {
        this(TaskManagerImpl.createTaskManager(), SettingManager.createSettingsManager());
    }

    //dependency injection
    public TaskActivity(TaskManager taskManager, SettingManager settingManager) {
        this.taskManager = taskManager;
        this.settingManager = settingManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }
}