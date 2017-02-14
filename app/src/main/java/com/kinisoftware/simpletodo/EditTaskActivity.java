package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kinisoftware.simpletodo.repository.model.Task;

public class EditTaskActivity extends AppCompatActivity {

    private Button btnSaveEditedTask;
    private EditText etEditTask;
    private int taskPostToBeEdited;
    private Task taskEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        btnSaveEditedTask = (Button) findViewById(R.id.btnSaveEditedTask);
        etEditTask = (EditText) findViewById(R.id.etEditTask);

        setupButtonSaveEditedListeners();
        showTaskToBeEdited();
    }

    private void showTaskToBeEdited() {
        taskEdited = (Task) getIntent().getSerializableExtra("task");
        taskPostToBeEdited = getIntent().getIntExtra("taskPos", -1);
        etEditTask.setText(taskEdited.getName(), TextView.BufferType.EDITABLE);
        etEditTask.requestFocus();
    }

    private void setupButtonSaveEditedListeners() {
        btnSaveEditedTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedTask = etEditTask.getText().toString();
                taskEdited.setName(editedTask);
                taskEdited.update();
                Intent intent = new Intent();
                intent.putExtra("editedTask", taskEdited);
                intent.putExtra("editedTaskPos", taskPostToBeEdited);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
