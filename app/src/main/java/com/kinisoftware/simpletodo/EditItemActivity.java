package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kinisoftware.simpletodo.repository.model.Task;

public class EditItemActivity extends AppCompatActivity {

    private Button btnSaveEditedItem;
    private EditText etEditItem;
    private int itemPostToBeEdited;
    private Task taskEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        btnSaveEditedItem = (Button) findViewById(R.id.btnSaveEditedItem);
        etEditItem = (EditText) findViewById(R.id.etEditItem);

        setupButtonSaveEditedListeners();
        showItemToBeEdited();
    }

    private void showItemToBeEdited() {
        taskEdited = (Task) getIntent().getSerializableExtra("task");
        itemPostToBeEdited = getIntent().getIntExtra("taskPos", -1);
        etEditItem.setText(taskEdited.getName(), TextView.BufferType.EDITABLE);
        etEditItem.requestFocus();
    }

    private void setupButtonSaveEditedListeners() {
        btnSaveEditedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedTask = etEditItem.getText().toString();
                taskEdited.setName(editedTask);
                taskEdited.update();
                Intent intent = new Intent();
                intent.putExtra("editedItemBody", editedTask);
                intent.putExtra("editedItemPos", itemPostToBeEdited);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
