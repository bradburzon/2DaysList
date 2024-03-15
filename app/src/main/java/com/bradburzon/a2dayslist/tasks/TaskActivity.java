package com.bradburzon.a2dayslist.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskActivity extends AppCompatActivity implements WorkScheduler.WorkExecutionCallback {

    private static final String TASKS_FILENAME = "tasks.txt";
    private static final String APP_PREFERENCES = "AppPreferences";
    private static final String SORT_STRATEGY_ORDINAL_KEY = "SortStrategyOrdinal";

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
        // Schedule archiving tasks using WorkManager
        WorkScheduler.scheduleArchiving(this, this);
        runTaskIfNeeded(this);
    }

    public void updateLastExecutionDate(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long epochDay;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            epochDay = LocalDate.now().toEpochDay();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            epochDay = TimeUnit.MILLISECONDS.toDays(calendar.getTimeInMillis());
        }
        editor.putLong("LastExecutionDate", epochDay);
        editor.apply();
        WorkScheduler.scheduleArchiving(this, this);
    }

    public boolean shouldRunTask(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        long lastExecutionDay = sharedPreferences.getLong("LastExecutionDate", -1);
        long today;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now().toEpochDay();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            today = TimeUnit.MILLISECONDS.toDays(calendar.getTimeInMillis());
        }
        return lastExecutionDay < today;
    }

    public void runTaskIfNeeded(Context context) {
        if (shouldRunTask(context)) {
            updateLastExecutionDate(context);
            WorkScheduler.scheduleArchiving(this, this);
            putListTaskToTaskView();
        }
    }

    private void initializeManagers() {
        File file = new File(getFilesDir(), TASKS_FILENAME);
        taskManager = TaskManagerImpl.createLocalTaskManager(file.getAbsolutePath());
        settingManager = SettingManager.createSettingsManager();
        settingManager.setSortStrategyType(sortStrategyType);
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
        int sortStrategyOrdinal = sharedPreferences.getInt(SORT_STRATEGY_ORDINAL_KEY, 0);
        sortSpinner.setSelection(sortStrategyOrdinal, false);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortStrategyType = SortStrategyType.values()[position];
                settingManager.setSortStrategyType(sortStrategyType);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(SORT_STRATEGY_ORDINAL_KEY, position);
                editor.apply();

                putListTaskToTaskView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void putListTaskToTaskView() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        int sortStrategyOrdinal = sharedPreferences.getInt(SORT_STRATEGY_ORDINAL_KEY, 0);
        settingManager.setSortStrategyType(SortStrategyType.values()[sortStrategyOrdinal]);
        List<Task> tasks = taskManager.listTasks(settingManager.createSortStrategyType());

        TaskViewHelper.updateTaskView(this, tasks, taskManager, settingManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        putListTaskToTaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSortStrategy();
    }

    private void saveSortStrategy() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SortStrategyType currentSortStrategy = settingManager.getSortStrategyType();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_STRATEGY_ORDINAL_KEY, currentSortStrategy.ordinal());
        editor.apply();
    }

    @Override
    public void onWorkExecuted() {
        runTaskIfNeeded(this);
        putListTaskToTaskView();
    }
}