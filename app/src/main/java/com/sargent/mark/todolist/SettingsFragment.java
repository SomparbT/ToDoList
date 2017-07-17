package com.sargent.mark.todolist;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Siriporn on 7/16/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    //fragment of settings page
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_todo);
    }
}
