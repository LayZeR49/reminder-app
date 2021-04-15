package com.example.reminderapp.room;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReminderRepository {
    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> allRemindersReverse;


    ReminderRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        reminderDao = db.reminderDao();
        allRemindersReverse = reminderDao.getAllReverse();
    }

    LiveData<List<Reminder>> getAllReverse() {
        return allRemindersReverse;
    }


    void insert(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reminderDao.insert(reminder);
        });
    }

    void update(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reminderDao.update(reminder);
        });
    }

    void delete(Reminder reminder) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            reminderDao.delete(reminder);
        });
    }

    LiveData<Reminder> getById(int uid) {
        return reminderDao.getById(uid);
    }

}
