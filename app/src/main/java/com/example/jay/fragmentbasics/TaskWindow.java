package com.example.jay.fragmentbasics;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


public class TaskWindow extends Activity {
    private LinearLayout ly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_window);

        ly = (LinearLayout) findViewById(R.id.taskWindowLy);

        //please set bg color according to your help type.
        int alpha = 50;
        setBgColor(alpha, "RED", ly);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_window, menu);
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

    /**
     * This is a function setting background color.
     * When you get your help's type, you can set bgcolor to HelpWindow accordingly.
     *
     * @param alpha Color's alpha
     * @param color switch
     * @param ly pass LinearLayout here
     * */
    private void setBgColor(int alpha, String color, LinearLayout ly){
        switch (color){
            case "RED":
                ly.setBackgroundColor(Color.argb(alpha, 255, 0, 0));
                break;
            case "GREEN":
                ly.setBackgroundColor(Color.argb(alpha, 0, 255, 0));
                break;
            case "BLUE":
                ly.setBackgroundColor(Color.argb(alpha, 0, 0, 255));
            break;
            case "WHITE":
            default:
                ly.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                break;
        }
    }
}
