package com.example.reminderapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "timestamp")
    public long timestamp;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "datetime")
    private long dateTime;
    @ColumnInfo(name = "description")
    private String desc;
    @ColumnInfo(name = "tag")
    private int tag;
    @ColumnInfo(name = "repeat_frequency")
    private int repeatFrequency;
    @ColumnInfo(name = "monday")
    private boolean monday;
    @ColumnInfo(name = "tuesday")
    private boolean tuesday;
    @ColumnInfo(name = "wednesday")
    private boolean wednesday;
    @ColumnInfo(name = "thursday")
    private boolean thursday;
    @ColumnInfo(name = "friday")
    private boolean friday;
    @ColumnInfo(name = "saturday")
    private boolean saturday;
    @ColumnInfo(name = "sunday")
    private boolean sunday;

    //0never, 1daily, 2custom
    public final static int CUSTOM = 2;
    @Ignore
    public final static int DAYS_NUMBER = 7;


    public Reminder() {
    }

    public Reminder(Reminder reminder) {
        this.uid = reminder.uid;
        this.timestamp = reminder.timestamp;
        this.title = reminder.title;
        this.dateTime = reminder.dateTime;
        this.repeatFrequency = reminder.repeatFrequency;
        this.desc = reminder.desc;
        this.tag = reminder.tag;
        this.monday = reminder.monday;
        this.tuesday = reminder.tuesday;
        this.wednesday = reminder.wednesday;
        this.thursday = reminder.thursday;
        this.friday = reminder.friday;
        this.saturday = reminder.saturday;
        this.sunday = reminder.sunday;
    }

    public Reminder(long timestamp, String title, long dateTime, int repeatFrequency, String desc, int tag) {
        this.timestamp = timestamp;
        this.title = title;
        this.dateTime = dateTime;
        this.repeatFrequency = repeatFrequency;
        this.desc = desc;
        this.tag = tag;
    }
    public Reminder(long timestamp, String title, long dateTime, String desc, int tag, boolean[] days) {
        this.timestamp = timestamp;
        this.title = title;
        this.dateTime = dateTime;
        this.repeatFrequency = Reminder.CUSTOM;
        this.desc = desc;
        this.tag = tag;
        this.monday = days[0];
        this.tuesday = days[1];
        this.wednesday = days[2];
        this.thursday = days[3];
        this.friday = days[4];
        this.saturday = days[5];
        this.sunday = days[6];
    }

    public String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");

        return sdf.format(new Date(timestamp));
    }

    public String getDebugString() {
        return "UID: " + uid + " Timestamp: " + getTimestamp() + " Title: " + title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(int repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public boolean[] getDays() {
        boolean[] days = new boolean[7];
        days[0] = monday;
        days[1] = tuesday;
        days[2] = wednesday;
        days[3] = thursday;
        days[4] = friday;
        days[5] = saturday;
        days[6] = sunday;

        return days;
    }

    public void setDays(boolean days[]) {
        monday = days[0];
        tuesday = days[1];
        wednesday = days[2];
        thursday = days[3];
        friday = days[4];
        saturday = days[5];
        sunday = days[6];
    }
}
