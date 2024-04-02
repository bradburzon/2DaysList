package com.bradburzon.a2dayslist.tasks;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.settings.SettingManager;

import java.util.List;

public class TaskViewHelper {
    public static void updateTaskView(Context context, List<Task> tasks, TaskManager taskManager, SettingManager settingManager) {
        LinearLayout scrollTaskView = ((TaskActivity) context).findViewById(R.id.scrollTaskView);
        scrollTaskView.removeAllViews();

        // Categories based on task status
        List<Task> todayList = TaskFilter.filterByStatus(tasks, TaskStatus.CREATED);
        List<Task> incompleteList =  TaskFilter.filterByStatus(tasks, TaskStatus.ARCHIVED);
        List<Task> trashList =  TaskFilter.filterByStatus(tasks, TaskStatus.DELETED);
        // Add sections for each category with tasks
        addSectionWithTasks(context, scrollTaskView, "TODAY'S LIST", todayList, Color.BLUE, taskManager, settingManager);
        addSectionWithTasks(context, scrollTaskView, "INCOMPLETE", incompleteList, Color.RED, taskManager, settingManager);
        addSectionWithTasks(context, scrollTaskView, "RECYCLE BIN", trashList, Color.BLACK, taskManager, settingManager);
    }

    private static void addSectionWithTasks(Context context, LinearLayout scrollTaskView, String sectionTitle, List<Task> tasks, int titleColor, TaskManager taskManager, SettingManager settingManager) {
        if ("TODAY'S LIST".equals(sectionTitle) && tasks.isEmpty()) {
            // Display a temporary message when no tasks are available
            TextView noTaskMessage = new TextView(context);
            noTaskMessage.setText(R.string.click_the_plus_button_to_add_a_to_do_item);
            noTaskMessage.setTextSize(16); // Set your desired text size
            noTaskMessage.setTextColor(Color.BLACK); // Set your desired text color
            int paddingInDp = (int) (16 * context.getResources().getDisplayMetrics().density);
            noTaskMessage.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp); // Adjust padding as needed
            noTaskMessage.setGravity(Gravity.CENTER_HORIZONTAL); // Center the text horizontally in its container
            scrollTaskView.addView(noTaskMessage);
        } else if (!tasks.isEmpty()) {
            scrollTaskView.addView(createSectionTitle(context, sectionTitle + " (" + tasks.size() + ")", titleColor));
            for (Task task : tasks) {
                addTaskToView(context, scrollTaskView, task, taskManager, settingManager);
            }
        }
    }

    private static TextView createSectionTitle(Context context, String title, int color) {
        TextView sectionTitle = new TextView(context);
        sectionTitle.setText(title);
        sectionTitle.setTextSize(26); // Slightly larger text size
        sectionTitle.setTextColor(color); // A more vibrant font color
        sectionTitle.setTypeface(null, Typeface.BOLD); // Make the text bold
        sectionTitle.setBackgroundColor(Color.parseColor("#AEFFFFFF")); // Light background color for contrast
        int paddingInPx = (int) (8 * context.getResources().getDisplayMetrics().density); // Increased padding for better visibility
        sectionTitle.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx);

        // Optionally, set margins for the title for better spacing from other elements
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int marginInPx = (int) (4 * context.getResources().getDisplayMetrics().density);
        layoutParams.setMargins(marginInPx, marginInPx, marginInPx, marginInPx);
        sectionTitle.setLayoutParams(layoutParams);

        return sectionTitle;
    }

    private static void addTaskToView(Context context, LinearLayout scrollTaskView, Task task, TaskManager taskManager, SettingManager settingManager) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.task_layout, scrollTaskView, false);

        CheckBox taskCheckBox = taskView.findViewById(R.id.taskCheckBox);
        TextView taskNameTextView = taskView.findViewById(R.id.taskNameTextView);
        Button renewButton = taskView.findViewById(R.id.renewButton);
        Button binButton = taskView.findViewById(R.id.binButton);
        Button deleteButton = taskView.findViewById(R.id.deleteButton);

        taskNameTextView.setText(task.getName());

        // Adjust visibility based on task status
        if (task.getTaskStatus() == TaskStatus.ARCHIVED || task.getTaskStatus() == TaskStatus.DELETED) {
            taskCheckBox.setVisibility(View.GONE); // Hide checkbox for archived and deleted tasks
        } else {
            taskCheckBox.setChecked(task.getTaskStatus() == TaskStatus.COMPLETED);
        }

        setupButtonListeners(task, taskManager, renewButton, binButton, deleteButton, context, settingManager);

        taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setTaskStatus(isChecked ? TaskStatus.COMPLETED : TaskStatus.CREATED);
            taskManager.update(task.getTaskId(), task);
            updateTaskView(context, taskManager.listTasks(settingManager.createSortStrategyType()), taskManager, settingManager);
        });

        scrollTaskView.addView(taskView);
    }

    private static void setupButtonListeners(Task task, TaskManager taskManager, Button renewButton, Button binButton, Button deleteButton, Context context, SettingManager settingManager) {
        // Reset button visibility
        renewButton.setVisibility(View.GONE);
        binButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);

        switch (task.getTaskStatus()) {
            case DELETED:
                renewButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                break;
            case ARCHIVED:
                renewButton.setVisibility(View.VISIBLE);
                break;
            default:
                binButton.setVisibility(View.VISIBLE);
                break;
        }

        renewButton.setOnClickListener(v -> {
            task.setTaskStatus(TaskStatus.CREATED);
            taskManager.update(task.getTaskId(), task);
            updateTaskView(context, taskManager.listTasks(settingManager.createSortStrategyType()), taskManager, settingManager);
        });

        binButton.setOnClickListener(v -> {
            task.setTaskStatus(TaskStatus.DELETED);
            taskManager.update(task.getTaskId(), task);
            updateTaskView(context, taskManager.listTasks(settingManager.createSortStrategyType()), taskManager, settingManager);
        });

        deleteButton.setOnClickListener(v -> {
            taskManager.delete(task.getTaskId());
            updateTaskView(context, taskManager.listTasks(settingManager.createSortStrategyType()), taskManager, settingManager);
        });
    }
}
