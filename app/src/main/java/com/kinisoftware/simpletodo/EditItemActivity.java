package com.kinisoftware.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    private Button btnSaveEditedItem;
    private EditText etEditItem;
    private int itemPostToBeEdited;

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
        String itemBodyToBeEdited = getIntent().getStringExtra("itemBody");
        itemPostToBeEdited = getIntent().getIntExtra("itemPos", -1);
        etEditItem.setText(itemBodyToBeEdited, TextView.BufferType.EDITABLE);
        etEditItem.requestFocus();
    }

    private void setupButtonSaveEditedListeners() {
        btnSaveEditedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("editedItemBody", etEditItem.getText().toString());
                intent.putExtra("editedItemPos", itemPostToBeEdited);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
