package com.bradburzon.a2dayslist.tasks;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.settings.SettingManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Comparator;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private TaskManager taskManager;
    private SettingManager settingManager;
    FloatingActionButton floatingActionButton;
    private Comparator<Task> sortStrategyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        String filename = "tasks.txt";
        File file = new File(getFilesDir(), filename); // Using internal storage
        this.taskManager = TaskManagerImpl.createLocalTaskManager(file.getAbsolutePath());
        this.settingManager = SettingManager.createSettingsManager();

        sortStrategyType = settingManager.getSortStrategyType();
        floatingActionButton = findViewById(R.id.button_add);
        floatingActionButton.setOnClickListener(view -> {
            floatingActionButton.hide();
            addTask(); // Add a new task
            putListTaskToTaskView();
        });

        putListTaskToTaskView();
    }

    private void addTask() {
        LinearLayout layoutContainer = findViewById(R.id.bottom_bar);

        LinearLayout inputLayout = new LinearLayout(this);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inputLayout.setLayoutParams(layoutParams);

        EditText taskInput = new EditText(this);
        taskInput.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        taskInput.setHint("Add task here");

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
            }
        });

        inputLayout.addView(taskInput);
        inputLayout.addView(addButton);
        layoutContainer.addView(inputLayout);

        taskInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(taskInput, InputMethodManager.SHOW_IMPLICIT);
    }

    private void putListTaskToTaskView() {
        sortStrategyType = settingManager.getSortStrategyType();
        List<Task> tasks = taskManager.listTasks(sortStrategyType);
        LinearLayout scrollTaskView = findViewById(R.id.scrollTaskView);
        scrollTaskView.removeAllViews();

        for (Task task : tasks) {
            LinearLayout taskLayout = new LinearLayout(this);
            taskLayout.setOrientation(LinearLayout.HORIZONTAL);
            taskLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            int paddingInPx = (int) (10 * getResources().getDisplayMetrics().density + 0.5f);
            taskLayout.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

            CheckBox taskCheckBox = new CheckBox(this);
            task.getTaskStatus().ordinal();
            taskCheckBox.setChecked(task.getTaskStatus() == TaskStatus.COMPLETED);
            taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    task.setTaskStatus(TaskStatus.COMPLETED);
                } else {
                    task.setTaskStatus(TaskStatus.UNCHANGED);
                }

                System.out.println(task);
            });

            TextView taskTextView = new TextView(this);
            taskTextView.setText(task.getName());
            taskTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // The weight is 1
            taskTextView.setPadding(16, 0, 0, 0);

            Button deleteButton = new Button(this);
            deleteButton.setText(R.string.delete);
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setTag(task.getTaskId());
            deleteButton.setOnClickListener(v -> {
                String taskId = (String) v.getTag();
                deleteTask(taskId);
                putListTaskToTaskView();
            });

            taskLayout.addView(taskCheckBox);
            taskLayout.addView(taskTextView);
            taskLayout.addView(deleteButton);
            scrollTaskView.addView(taskLayout);
        }
    }

    private Task deleteTask(String taskId) {
        Task task = taskManager.delete(taskId);
        System.out.println("DELETED: " + task );
        return task;
    }
}