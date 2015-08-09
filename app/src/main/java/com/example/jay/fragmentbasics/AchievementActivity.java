package com.example.jay.fragmentbasics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;

import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.view.*;
import android.widget.AdapterView;

import android.support.v7.app.AppCompatActivity;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.app.Activity;

public class AchievementActivity extends AppCompatActivity {
    //This is a string array for listview, and those should be dynamically loaded when you start implementing
    private String[] myAchievement = {"Uplevel to LV5!", "LV10", "Help 10 people", "Help 100 people"};
    //This is an img src array for listview
    private int[] myAchievementImg = {R.drawable.star_icon, R.drawable.lock_icon, R.drawable.lock_icon, R.drawable.lock_icon};

    //This is the adapter
    private AchievementAdapter listAdapter;
    //My Achievement ListView
    private ListView achievementListView;
    private Context context;
    private ActionBar actionBar;

    private void customActionBar() {
        try {
            actionBar = getActionBar();
            //actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255, 255, 255, 255)));
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_achievement_actionbar, null);
            actionBar.setCustomView(actionBarLayout, layout);

        }catch (NullPointerException npe){
            Log.d("Jean", "NPE");
            Log.e("Jean", "error:"+npe.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_achievement);

        Log.d("Jean","actionbar start");
        //set actionbar
        customActionBar();
        Log.d("Jean", "actionbar done");

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
        if (id == R.id.menu_next) {
            startActivity(new Intent(AchievementActivity.this, HeatmapActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }
}
