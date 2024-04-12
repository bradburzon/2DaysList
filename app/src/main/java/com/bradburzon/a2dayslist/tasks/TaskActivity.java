package com.bradburzon.a2dayslist.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.date.DateManager;
import com.bradburzon.a2dayslist.settings.SettingManager;
import com.bradburzon.a2dayslist.settings.SortStrategyType;
import com.bradburzon.a2dayslist.tasks.archiver.MidnightTaskArchivingStrategy;
import com.bradburzon.a2dayslist.tasks.manager.TaskManager;
import com.bradburzon.a2dayslist.tasks.manager.TaskManagerImpl;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity {

    private static final String TASKS_FILENAME = "tasks.txt";
    private static final String DATE_FILENAME = "dateManager.txt";
    private static final String APP_PREFERENCES = "AppPreferences";
    private static final String SORT_STRATEGY_ORDINAL_KEY = "SortStrategyOrdinal";

    private TaskManager taskManager;
    private SettingManager settingManager;
    private SortStrategyType sortStrategyType;
    private DateManager dateManager;
    private MidnightTaskArchivingStrategy midnightTaskArchivingStrategy;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        setupSortSpinner();
        setupTaskInputLayout();
        initializeManagers();
        runTaskIfNeeded();
    }

    private void initializeManagers() {
        File file = new File(getFilesDir(), TASKS_FILENAME);
        taskManager = TaskManagerImpl.createLocalTaskManager(file.getAbsolutePath());
        settingManager = SettingManager.createSettingsManager();
        settingManager.setSortStrategyType(sortStrategyType);
        midnightTaskArchivingStrategy = new MidnightTaskArchivingStrategy();
        dateManager = new DateManager(getFilesDir() + File.separator + DATE_FILENAME);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
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

    private void setupTaskInputLayout() {
        EditText taskInput = findViewById(R.id.edittext_task_input);
        taskInput.setFilters(new InputFilter[]{this::filterNonAlphanumericCharacters});

        Button addButton = findViewById(R.id.addButton);
        addButton.setText(R.string.add);

        addButton.setOnClickListener(v -> {
            String taskName = taskInput.getText().toString();
            if (!taskName.isEmpty()) {
                taskManager.addTask(taskName);
                taskInput.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                getCurrentFocus().clearFocus();
                putListTaskToTaskView();
            }
        });
    }

    private CharSequence filterNonAlphanumericCharacters(CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                return "";
            }
        }
        return null;
    }
    private void setupSortSpinner() {
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);


        int sortStrategyOrdinal = getSortStrategyOrdinal(getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE));
        sortSpinner.setSelection(sortStrategyOrdinal, false);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortStrategyType = SortStrategyType.values()[position];
                settingManager.setSortStrategyType(sortStrategyType);
                saveSortStrategy(position, sharedPreferences );
                putListTaskToTaskView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void putListTaskToTaskView() {
        int sortStrategyOrdinal = getSortStrategyOrdinal(sharedPreferences);
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
        int sortStrategy = getSortStrategyOrdinal(sharedPreferences);
        saveSortStrategy(sortStrategy, sharedPreferences);
    }

    private void saveSortStrategy(int sortStrategy, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_STRATEGY_ORDINAL_KEY, sortStrategy);
        editor.apply();
    }

    private int getSortStrategyOrdinal(SharedPreferences sharedPreferences) {
        return sharedPreferences.getInt(SORT_STRATEGY_ORDINAL_KEY, 0);
    }
}