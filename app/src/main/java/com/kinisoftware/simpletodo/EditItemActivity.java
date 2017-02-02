package com.kinisoftware.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String item = getIntent().getStringExtra("itemBody");

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(item, TextView.BufferType.EDITABLE);
        etEditItem.requestFocus();

        //TODO: Back to main activity
    }
}
