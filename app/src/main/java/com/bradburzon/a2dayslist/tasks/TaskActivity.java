package com.bradburzon.a2dayslist.tasks;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.date.DateManager;
import com.bradburzon.a2dayslist.settings.SettingManager;
import com.bradburzon.a2dayslist.settings.SortStrategyType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private static final String TASKS_FILENAME = "tasks.txt";
    private static final String APP_PREFERENCES = "AppPreferences";
    private static final String SORT_STRATEGY_ORDINAL_KEY = "SortStrategyOrdinal";

    private TaskManager taskManager;
    private SettingManager settingManager;
    private FloatingActionButton floatingActionButton;
    private SortStrategyType sortStrategyType;
    private DateManager dateManager;
    private MidnightTaskArchivingStrategy midnightTaskArchivingStrategy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setupSortSpinner();
        setupFloatingActionButton();

        // Initialize DateManager with the appropriate file path
        dateManager = new DateManager(getFilesDir() + File.separator + "dateManager.txt");

        initializeManagers();
        runTaskIfNeeded();
    }

    public void runTaskIfNeeded() {
        Calendar lastActiveDate = dateManager.getLastActiveDate();
        Calendar today = Calendar.getInstance();
        lastActiveDate = dateManager.removeTimeComponents(lastActiveDate);
        today = dateManager.removeTimeComponents(today);

        if (!lastActiveDate.equals(today)) {
            midnightTaskArchivingStrategy.archiveTasks(taskManager);
            dateManager.setLastActiveDate(today); // Update last active date
            putListTaskToTaskView();
        }
    }

    private void initializeManagers() {
        File file = new File(getFilesDir(), TASKS_FILENAME);
        taskManager = TaskManagerImpl.createLocalTaskManager(file.getAbsolutePath());
        settingManager = SettingManager.createSettingsManager();
        settingManager.setSortStrategyType(sortStrategyType);
        midnightTaskArchivingStrategy = new MidnightTaskArchivingStrategy();
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
        runTaskIfNeeded();
        putListTaskToTaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        runTaskIfNeeded();
        saveSortStrategy();
    }

    private void saveSortStrategy() {
        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SortStrategyType currentSortStrategy = settingManager.getSortStrategyType();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_STRATEGY_ORDINAL_KEY, currentSortStrategy.ordinal());
        editor.apply();
    }
}