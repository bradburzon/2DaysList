package com.bradburzon.a2dayslist.tasks;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.settings.SettingManager;
import com.bradburzon.a2dayslist.settings.SortStrategyType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private TaskManager taskManager;
    private SettingManager settingManager;
    FloatingActionButton floatingActionButton;
    private int strategyTypeInt;
    private final static int TASK_FONT_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        // Initialization of TaskManager, SettingManager, and scheduling archiving
        String filename = "tasks.txt";
        File file = new File(getFilesDir(), filename); // Using internal storage
        this.taskManager = TaskManagerImpl.createLocalTaskManager(file.getAbsolutePath());
        this.settingManager = SettingManager.createSettingsManager();
        SetupAlarmManager.scheduleArchiving(this);

        // Initialize FloatingActionButton and its click listener
        floatingActionButton = findViewById(R.id.button_add);
        floatingActionButton.setOnClickListener(view -> {
            floatingActionButton.hide();
            addTask(); // Add a new task
            putListTaskToTaskView();
        });

        // Initialize Spinner for sort options
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_items); // Custom layout for spinner items
        sortSpinner.setAdapter(adapter);

        // Retrieve the saved sort strategy from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        // Retrieve the saved sort strategy as an int using a new key to avoid conflicts
        int sortStrategyOrdinal = sharedPreferences.getInt("SortStrategyOrdinal", SortStrategyType.BY_INDEX.ordinal());
        SortStrategyType sortStrategyType = SortStrategyType.values()[sortStrategyOrdinal];


        settingManager.setSortStrategyType(sortStrategyType);

        sortSpinner.setSelection(sortStrategyOrdinal, false);

        // Spinner item selection listener to handle sort strategy changes
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView) parentView.getChildAt(0)).setTextColor(Color.BLACK);

                SortStrategyType selectedSortStrategyType = SortStrategyType.BY_NAME; // Default
                switch (position) {
                    case 0: // Name
                        break;
                    case 1: // Index
                        selectedSortStrategyType = SortStrategyType.BY_STATUS;
                        break;
                    case 2: // Status
                        selectedSortStrategyType = SortStrategyType.BY_INDEX;
                        break;
                }
                strategyTypeInt = position;
                // Save the selected sort strategy to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("SortStrategyOrdinal", position); // Notice the updated key
                editor.apply();

                settingManager.setSortStrategyType(selectedSortStrategyType);
                putListTaskToTaskView(); // Refresh the task view based on new sort strategy
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        putListTaskToTaskView(); // Initial display of tasks based on current sort strategy
    }


    @SuppressLint("ClickableViewAccessibility")
    private void addTask() {
        LinearLayout layoutContainer = findViewById(R.id.bottom_bar);
        RelativeLayout rootLayout = findViewById(R.id.taskAppLayout); // Assuming this is your root layout's ID

        LinearLayout inputLayout = new LinearLayout(this);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inputLayout.setLayoutParams(layoutParams);
        inputLayout.setBackgroundColor(Color.WHITE);

        EditText taskInput = new EditText(this);
        taskInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        taskInput.setHint("Add task here");
        taskInput.setHintTextColor(Color.LTGRAY);
        taskInput.setTextColor(Color.BLACK);
        taskInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        taskInput.setFilters(new InputFilter[]{filter});
        taskInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        Button addButton = new Button(this);
        addButton.setText(R.string.add);
        addButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        addButton.setOnClickListener(v -> {
            String taskName = taskInput.getText().toString();
            if (!taskName.isEmpty()) {
                taskManager.addTask(taskName);
                putListTaskToTaskView();
                layoutContainer.removeView(inputLayout);
                floatingActionButton.show();
                taskInput.clearFocus();
            }
        });

        inputLayout.addView(taskInput);
        inputLayout.addView(addButton);
        layoutContainer.addView(inputLayout);

        taskInput.requestFocus();

        rootLayout.setOnTouchListener((v, event) -> {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof EditText) {
                InputMethodManager imm;
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getCurrentFocus().clearFocus();

                // Check if inputLayout is still a part of its parent
                if (inputLayout.getParent() != null) {
                    layoutContainer.removeView(inputLayout);
                    floatingActionButton.show();// Remove inputLayout
                }
            }
            return false;
        });
    }


    private TextView createSectionTitle(String title, int color) {
        TextView sectionTitle = new TextView(this);
        sectionTitle.setText(title);
        sectionTitle.setTextSize(24);
        sectionTitle.setTextColor(color);
        int paddingInPx = (int) (8 * getResources().getDisplayMetrics().density);
        sectionTitle.setPadding(paddingInPx, paddingInPx, paddingInPx, 0);
        return sectionTitle;
    }

    private void putListTaskToTaskView() {
        Comparator<Task> sortStrategyType = settingManager.getSortStrategyType();
        List<Task> tasks = taskManager.listTasks(sortStrategyType);
        LinearLayout scrollTaskView = findViewById(R.id.scrollTaskView);
        scrollTaskView.removeAllViews();

        // Calculate counts
        long completedListCount = tasks.stream()
                .filter(task -> task.getTaskStatus() == TaskStatus.COMPLETED)
                .count();
        long todayListCount = tasks.stream()
                .filter(task -> task.getTaskStatus() != TaskStatus.ARCHIVED && task.getTaskStatus() != TaskStatus.DELETED)
                .count();
        long incompleteCount = tasks.stream()
                .filter(task -> task.getTaskStatus() == TaskStatus.ARCHIVED)
                .count();
        long trashCount = tasks.stream()
                .filter(task -> task.getTaskStatus() == TaskStatus.DELETED)
                .count();

        // Today's List Section with count
        int todayFontColor = Color.BLACK;
        if (todayListCount > 0 && completedListCount == todayListCount) {
            todayFontColor = Color.BLUE;
        }
        scrollTaskView.addView(createSectionTitle("TODAY'S LIST (" + completedListCount + "/" + todayListCount + ")", todayFontColor));
        for (Task task : tasks) {
            if (task.getTaskStatus() != TaskStatus.ARCHIVED && task.getTaskStatus() != TaskStatus.DELETED) {
                addTaskToView(task, scrollTaskView, "BIN", TaskStatus.DELETED);
            }
        }
        // Incomplete Section with count
        int inCompleteFontColor = Color.BLACK;
        if (incompleteCount > 0) {
            inCompleteFontColor = Color.RED;
        }
        scrollTaskView.addView(createSectionTitle("INCOMPLETE (" + incompleteCount + ")", inCompleteFontColor));
        for (Task task : tasks) {
            if (task.getTaskStatus() == TaskStatus.ARCHIVED) {
                addTaskToView(task, scrollTaskView, "RENEW", TaskStatus.ARCHIVED);
            }
        }// 1dp to pixels

        // Trash Section with count
        scrollTaskView.addView(createSectionTitle("BIN (" + trashCount + ")", Color.BLACK));
        for (Task task : tasks) {
            if (task.getTaskStatus() == TaskStatus.DELETED) {
                addTrashTaskToView(task, scrollTaskView);
            }
        }
    }
    private void addTrashTaskToView(Task task, LinearLayout scrollTaskView) {
        LinearLayout taskLayout = new LinearLayout(this);
        taskLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        int marginInPx = (int) (8 * getResources().getDisplayMetrics().density);
        layoutParams.setMargins(marginInPx, marginInPx, marginInPx, 0);
        taskLayout.setLayoutParams(layoutParams);
        taskLayout.setBackgroundResource(R.drawable.task_background);
        int paddingInPx = (int) (8 * getResources().getDisplayMetrics().density);
        taskLayout.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        TextView taskTextView = new TextView(this);
        taskTextView.setText(task.getName());
        taskTextView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textViewParams.gravity = Gravity.CENTER_VERTICAL; // Ensure the text view is also centered vertically
        taskTextView.setLayoutParams(textViewParams);
        taskTextView.setPadding(paddingInPx * 3, 0, 0, 0);

        taskTextView.setTextSize(TASK_FONT_SIZE);
        taskLayout.addView(taskTextView);

        // Add RENEW button
        Button renewButton = new Button(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Button_OutlinedButton);
        renewButton.setText(R.string.renew);
        renewButton.setOnClickListener(v -> {
            task.setTaskStatus(TaskStatus.CREATED); // Assuming UNCHANGED marks it as active again
            taskManager.update(task.getTaskId(), task);
            putListTaskToTaskView(); // Refresh the task view
        });
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.gravity = Gravity.CENTER_VERTICAL; // Align button center vertically
        renewButton.setLayoutParams(buttonParams);
        taskLayout.addView(renewButton);

        // Add DELETE button
        Button deleteButton = new Button(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Button_OutlinedButton);
        deleteButton.setText(R.string.delete);
        deleteButton.setOnClickListener(v -> {
            deleteTask(task.getTaskId());
            putListTaskToTaskView();// Assuming this method removes the task
        });
        deleteButton.setLayoutParams(buttonParams);
        taskLayout.addView(deleteButton);

        scrollTaskView.addView(taskLayout);
    }


    private void addTaskToView(Task task, LinearLayout scrollTaskView, String buttonLabel, TaskStatus newStatus) {
        LinearLayout taskLayout = new LinearLayout(this);
        taskLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        int marginInPx = (int) (8 * getResources().getDisplayMetrics().density);
        layoutParams.setMargins(marginInPx, marginInPx, marginInPx, 0);
        taskLayout.setLayoutParams(layoutParams);

        taskLayout.setBackgroundResource(R.drawable.task_background);
        int paddingInPx = (int) (8 * getResources().getDisplayMetrics().density);
        taskLayout.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        if (newStatus != TaskStatus.ARCHIVED && newStatus != null) {
            CheckBox taskCheckBox = new CheckBox(this);
            LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            taskCheckBox.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            checkBoxParams.gravity = Gravity.CENTER_VERTICAL;
            taskCheckBox.setLayoutParams(checkBoxParams);
            taskCheckBox.setChecked(task.getTaskStatus() == TaskStatus.COMPLETED);
            taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    task.setTaskStatus(TaskStatus.COMPLETED);
                } else {
                    // Assuming you want to set the task status back to UNCHANGED when unchecked
                    task.setTaskStatus(TaskStatus.CREATED);
                }
                taskManager.update(task.getTaskId(), task);
                putListTaskToTaskView(); // Optionally, refresh the task view to reflect changes
            });
            taskLayout.addView(taskCheckBox);
        }

        TextView taskTextView = new TextView(this);
        taskTextView.setText(task.getName());
        taskTextView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        textViewParams.gravity = Gravity.CENTER_VERTICAL;
        taskTextView.setLayoutParams(textViewParams);

        taskTextView.setPadding(16, 0, 0, 0);
        taskTextView.setTextSize(TASK_FONT_SIZE);
        taskLayout.addView(taskTextView);

        Button actionButton = new Button(this, null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Button_OutlinedButton);
        actionButton.setText(buttonLabel);
        actionButton.setOnClickListener(v -> {
            if ("DELETE".equals(buttonLabel)) {
                String taskId = (String) v.getTag();
                deleteTask(taskId);
            } else {
                task.setTaskStatus(TaskStatus.CREATED);
                taskManager.update(task.getTaskId(), task);
            }
            putListTaskToTaskView();
        });
        actionButton.setTag(task.getTaskId());
        LinearLayout.LayoutParams actionButtonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        actionButtonParams.gravity = Gravity.CENTER_VERTICAL;
        actionButton.setLayoutParams(actionButtonParams);
        taskLayout.addView(actionButton);

        scrollTaskView.addView(taskLayout);
    }



    private void deleteTask(String taskId) {
        Task task = taskManager.delete(taskId);
        System.out.println("DELETED: " + task);
    }

    private final BroadcastReceiver archivingCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_ARCHIVING_COMPLETED.equals(intent.getAction())) {
                System.out.println("THIS");
                putListTaskToTaskView();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(Constants.ACTION_ARCHIVING_COMPLETED);
        registerReceiver(archivingCompleteReceiver, filter);
        putListTaskToTaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(archivingCompleteReceiver);
        // Save the current sort strategy to SharedPreferences when the activity is paused
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        // Save the ordinal of the current sort strategy
        editor.putInt("SortStrategyOrdinal", strategyTypeInt); // Save the ordinal
        editor.apply();
        putListTaskToTaskView();
    }
}