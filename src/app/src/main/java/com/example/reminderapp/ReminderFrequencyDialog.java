package com.example.reminderapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ReminderFrequencyDialog extends DialogFragment {
    int checked;

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(int checked);
    }

    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    public static ReminderFrequencyDialog newInstance(int checked) {
        ReminderFrequencyDialog fragment = new ReminderFrequencyDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("checked", checked);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] array = {"Never", "Daily", "Custom"};
        checked = getArguments().getInt("checked");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DefaultAlertDialogTheme);

        builder.setTitle("Repeat")
                .setSingleChoiceItems(array, checked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        checked = id;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogPositiveClick(checked);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

}
