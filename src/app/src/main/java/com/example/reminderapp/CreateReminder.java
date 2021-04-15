package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminderapp.room.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateReminder extends AppCompatActivity implements ReminderFrequencyDialog.NoticeDialogListener {
    EditText title;
    TextView date;
    ImageView date_img;
    TextView time;
    ImageView time_img;
    EditText freq;
    EditText desc;
    RadioGroup tag;
    int frequencyChoice = 0;
    LinearLayout days;
    boolean[] checkDays = new boolean[7];

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);

        title = (EditText) findViewById(R.id.et_title);
        date = (TextView) findViewById(R.id.et_date);
        date_img = (ImageView) findViewById(R.id.et_date_img);
        time = (TextView) findViewById(R.id.et_time);
        time_img = (ImageView) findViewById(R.id.et_time_img);
        freq = (EditText) findViewById(R.id.et_freq);
        desc = (EditText) findViewById(R.id.et_desc);
        tag = (RadioGroup) findViewById(R.id.et_tag);
        days = (LinearLayout) findViewById(R.id.layout_days);

        days.setVisibility(View.GONE);
        tag.check(tag.getChildAt(0).getId());


        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };

        TimePickerDialog.OnTimeSetListener timePicker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateTime();
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateReminder.this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateReminder.this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateReminder.this, timePicker, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        time_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CreateReminder.this, timePicker, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });


    }

    public void confirmReminder(View view) {
        if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(time.getText())) {
            Toast.makeText(getApplicationContext(),"Missing inputs!",Toast.LENGTH_SHORT).show();
        } else {
            Intent replyIntent = new Intent();

            String titleReply = title.getText().toString();
            long dateTimeReply = myCalendar.getTime().getTime();
            String descReply = desc.getText().toString();
            int freqReply = frequencyChoice;

            int radioButtonID = tag.getCheckedRadioButtonId();
            View radioButton = tag.findViewById(radioButtonID);
            int tagReply =  tag.indexOfChild(radioButton);


            if(frequencyChoice == Reminder.CUSTOM) {
                for(int index = 0; index < days.getChildCount(); index++) {
                    checkDays[index] = ( (CheckBox) days.getChildAt(index) ).isChecked();
                }
                replyIntent.putExtra("days", checkDays);
            }

            replyIntent.putExtra("title", titleReply);
            replyIntent.putExtra("dateTime", dateTimeReply);
            replyIntent.putExtra("desc", descReply);
            replyIntent.putExtra("freq", freqReply);
            replyIntent.putExtra("tag", tagReply);

            setResult(RESULT_OK, replyIntent);

            finish();
        }
    }

    public void updateDate() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateTime() {
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        time.setText(sdf.format(myCalendar.getTime()));
    }

    public void showDialog(View view) {
        DialogFragment newFragment = ReminderFrequencyDialog.newInstance(frequencyChoice);
        newFragment.show(getSupportFragmentManager(), "missiles");
    }

    @Override
    public void onDialogPositiveClick(int checked) {
        frequencyChoice = checked;
        switch(frequencyChoice) {
            case 0:
                freq.setText("Never");
                days.setVisibility(View.GONE);
                break;
            case 1:
                freq.setText("Daily");
                days.setVisibility(View.GONE);
                break;
            case 2:
                freq.setText("Custom");
                days.setVisibility(View.VISIBLE);
                break;
            default:
                freq.setText("ERROR");
        }
    }
}