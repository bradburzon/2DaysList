package com.bradburzon.a2dayslist.tasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.settings.SettingManager;
import com.bradburzon.a2dayslist.settings.SortStrategyType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private static final String TASKS_FILENAME = "tasks.txt";
    private static final String APP_PREFERENCES = "AppPreferences";
    private static final String SORT_STRATEGY_ORDINAL_KEY = "SortStrategyOrdinal";
    private static final int TASK_FONT_SIZE = 20;

    private TaskManager taskManager;
    private SettingManager settingManager;
    private FloatingActionButton floatingActionButton;
    private SortStrategyType sortStrategyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setupSortSpinner();
        setupFloatingActionButton();
        initializeManagers();
    }

    private void initializeManagers() {
        File file = new File(getFilesDir(), TASKS_FILENAME);
        taskManager = TaskManagerImpl.createLocalTaskManager(file.getAbsolutePath());
        settingManager = SettingManager.createSettingsManager();
        settingManager.setSortStrategyType(sortStrategyType);
        SetupAlarmManager.scheduleArchiving(this);
    }

    private void setupFloatingActionButton() {
        floatingActionButton = findViewById(R.id.button_add);
        floatingActionButton.setOnClickListener(view -> {
            floatingActionButton.hide();
            new TaskInputHelper(this, taskManager, this::putListTaskToTaskView, floatingActionButton).showTaskInput();
            putListTaskToTaskView();
        });
    }

    private void setupSortSpinner() {
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int sortStrategyOrdinal = sharedPreferences.getInt(SORT_STRATEGY_ORDINAL_KEY, 0); // Default to 0 if not found
        sortSpinner.setSelection(sortStrategyOrdinal, false);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortStrategyType = SortStrategyType.values()[position];
                settingManager.setSortStrategyType(sortStrategyType); // Assuming this method accepts SortStrategyType

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SORT_STRATEGY_ORDINAL_KEY, position);
                editor.apply();

                putListTaskToTaskView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No operation
            }
        });
    }

    void putListTaskToTaskView() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int sortStrategyOrdinal = sharedPreferences.getInt(SORT_STRATEGY_ORDINAL_KEY, 0);
        settingManager.setSortStrategyType(SortStrategyType.values()[sortStrategyOrdinal]); // Adjusted to use a factory method
        List<Task> tasks = taskManager.listTasks(settingManager.createSortStrategyType());

        TaskViewHelper.updateTaskView(this, tasks, taskManager, settingManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(archivingCompleteReceiver, new IntentFilter(Constants.ACTION_ARCHIVING_COMPLETED));
        putListTaskToTaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(archivingCompleteReceiver);
        saveSortStrategy();
    }

    private void saveSortStrategy() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SortStrategyType currentSortStrategy = settingManager.getSortStrategyType(); // Assuming you have a way to get the current strategy
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_STRATEGY_ORDINAL_KEY, currentSortStrategy.ordinal());
        editor.apply();
    }

    private final BroadcastReceiver archivingCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_ARCHIVING_COMPLETED.equals(intent.getAction())) {
                Log.d("ARCHIVING", "Successfull");
                putListTaskToTaskView();
            }
        }
    };
}