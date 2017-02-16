package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.kinisoftware.simpletodo.repository.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditTaskActivity extends ActionBarCustomizeActivity {

    public static final String DUE_DATE_FORMAT = "dd/MM/yy";
    private EditText etEditTask;
    private int taskPostToBeEdited;
    private Task taskEdited;
    private EditText etDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        etEditTask = (EditText) findViewById(R.id.etEditTask);
        etDueDate = (EditText) findViewById(R.id.etDueDate);

        showTaskToBeEdited();
    }

    private void showTaskToBeEdited() {
        taskEdited = (Task) getIntent().getSerializableExtra("task");
        taskPostToBeEdited = getIntent().getIntExtra("taskPos", -1);
        etEditTask.setText(taskEdited.getName(), TextView.BufferType.EDITABLE);
        etEditTask.requestFocus();

        if (taskEdited.hasDueDate()) {
            etDueDate.setText(getDateFormatForDueDate().format(taskEdited.getDueDate()));
        }
    }

    private SimpleDateFormat getDateFormatForDueDate() {
        return new SimpleDateFormat(DUE_DATE_FORMAT);
    }

    @Override
    public void onSaveTask(MenuItem menuItem) {
        String editedTask = etEditTask.getText().toString();
        taskEdited.setName(editedTask);

        try {
            String updatedDueDate = etDueDate.getText().toString();
            taskEdited.setDueDate(getDateFormatForDueDate().parse(updatedDueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskEdited.update();
        Intent intent = new Intent();
        intent.putExtra("editedTask", taskEdited);
        intent.putExtra("editedTaskPos", taskPostToBeEdited);
        setResult(RESULT_OK, intent);
        finish();
    }
}
