package com.sargent.mark.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sargent.mark.todolist.data.Contract;
import com.sargent.mark.todolist.data.ToDoItem;

import java.util.ArrayList;

/**
 * Created by mark on 7/4/17.
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ItemHolder> {

    private Cursor cursor;
    private ItemClickListener listener;
    private String TAG = "todolistadapter";
    private boolean showVeryHigh;
    private boolean showHigh;
    private boolean showMedium;
    private boolean showLow;

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //get all settings from sharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        showVeryHigh = sharedPreferences.getBoolean("show_very_high", true);
        showHigh = sharedPreferences.getBoolean("show_high", true);
        showMedium = sharedPreferences.getBoolean("show_medium", true);
        showLow = sharedPreferences.getBoolean("show_low", true);

        View view = inflater.inflate(R.layout.item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder, position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface ItemClickListener {
        void onItemClick(int pos, String description, String duedate, boolean done, String category, long id);
    }

    public ToDoListAdapter(Cursor cursor, ItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descriptionView;
        TextView duedateView;
        String duedate;
        String description;
        boolean done;
        String category;
        long id;
        LinearLayout layout;


        ItemHolder(View view) {
            super(view);
            descriptionView = (TextView) view.findViewById(R.id.description);
            duedateView = (TextView) view.findViewById(R.id.dueDate);
            layout = (LinearLayout) view.findViewById(R.id.task_layout);
            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder, int pos) {
            cursor.moveToPosition(pos);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_TODO._ID));
            Log.d(TAG, "deleting id: " + id);

            //get all data from row that cursor point
            duedate = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
            description = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
            done = cursor.getInt(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DONE)) > 0;
            category = cursor.getString(cursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
            descriptionView.setText(description);
            duedateView.setText(duedate);

            //if done text will be strikethrough
            if(done) {
                descriptionView.setPaintFlags(descriptionView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                descriptionView.setPaintFlags(descriptionView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }


            //change background appearance according to category and preferences
            switch(category){
                case "Very High" :
                    if(showVeryHigh) {
                        layout.setBackgroundResource(R.color.taskVeryHigh);
                    }else{
                        hideItem(layout);
                } break;
                case "High" :
                    if(showHigh) {
                        layout.setBackgroundResource(R.color.taskHigh);
                    }else{
                        hideItem(layout);
                    } break;
                case "Medium" :
                    if(showMedium) {
                        layout.setBackgroundResource(R.color.taskMedium);
                    }else{
                        hideItem(layout);
                    } break;
                case "Low" :
                    if(showLow){
                        layout.setBackgroundResource(R.color.taskLow);
                    }else{
                        hideItem(layout);
                    } break;
                default: layout.setBackgroundColor(Color.GRAY); break;
            }

            holder.itemView.setTag(id);


        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos, description, duedate, done, category, id);
        }

        //method for hiding item in recyclerview
        private void hideItem(LinearLayout layout){
            layout.setVisibility(View.GONE);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.height = 0;
            layout.setLayoutParams(layoutParams);
        }
    }

}
