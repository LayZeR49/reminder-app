package com.example.reminderapp.room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    public static ReminderRepository repository;
    public final LiveData<List<Reminder>> allRemindersReverse;

    public ReminderViewModel (Application application) {
        super(application);
        repository = new ReminderRepository(application);
        allRemindersReverse = repository.getAllReverse();
    }

    public LiveData<List<Reminder>> getAllReverse() { return allRemindersReverse; }

    public static void insert(Reminder reminder) { repository.insert(reminder); }
    public static void update(Reminder reminder) { repository.update(reminder); }
    public static void delete(Reminder reminder) { repository.delete(reminder); }
    public static LiveData<Reminder> getById(int uid) { return repository.getById(uid); }
}
