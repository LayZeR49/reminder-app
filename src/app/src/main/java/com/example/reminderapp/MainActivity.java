package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminderapp.room.AppDatabase;
import com.example.reminderapp.room.Reminder;
import com.example.reminderapp.room.ReminderAdapter;
import com.example.reminderapp.room.ReminderViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    BottomAppBar bottomAppBar;
    LinearLayout layoutReminders;
    CardView cardBase;
    RecyclerView recyclerView;
    TextView currentDateView;
    Date currentDate;
    Calendar calendar;
    TableRow currentWeekDays;
    View lastClicked;

    ViewGroup.LayoutParams params;
    ViewGroup.LayoutParams paramsHide;

    LinearLayoutManager linearLayoutManager;
    AppDatabase db;
    ReminderAdapter reminderAdapter;
    private ReminderViewModel reminderViewModel;

    public static final int CREATE_REMINDER_ACTIVITY_REQUEST_CODE = 1;
    public static final int VIEW_REMINDER_ACTIVITY_REQUEST_CODE = 2;

    public static final int DELETE_REMINDER_RESULT_CODE = 7;
    public static final int EDIT_REMINDER_RESULT_CODE = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        setSupportActionBar(bottomAppBar);
        layoutReminders = (LinearLayout) findViewById(R.id.layout_reminders);
        cardBase = (CardView) findViewById(R.id.card_base);
        currentWeekDays = (TableRow) findViewById(R.id.current_week_days);

        currentDateView = (TextView) findViewById(R.id.et_currentDate);
       /* view = (View) findViewById(R.id.bottom_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(bottomAppBar.getWidth(), bottomAppBar.getHeight());
        view.setLayoutParams(params);*/

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        linearLayoutManager = new LinearLayoutManager(this);
        reminderAdapter = new ReminderAdapter(new ReminderAdapter.ReminderDiff(), new ReminderAdapter.ReminderListener() {
            @Override
            public void onClickCard(Reminder reminder, View view) {
                lastClicked = view;
                params = view.getLayoutParams();
                paramsHide = params;
                paramsHide.height = 0;
                viewReminder(reminder);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reminderAdapter);

        currentDate = new Date();
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        currentDateView.setText(sdf.format(currentDate));

        calendar = Calendar.getInstance();

        for (int index = 1; index <= 7; index++) {
            calendar.set(Calendar.DAY_OF_WEEK, index);
            LinearLayout dayLayout = (LinearLayout) currentWeekDays.getChildAt(index-1);
            TextView day = (TextView) dayLayout.getChildAt(0);

            if(sdf.format(currentDate).equals(sdf.format(calendar.getTime()))) {
                day.setBackgroundResource(R.drawable.circle);
                day.setTextColor(Color.WHITE);
            }

            day.setText(String.valueOf(calendar.get(Calendar.DATE)));
        }

        //reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        reminderViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ReminderViewModel.class);

        reminderViewModel.getAllReverse().observe(this, reminders -> {
            reminderAdapter.submitList(reminders);
        });



        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        /*bottomAppBar.setOnMenuItemClickListener() { menuItem ->
                when (menuItem.itemId) {
            R.id.search -> {

                true
            }
            R.id.more -> {

                true
            }
        else -> false
        }
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar,menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Date date = new Date();

        if (requestCode == CREATE_REMINDER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Reminder reminder;

            if(data.getIntExtra("freq", 0) != Reminder.CUSTOM) {
                Log.d("REMINDER", "NODAYS");
                reminder = new Reminder(date.getTime(), data.getStringExtra("title"), data.getLongExtra("dateTime", 0),
                        data.getIntExtra("freq", 0), data.getStringExtra("desc"), data.getIntExtra("tag", 0));
            } else {
                Log.d("REMINDER", "DAYS");
                reminder = new Reminder(date.getTime(), data.getStringExtra("title"), data.getLongExtra("dateTime", 0),
                        data.getStringExtra("desc"), data.getIntExtra("tag", 0), data.getBooleanArrayExtra("days"));
            }
            Toast.makeText(
                    getApplicationContext(),
                    "Reminder created",
                    Toast.LENGTH_LONG).show();
            ReminderViewModel.insert(reminder);

        } else {

        }
        if (requestCode == VIEW_REMINDER_ACTIVITY_REQUEST_CODE) {
            if(resultCode == MainActivity.EDIT_REMINDER_RESULT_CODE) {
                Reminder reminder;

                if(data.getIntExtra("freq", 0) != Reminder.CUSTOM) {
                    Log.d("REMINDER", "NODAYS");
                    reminder = new Reminder(date.getTime(), data.getStringExtra("title"), data.getLongExtra("dateTime", 0),
                            data.getIntExtra("freq", 0), data.getStringExtra("desc"), data.getIntExtra("tag", 0));
                } else {
                    Log.d("REMINDER", "DAYS");
                    reminder = new Reminder(date.getTime(), data.getStringExtra("title"), data.getLongExtra("dateTime", 0),
                            data.getStringExtra("desc"), data.getIntExtra("tag", 0), data.getBooleanArrayExtra("days"));

                }


                reminder.setUid(data.getIntExtra("uid", -1));
                ReminderViewModel.update(reminder);

                Intent intent = new Intent(MainActivity.this, ViewReminder.class);
                intent.putExtra("uid", reminder.getUid());
                intent.putExtra("title", reminder.getTitle());
                intent.putExtra("dateTime", reminder.getDateTime());
                intent.putExtra("days", reminder.getDays());
                intent.putExtra("freq", reminder.getRepeatFrequency());
                intent.putExtra("desc", reminder.getDesc());
                intent.putExtra("tag", reminder.getTag());

                startActivityForResult(intent, VIEW_REMINDER_ACTIVITY_REQUEST_CODE);
            } else if (resultCode == MainActivity.DELETE_REMINDER_RESULT_CODE) {

                Snackbar.make(cardBase, "Deleting reminder...", Snackbar.LENGTH_LONG)
                        .addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                switch (event) {
                                    case Snackbar.Callback.DISMISS_EVENT_ACTION:

                                        break;
                                    default:
                                        Reminder reminder = new Reminder();
                                        reminder.setUid(data.getIntExtra("uid", -1));
                                        ReminderViewModel.delete(reminder);
                                        break;
                                }
                            }
                        })
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();
            } else {

            }
        } else {

        }

    }

    public void createReminder(View view) {
        Intent intent = new Intent(this, CreateReminder.class);
        startActivityForResult(intent, CREATE_REMINDER_ACTIVITY_REQUEST_CODE);
    }

    public void viewReminder(Reminder reminder) {
        Intent intent = new Intent(MainActivity.this, ViewReminder.class);
        intent.putExtra("uid", reminder.getUid());
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("dateTime", reminder.getDateTime());
        intent.putExtra("days", reminder.getDays());
        intent.putExtra("freq", reminder.getRepeatFrequency());
        intent.putExtra("desc", reminder.getDesc());
        intent.putExtra("tag", reminder.getTag());

        startActivityForResult(intent, VIEW_REMINDER_ACTIVITY_REQUEST_CODE);
    }


}

/*

 */

/*CardView cardView = new CardView(MainActivity.this);
                float factor = getResources().getDisplayMetrics().density;
                int dp100 = (int) factor * 100;
                LinearLayout.LayoutParams params = cardBase.getLayoutParams();

                LinearLayout layoutParent = new LinearLayout(MainActivity.this);
                LinearLayout layoutChild1 = new LinearLayout(MainActivity.this);
                TextView textView11 = new TextView(MainActivity.this);
                TextView textView12 = new TextView(MainActivity.this);
                LinearLayout layoutChild2 = new LinearLayout(MainActivity.this);
                TextView textView21 = new TextView(MainActivity.this);
                TextView textView22 = new TextView(MainActivity.this);
                View view = new View(MainActivity.this);

                TextView test = new TextView(MainActivity.this);
                test.setText("HEEEYYY");

                int margin8 = (int) factor * 8;

                params.setMargins(margin8, margin8, margin8, margin8);
                cardView.setLayoutParams(params);
                cardView.setCardElevation(8);
                cardView.setRadius(8);

                cardView.addView(test);
                layoutReminders.addView(cardView);*/

                /*
                layoutParent.setOrientation(LinearLayout.HORIZONTAL);

                params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.CENTER_VERTICAL;
                layoutChild1.setLayoutParams(params);
                layoutChild1.setBackgroundResource(R.drawable.rectangle);

                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.gravity = Gravity.NO_GRAVITY;
                layoutChild2.setLayoutParams(params);

                params.width = 30;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                params.setMargins(0, 0, 0, 0);
                view.setLayoutParams(params);

                textView11.setTextSize(24);
                textView11.setText("10");

                textView12.setText("AM");

                textView21.setTextSize(18);
                //SET FONT / FONTFACE
                textView21.setText("TITLE");

                textView22.setText("This is the description");

                layoutChild1.addView(textView11);
                layoutChild1.addView(textView12);
                layoutChild2.addView(textView21);
                layoutChild2.addView(textView22);
                layoutParent.addView(layoutChild1);
                layoutParent.addView(layoutChild2);
                layoutParent.addView(view);
                cardView.addView(layoutParent);*/