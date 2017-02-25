package com.kinisoftware.simpletodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kinisoftware.simpletodo.repository.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditTaskDialogFragment extends DialogFragment {

    public static final String DUE_DATE_FORMAT = "dd/MM/yy";
    private EditText etEditTask;
    private int taskPostToBeEdited;
    private Task taskEdited;
    private EditText etDueDate;

    public interface EditTaskDialogListener {
        void onFinishEditDialog(Task task, int editedTaskPos);
    }

    public EditTaskDialogFragment() {
    }

    public static EditTaskDialogFragment build(String title) {
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_task, null);
        etEditTask = (EditText) view.findViewById(R.id.etEditTask);
        etDueDate = (EditText) view.findViewById(R.id.etDueDate);
        loadTaskInfo();
        return buildTheDialog(view).create();
    }

    @NonNull
    private AlertDialog.Builder buildTheDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateTask();
                EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
                listener.onFinishEditDialog(taskEdited, taskPostToBeEdited);
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    private void updateTask() {
        String editedTask = etEditTask.getText().toString();
        taskEdited.setName(editedTask);

        try {
            String updatedDueDate = etDueDate.getText().toString();
            taskEdited.setDueDate(getDateFormatForDueDate().parse(updatedDueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        taskEdited.update();
    }

    private void loadTaskInfo() {
        taskEdited = (Task) getArguments().getSerializable("task");
        taskPostToBeEdited = getArguments().getInt("taskPos", -1);
        etEditTask.setText(taskEdited.getName(), TextView.BufferType.EDITABLE);
        etEditTask.requestFocus();

        if (taskEdited.hasDueDate()) {
            etDueDate.setText(getDateFormatForDueDate().format(taskEdited.getDueDate()));
        }
    }

    private SimpleDateFormat getDateFormatForDueDate() {
        return new SimpleDateFormat(DUE_DATE_FORMAT);
    }
}
