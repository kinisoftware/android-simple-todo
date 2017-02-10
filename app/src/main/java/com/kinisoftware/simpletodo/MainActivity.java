package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kinisoftware.simpletodo.repository.model.Task;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    private List<String> items;
    private ListView lvItems;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListeners();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newItem = etNewItem.getText().toString();
        itemsAdapter.add(newItem);
        saveItem(newItem);
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedItemBody = data.getExtras().getString("editedItemBody");
            int editedItemPos = data.getExtras().getInt("editedItemPos");
            items.set(editedItemPos, editedItemBody);
            itemsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Item edited", Toast.LENGTH_SHORT).show();
        }
    }

    private void readItems() {
        List<Task> tasks = SQLite.select().from(Task.class).queryList();
        items = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            items.add(task.getName());
        }
    }

    private void setupListViewListeners() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                deleteTask(pos);
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intentEditItemActivity = new Intent(MainActivity.this, EditItemActivity.class);
                Task task = getTask(pos);
                intentEditItemActivity.putExtra("taskPos", pos);
                intentEditItemActivity.putExtra("task", task);
                startActivityForResult(intentEditItemActivity, REQUEST_CODE);
            }
        });
    }

    private void saveItem(String newItem) {
        Task task = new Task();
        task.setName(newItem);
        task.save();
    }

    private void deleteTask(int pos) {
        Task task = getTask(pos);
        task.delete();
    }

    private Task getTask(int pos) {
        String item = items.get(pos);
        Condition condition = Condition.column(NameAlias.builder("name").build());
        condition.eq(item);
        return SQLite.select().from(Task.class).where(condition).querySingle();
    }
}
