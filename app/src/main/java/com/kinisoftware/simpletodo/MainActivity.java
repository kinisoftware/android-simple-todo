package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kinisoftware.simpletodo.repository.model.Task;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_TASK = 0;
    public static final int REQUEST_CODE_EDIT_TASK = 1;
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

    public void onAddTask(View v) {
        Intent intentAddTaskActivity = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(intentAddTaskActivity, REQUEST_CODE_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_NEW_TASK) {
            Task newTask = (Task) data.getExtras().getSerializable("newTask");
            taskAdapter.add(newTask);
            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_TASK) {
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
                startActivityForResult(intentEditTaskActivity, REQUEST_CODE_EDIT_TASK);
            }
        });
    }

    private void deleteTask(int pos) {
        Task task = taskAdapter.getItem(pos);
        task.delete();
        taskAdapter.remove(task);
    }
}
