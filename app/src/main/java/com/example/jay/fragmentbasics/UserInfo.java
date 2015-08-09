package com.example.jay.fragmentbasics;

import android.app.ActionBar;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.widget.ListView.*;

public class UserInfo extends AppCompatActivity {

    private VerticalProgressBar vProgressBar;
    private ListView ReceivedList;
    private ListView GivenList;
    private String[] Received;  //store the accepted list
    private String[] Given;    //store the helped list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        /*
        * the lists.xm file in vlaues file  list the  accepted list item and the helped list item
        */
        //set the accepted litview
        Received=getResources().getStringArray(R.array.accepted);
        ReceivedList=(ListView)findViewById(R.id.listViewReceived);
        ArrayAdapter<String> a =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Received);
        ReceivedList.setAdapter(a);

        //set the helped listview
        Given=getResources().getStringArray(R.array.helped);
        GivenList=(ListView)findViewById(R.id.listViewGiven);
        ArrayAdapter<String> b=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Given);
        GivenList.setAdapter(b);
        GivenList.setVisibility(INVISIBLE);

        //set the vertical progress bar to 2%
        vProgressBar=(VerticalProgressBar)findViewById(R.id.verticalProgressBar);
        vProgressBar.setProgress(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
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
    //when the accepted button i s pressed acceptedList will be visble and helpedList will hide
    public void btnReceived_click(View view){
        ReceivedList.setVisibility(ListView.VISIBLE);
        GivenList.setVisibility(ListView.INVISIBLE);
    }
    //when the helped button i s pressed acceptedList will hide and helpedList will be visble
    public void btnGiven_click(View view){
        GivenList.setVisibility(ListView.VISIBLE);
        ReceivedList.setVisibility(ListView.INVISIBLE);
    }
    //when the setting button(an image button) is pressed start the settingactivity
    public void imgBtnSetting_click(View view){
        Intent intent=new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
}
