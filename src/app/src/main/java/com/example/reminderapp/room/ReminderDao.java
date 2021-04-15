package com.example.reminderapp.room;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminder")
    LiveData<List<Reminder>> getAll();
    @Query("SELECT * FROM reminder ORDER BY uid DESC")
    LiveData<List<Reminder>> getAllReverse();
    @Query("SELECT * FROM reminder WHERE uid = :id")
    LiveData<Reminder> getById(int id);

    @Insert
    void insertAll(Reminder... reminders);
    @Insert
    void insert(Reminder reminder);


    @Update
    public void updateReminders(Reminder... reminders);
    @Update
    public void update(Reminder reminder);

    @Delete
    void delete(Reminder reminder);


    @Query("DELETE FROM reminder")
    void deleteAll();
    @Query("DELETE FROM reminder WHERE uid = :id")
    void deleteByItemId(long id);
}
