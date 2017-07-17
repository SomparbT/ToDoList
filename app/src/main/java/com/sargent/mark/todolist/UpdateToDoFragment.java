package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Calendar;

/**
 * Created by mark on 7/5/17.
 */

public class UpdateToDoFragment extends DialogFragment {

    private EditText toDo;
    private DatePicker dp;
    private Button add;
    private Spinner category_spinner;
    private RadioButton notdone_button;
    private RadioButton done_button;
    private boolean mDone;
    private String mCategory;
    private final String TAG = "updatetodofragment";
    private long id;


    public UpdateToDoFragment(){}

    public static UpdateToDoFragment newInstance(int year, int month, int day, String description, boolean done, String category, long id) {
        UpdateToDoFragment f = new UpdateToDoFragment();

        // Supply all data as an argument.
        Bundle args = new Bundle();
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putLong("id", id);
        args.putString("description", description);
        args.putBoolean("done", done);
        args.putString("category", category);

        f.setArguments(args);

        return f;
    }

    //To have a way for the activity to get the data from the dialog
    public interface OnUpdateDialogCloseListener {
        void closeUpdateDialog(int year, int month, int day, String description, boolean done, String category, long id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_do_adder, container, false);
        toDo = (EditText) view.findViewById(R.id.toDo);
        dp = (DatePicker) view.findViewById(R.id.datePicker);
        category_spinner = (Spinner) view.findViewById(R.id.categories_spinner);
        notdone_button = (RadioButton) view.findViewById(R.id.radButton1);
        done_button = (RadioButton) view.findViewById(R.id.radButton2);
        add = (Button) view.findViewById(R.id.add);

        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");
        id = getArguments().getLong("id");
        String description = getArguments().getString("description");
        dp.updateDate(year, month, day);

        toDo.setText(description);

        //check radio button according to done status
        if(getArguments().getBoolean("done")){
            done_button.setChecked(true);
        }else{
            notdone_button.setChecked(true);
        }

        //convert category string to index
        int categoryIndex = 0;
        switch(getArguments().getString("category")){
            case "Very High" : categoryIndex = 0; break;
            case "High" : categoryIndex = 1; break;
            case "Medium" : categoryIndex = 2; break;
            case "Low" : categoryIndex = 3; break;
            default: categoryIndex = 0; break;
        }


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        category_spinner.setAdapter(adapter);
        category_spinner.setSelection(categoryIndex);


        add.setText("Update");
        //save all data to database
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateToDoFragment.OnUpdateDialogCloseListener activity = (UpdateToDoFragment.OnUpdateDialogCloseListener) getActivity();
                Log.d(TAG, "id: " + id);
                activity.closeUpdateDialog(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), toDo.getText().toString(), done_button.isChecked(), category_spinner.getSelectedItem().toString(), id);
                UpdateToDoFragment.this.dismiss();
            }
        });

        return view;
    }
}