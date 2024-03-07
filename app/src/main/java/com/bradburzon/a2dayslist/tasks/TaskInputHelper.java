package com.bradburzon.a2dayslist.tasks;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bradburzon.a2dayslist.R;
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

    public void showTaskInput() {
        LinearLayout layoutContainer = ((TaskActivity)context).findViewById(R.id.bottom_bar);
        RelativeLayout rootLayout = ((TaskActivity)context).findViewById(R.id.taskAppLayout);

        // Create input layout programmatically as in your addTask method
        LinearLayout inputLayout = new LinearLayout(context);
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inputLayout.setLayoutParams(layoutParams);
        inputLayout.setBackgroundColor(Color.WHITE);

        EditText taskInput = new EditText(context);
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

        Button addButton = new Button(context);
        addButton.setText(R.string.add);
        addButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
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

        // Similar functionality to remove the input layout when touching outside
        rootLayout.setOnTouchListener((v, event) -> {
            hideInputLayout(inputLayout, layoutContainer);
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
