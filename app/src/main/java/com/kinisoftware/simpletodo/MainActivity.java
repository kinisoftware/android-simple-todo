package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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
        etNewItem.setText("");
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String editedItemBody = data.getExtras().getString("editedItemBody");
            int editedItemPos = data.getExtras().getInt("editedItemPos");
            items.set(editedItemPos, editedItemBody);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, "Item edited", Toast.LENGTH_SHORT).show();
        }
    }

    private void readItems() {
        File todoFile = getTodoFile();
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File todoFile = getTodoFile();
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private File getTodoFile() {
        File filesDir = getFilesDir();
        return new File(filesDir, "todo.txt");
    }

    private void setupListViewListeners() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long l) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intentEditItemActivity = new Intent(MainActivity.this, EditItemActivity.class);
                intentEditItemActivity.putExtra("itemPos", pos);
                intentEditItemActivity.putExtra("itemBody", items.get(pos));
                startActivityForResult(intentEditItemActivity, REQUEST_CODE);
            }
        });
    }
}
