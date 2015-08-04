package com.example.jay.fragmentbasics;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;

import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.view.*;
import android.widget.AdapterView;


public class AchievementActivity extends ActionBarActivity {
    //This is a string array for listview, and those should be dynamically loaded when you start implementing
    private String[] myAchievement = {"Uplevel to LV5!", "LV10", "Help 10 people", "Help 100 people"};
    //This is an img src array for listview
    private int[] myAchievementImg = {R.drawable.star_icon, R.drawable.lock_icon, R.drawable.lock_icon, R.drawable.lock_icon};

    //This is the adapter
    private AchievementAdapter listAdapter;
    //My Achievement ListView
    private ListView achievementListView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        context=this;

        achievementListView = (ListView) findViewById(R.id.listAchievement);
        listAdapter = new AchievementAdapter(this, myAchievement, myAchievementImg);
        achievementListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_achievement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
