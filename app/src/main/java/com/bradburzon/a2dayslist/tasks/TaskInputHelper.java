package com.bradburzon.a2dayslist.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bradburzon.a2dayslist.R;
import com.bradburzon.a2dayslist.tasks.manager.TaskManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskInputHelper {
    private final Context context;
    private final TaskManager taskManager;
    private final Runnable onTaskAdded;
    private final FloatingActionButton floatingActionButton;

    public TaskInputHelper(Context context, TaskManager taskManager, Runnable onTaskAdded, FloatingActionButton floatingActionButton) {
        this.context = context;
        this.taskManager = taskManager;
        this.onTaskAdded = onTaskAdded;
        this.floatingActionButton = floatingActionButton;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showTaskInput() {
        LinearLayout layoutContainer = ((TaskActivity)context).findViewById(R.id.bottom_bar);



        LinearLayout inputLayout = new LinearLayout(context);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inputLayout.setLayoutParams(layoutParams);
        inputLayout.setBackgroundColor(Color.WHITE);

        EditText taskInput = new EditText(context);
        LinearLayout.LayoutParams taskInputLayoutParams = new LinearLayout.LayoutParams(
                0,
                (int) (55 * context.getResources().getDisplayMetrics().density), 1f);
        taskInput.setLayoutParams(taskInputLayoutParams);
        taskInput.setHint("Add task here");
        taskInput.setHintTextColor(Color.BLACK);
        taskInput.setTextColor(Color.BLACK);
        taskInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        taskInput.setMinHeight((int) (50 * context.getResources().getDisplayMetrics().density));

        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        taskInput.setFilters(new InputFilter[]{filter});

        Button addButton = new Button(context);
        addButton.setText(R.string.add);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) (48 * context.getResources().getDisplayMetrics().density));
        addButton.setLayoutParams(buttonLayoutParams);
        addButton.setOnClickListener(v -> {
            String taskName = taskInput.getText().toString();
            if (!taskName.isEmpty()) {
                taskManager.addTask(taskName);
                onTaskAdded.run();
                layoutContainer.removeView(inputLayout);
                floatingActionButton.show();
            }
        });

        inputLayout.addView(taskInput);
        inputLayout.addView(addButton);
        layoutContainer.addView(inputLayout);
        taskInput.requestFocus();
        LinearLayout rootLayout = ((TaskActivity)context).findViewById(R.id.scrollTaskView);
        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Get the location of the touch event

                    hideInputLayout(inputLayout, layoutContainer);
                    v.performClick(); // For accessibility
                    return true;
                }

            return false;
        });

    }

    private void hideInputLayout(LinearLayout inputLayout, LinearLayout layoutContainer) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            getCurrentFocus().clearFocus();
        }
        if (inputLayout.getParent() != null) {
            layoutContainer.removeView(inputLayout);
            floatingActionButton.show();
        }
    }

    private View getCurrentFocus() {
        return ((TaskActivity)context).getCurrentFocus();
    }
}
