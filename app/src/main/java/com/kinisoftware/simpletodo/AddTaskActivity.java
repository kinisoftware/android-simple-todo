package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.kinisoftware.simpletodo.repository.model.Task;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etNewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etNewTask = (EditText) findViewById(R.id.etNewTask);
    }

    public void onSaveNewTask(View v) {
        String taskName = etNewTask.getText().toString();
        Task task = new Task();
        task.setName(taskName);
        task.save();
        Intent intent = new Intent();
        intent.putExtra("newTask", task);
        setResult(RESULT_OK, intent);
        finish();
    }
}
