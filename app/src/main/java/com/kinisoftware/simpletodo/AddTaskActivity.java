package com.kinisoftware.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.kinisoftware.simpletodo.repository.model.Task;

import java.util.Calendar;

/**
 * TODO: Add feedback when select a due date
 */
public class AddTaskActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {

    private EditText etNewTask;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        task = new Task();
        etNewTask = (EditText) findViewById(R.id.etNewTask);
    }

    public void onSaveNewTask(View v) {
        String taskName = etNewTask.getText().toString();
        task.setName(taskName);
        task.save();
        Intent intent = new Intent();
        intent.putExtra("newTask", task);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        task.setDueDate(calendar.getTime());
    }

    // TODO: Extract to a class
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener listener =
                    (DatePickerDialog.OnDateSetListener) getActivity();

            return new DatePickerDialog(getActivity(), listener, year, month, day);
        }
    }

}
