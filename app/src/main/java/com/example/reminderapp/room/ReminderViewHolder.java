package com.example.reminderapp.room;

import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reminderapp.CreateReminder;
import com.example.reminderapp.R;
import com.example.reminderapp.ReminderFrequencyDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class ReminderViewHolder extends RecyclerView.ViewHolder {
    private final TextView time, timeperiod, title, description;
    private final CardView cardView;
    private final View view;


    private ReminderViewHolder(View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.reminder_time);
        timeperiod = itemView.findViewById(R.id.reminder_timeperiod);
        title = itemView.findViewById(R.id.reminder_title);
        description = itemView.findViewById(R.id.reminder_description);
        cardView = itemView.findViewById(R.id.reminder_card);
        view = itemView.findViewById(R.id.color_strip);
    }

    public void bind(final Reminder reminder, ReminderAdapter.ReminderListener listener) {
        Date date = new Date(reminder.getDateTime());

        String hourFormat = "hh:mm";
        String amPmFormat = "a";
        SimpleDateFormat sdf = new SimpleDateFormat(hourFormat, Locale.US);
        String hour = sdf.format(date);
        sdf = new SimpleDateFormat(amPmFormat, Locale.US);
        String amPm = sdf.format(date);
        amPm.toLowerCase();

        time.setText(hour);
        timeperiod.setText(amPm);
        title.setText(reminder.getTitle());
        description.setText(reminder.getDesc());

        switch(reminder.getTag()) {
            case 0:
                view.setBackgroundColor(0xFF1661F2);
                break;
            case 1:
                view.setBackgroundColor(0xff33b5e5);
                break;
            case 2:
                view.setBackgroundColor(0xFFFDB22C);
                break;
            case 3:
                view.setBackgroundColor(0xFFF3431C);
                break;
            case 4:
                view.setBackgroundColor(0xFF2C2F56);
                break;
            default:
                view.setBackgroundColor(0xFFFFFFFF);
                break;
        }

        //cardView.setTag("hey");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onClickCard(reminder, v);
            }
        });

    }

    static ReminderViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_card, parent, false);
        return new ReminderViewHolder(view);
    }
}
