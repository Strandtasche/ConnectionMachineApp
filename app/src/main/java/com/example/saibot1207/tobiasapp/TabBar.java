package com.example.saibot1207.tobiasapp;

/**
 * Created by saibot1207 on 23.02.15.
 */

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.app.TabActivity;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TabBar extends TabActivity implements OnTabChangeListener{


    TabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tabHost = getTabHost();




        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, Tab1.class);
        spec = tabHost.newTabSpec("First").setIndicator("Settings")
                .setContent(intent);

        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab2.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Game")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Tab3.class);
        spec = tabHost.newTabSpec("Third").setIndicator("Credits")
                .setContent(intent);

        tabHost.addTab(spec);




        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(-7829368);
        tabHost.getTabWidget().getChildAt(2).setBackgroundColor(-7829368);

        tabHost.getTabWidget().setCurrentTab(2);
        //tabHost.getTabWidget().getChildAt(1).setBackgroundColor(-1);


    }

    @Override
    public void onTabChanged(String tabId) {

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            if(i==0)
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(-7829368);
            else if(i==1)
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(-7829368);
            else if(i==2)
                tabHost.getTabWidget().getChildAt(i).setBackgroundColor(-7829368);
        }


        Log.i("tabs", "CurrentTab: "+tabHost.getCurrentTab());

        if(tabHost.getCurrentTab()==0)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(-1);
        else if(tabHost.getCurrentTab()==1)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(-1);
        else if(tabHost.getCurrentTab()==2)
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(-1);

    }

}