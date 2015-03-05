package com.example.saibot1207.tobiasapp;

import android.preference.ListPreference;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Copied from Stackoverflow on 5.3.2015
 */

public class DynamicListPreference extends ListPreference {
    public interface DynamicListPreferenceOnClickListener {
        public void onClick(DynamicListPreference preference);
    }

    private DynamicListPreferenceOnClickListener mOnClickListener;

    public DynamicListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick(){
        if (mOnClickListener != null)
            mOnClickListener.onClick(this);
        super.onClick();
    }

    public void setOnClickListener(DynamicListPreferenceOnClickListener l) {
        mOnClickListener = l;
    }

}