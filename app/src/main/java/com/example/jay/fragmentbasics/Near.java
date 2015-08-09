package com.example.jay.fragmentbasics;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Jay on 2015/7/28.
 */
public class Near extends Application {

    public void onCreate(){
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "oy107iHrlDR1HQW5GcoHJZv1fYUvy1yaKza8eJYi",
                "LfsBpV2HF8BygaZGsV7IRQeXlFttOXnOb8qK0rx8");
    }
}
