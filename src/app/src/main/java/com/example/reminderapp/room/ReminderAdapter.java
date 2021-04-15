package com.example.reminderapp.room;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;


public class ReminderAdapter extends ListAdapter<Reminder, ReminderViewHolder> {

    public interface ReminderListener {
        public void onClickCard(Reminder reminder, View view);
    }

    ReminderListener listener;

    public ReminderAdapter(@NonNull DiffUtil.ItemCallback<Reminder> diffCallback, ReminderListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ReminderViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        Reminder current = getItem(position);
        holder.bind(current, listener);

    }

    public static class ReminderDiff extends DiffUtil.ItemCallback<Reminder> {
        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem.getTitle().matches(newItem.getTitle());
        }
    }
}