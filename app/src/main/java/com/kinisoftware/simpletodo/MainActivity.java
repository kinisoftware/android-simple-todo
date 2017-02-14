package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kinisoftware.simpletodo.repository.model.Task;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    private ArrayList<Task> tasks;
    private ListView lvTasks;
    private TaskAdapter taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadTasks();
        lvTasks = (ListView) findViewById(R.id.lvTasks);
        taskAdapter = new TaskAdapter(this, tasks);
        lvTasks.setAdapter(taskAdapter);

        setupListViewListeners();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewTask);
        String newItem = etNewItem.getText().toString();
        saveTask(newItem);
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Task editedTask = (Task) data.getExtras().getSerializable("editedTask");
            int editedTaskPos = data.getExtras().getInt("editedTaskPos");
            /**
             * TODO: It has to be a better way to do this
             */
            Task task = taskAdapter.getItem(editedTaskPos);
            task.setName(editedTask.getName());

            taskAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Task edited", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTasks() {
        tasks = (ArrayList<Task>) SQLite.select().from(Task.class).queryList();
    }

    private void setupListViewListeners() {
        lvTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                deleteTask(pos);
                return true;
            }
        });
        lvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intentEditTaskActivity = new Intent(MainActivity.this, EditTaskActivity.class);
                Task task = taskAdapter.getItem(pos);
                intentEditTaskActivity.putExtra("taskPos", pos);
                intentEditTaskActivity.putExtra("task", task);
                startActivityForResult(intentEditTaskActivity, REQUEST_CODE);
            }
        });
    }

    private void saveTask(String newItem) {
        Task task = new Task();
        task.setName(newItem);
        task.save();
        taskAdapter.add(task);
    }

    private void deleteTask(int pos) {
        Task task = taskAdapter.getItem(pos);
        task.delete();
        taskAdapter.remove(task);
    }
}
