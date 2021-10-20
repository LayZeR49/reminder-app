package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.reminderapp.room.Reminder;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewReminder extends AppCompatActivity implements ReminderFrequencyDialog.NoticeDialogListener{
    TextView title, freq, desc, title2, freq2, desc2;
    TextView date, time, date2, time2;
    ImageView date_img2, time_img2;
    LinearLayout days, days2;
    RadioGroup tags, tags2;
    int frequencyChoice = 0;
    final Calendar myCalendar = Calendar.getInstance();

    int uid = 0;
    boolean editMode = false;
    boolean edited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);

        setup();
    }

    public void setup() {
        title = (TextView) findViewById(R.id.view_title);
        freq = (TextView) findViewById(R.id.view_freq);
        desc = (TextView) findViewById(R.id.view_desc);
        date = (TextView) findViewById(R.id.view_date);
        time = (TextView) findViewById(R.id.view_time);
        days = (LinearLayout) findViewById(R.id.view_days);
        tags = (RadioGroup) findViewById(R.id.view_tags);

        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", 0);
        title.setText(intent.getStringExtra("title"));
        desc.setText(intent.getStringExtra("desc"));
        Date dateType = new Date(intent.getLongExtra("dateTime", 0));

        String dateFormat = "dd MMM yyyy";
        String timeFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        date.setText(sdf.format(dateType));
        sdf = new SimpleDateFormat(timeFormat, Locale.US);
        time.setText(sdf.format(dateType));

        frequencyChoice = intent.getIntExtra("freq", 0);
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
                CheckBox checkbox;
                for(int index = 0; index < days.getChildCount(); index++) {
                    checkbox = (CheckBox) days.getChildAt(index);

                    checkbox.setChecked(intent.getBooleanArrayExtra("days")[index]);
                }
                break;
            default:
                freq.setText("ERROR");
        }

        tags.check(tags.getChildAt(intent.getIntExtra("tag", 0)).getId());
    }

    public void deleteReminder(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete reminder")
                .setMessage("Are you sure you want to delete this reminder?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent replyIntent = new Intent();
                        replyIntent.putExtra("uid", uid);

                        setResult(MainActivity.DELETE_REMINDER_RESULT_CODE, replyIntent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    public void editReminder(View view) {
        editMode = true;
        setContentView(R.layout.activity_create_reminder);
        title2 = (EditText) findViewById(R.id.et_title);
        date2 = (TextView) findViewById(R.id.et_date);
        date_img2 = (ImageView) findViewById(R.id.et_date_img);
        time2 = (TextView) findViewById(R.id.et_time);
        time_img2 = (ImageView) findViewById(R.id.et_time_img);
        freq2 = (EditText) findViewById(R.id.et_freq);
        desc2 = (EditText) findViewById(R.id.et_desc);
        tags2 = (RadioGroup) findViewById(R.id.et_tag);
        days2 = (LinearLayout) findViewById(R.id.layout_days);
        TextView layoutTitle = (TextView) findViewById(R.id.layout_title);
        Button buttonConfirm = (Button) findViewById(R.id.button_confirm);
        ImageView imageLeft = (ImageView) findViewById(R.id.image_left);

        imageLeft.setBackgroundResource(R.drawable.ic_baseline_arrow_back_24);
        layoutTitle.setText("Edit");
        buttonConfirm.setText("Save");

        Intent intent = getIntent();
        title2.setText(intent.getStringExtra("title"));
        desc2.setText(intent.getStringExtra("desc"));

        Date dateType = new Date(intent.getLongExtra("dateTime", 0));
        myCalendar.setTime(dateType);

        String dateFormat = "dd MMM yyyy";
        String timeFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        date2.setText(sdf.format(dateType));
        sdf = new SimpleDateFormat(timeFormat, Locale.US);
        time2.setText(sdf.format(dateType));

        switch(intent.getIntExtra("freq", 0)) {
            case 0:
                freq2.setText("Never");
                days2.setVisibility(View.GONE);
                break;
            case 1:
                freq2.setText("Daily");
                days2.setVisibility(View.GONE);
                break;
            case 2:
                freq2.setText("Custom");
                days2.setVisibility(View.VISIBLE);
                CheckBox checkbox;
                for(int index = 0; index < days2.getChildCount(); index++) {
                    checkbox = (CheckBox) days2.getChildAt(index);
                    checkbox.setChecked(intent.getBooleanArrayExtra("days")[index]);
                }
                break;
            default:
                freq2.setText("ERROR");
        }

        tags2.check(tags2.getChildAt(intent.getIntExtra("tag", 0)).getId());

        imageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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


        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ViewReminder.this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ViewReminder.this, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ViewReminder.this, timePicker, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        time_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ViewReminder.this, timePicker, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

    }

    public void confirmReminder(View view) {
        if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(date.getText()) || TextUtils.isEmpty(time.getText())) {
            Toast.makeText(getApplicationContext(),"Missing inputs!",Toast.LENGTH_SHORT).show();
        } else {
            Intent replyIntent = new Intent();

            int uidReply = uid;
            String titleReply = title2.getText().toString();
            long dateTimeReply = myCalendar.getTime().getTime();
            String descReply = desc2.getText().toString();
            int freqReply = frequencyChoice;

            int radioButtonID = tags2.getCheckedRadioButtonId();
            View radioButton = tags2.findViewById(radioButtonID);
            int tagReply = tags2.indexOfChild(radioButton);

            boolean[] daysReply = new boolean[7];

            if (frequencyChoice == Reminder.CUSTOM) {
                for (int index = 0; index < days2.getChildCount(); index++) {
                    daysReply[index] = ((CheckBox) days2.getChildAt(index)).isChecked();
                }

                replyIntent.putExtra("days", daysReply);
            }

            replyIntent.putExtra("uid", uidReply);
            replyIntent.putExtra("title", titleReply);
            replyIntent.putExtra("dateTime", dateTimeReply);
            replyIntent.putExtra("desc", descReply);
            replyIntent.putExtra("freq", freqReply);
            replyIntent.putExtra("tag", tagReply);

            setResult(MainActivity.EDIT_REMINDER_RESULT_CODE, replyIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(!editMode) {
            finish();

        } else {
            editMode = false;
            setContentView(R.layout.activity_view_reminder);

            setup();
        }
    }

    public void showDialog(View view) {
        DialogFragment newFragment = ReminderFrequencyDialog.newInstance(frequencyChoice);
        newFragment.show(getSupportFragmentManager(), "missiles");
    }

    public void updateDate() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date2.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateTime() {
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        time2.setText(sdf.format(myCalendar.getTime()));
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