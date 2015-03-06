package com.example.saibot1207.tobiasapp;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by saibot1207 on 05.03.15.
 */
public class Tab1Fragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.layout.tab1);
    }





}
