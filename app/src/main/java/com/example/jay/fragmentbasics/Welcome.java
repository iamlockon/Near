package com.example.jay.fragmentbasics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;
import com.parse.ui.ParseSignupFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Welcome extends Activity{
    Button btnWelcome,btnLogin;
    public boolean isShownAlready = false;
    ParseUser user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnWelcome = (Button)findViewById(R.id.btnWelcome);
        btnWelcome.setOnClickListener(btnWelcomeClickListener);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(btnLoginClickListener);
        final Animation animation = new AlphaAnimation(1.0f, 0.5f); // Change alpha from fully visible to invisible
        animation.setDuration(1500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        btnWelcome.startAnimation(animation);
        //Check user's gps provider
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //OK
        }else{
            showGPSDisabledAlertToUser();
        }
        user = ParseUser.getCurrentUser();
        user.put("isShownAlready",false);
        user.pinInBackground();
    }

    private Button.OnClickListener btnWelcomeClickListener =new Button.OnClickListener(){
        Intent i = new Intent();

        public void onClick(View v){
            if(haveNetworkConnection()) {
                v.clearAnimation();
                i.setClass(Welcome.this, MainActivity.class);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.fromLocalDatastore();
                query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            isShownAlready = object.getBoolean("isShownAlready");
                            if (isSuccessive() && !isShownAlready) showBonusDialog(i);
                            else startActivity(i);
                        } else {
                            // something went wrong
                        }
                    }
                });
            }else{
                new AlertDialog.Builder(Welcome.this)
                        .setTitle("Network is absent")
                        .setIcon(com.parse.ui.R.drawable.com_parse_ui_app_logo)
                        .setMessage("Click \"Setting\" to switch on, \"Quit\" to finish")
                        .setPositiveButton("Setting", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                                    }
                                })
                        .setNegativeButton("Quit", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        finish();
                                    }
                                })
                        .show();
            }
        }
    };
    private Button.OnClickListener btnLoginClickListener =new Button.OnClickListener(){
        public void onClick(View v){
            Intent i = new Intent(Welcome.this, ParseLoginActivity.class);
            startActivity(i);
        }
    };
    private void showBonusDialog(final Intent intent){
        user.put("isShownAlready",true);
        user.pinInBackground();
        AlertDialog.Builder adNotice =new AlertDialog.Builder(Welcome.this);
        adNotice.setTitle("Congratulations")
                .setIcon(R.drawable.notification_template_icon_bg)
                .setMessage("You've got bonus achievement points for successive login.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                })
                .show();
    }
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Click \"Settings\" to enable, \"Quit\" to finish")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Quit",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    private boolean isSuccessive(){
        Calendar c = Calendar.getInstance();
        Calendar c_up = Calendar.getInstance();
        int month_c = c.get(Calendar.MONTH) + 1;           //取出月，月份的編號是由0~11 故+1
        int day_c = c.get(Calendar.DAY_OF_MONTH);        //取出日

        ParseUser user = ParseUser.getCurrentUser();
        Date update_date = user.getUpdatedAt();
        c_up.setTime(update_date);
        int month_c_up = c.get(Calendar.MONTH) + 1;           //取出月，月份的編號是由0~11 故+1
        int day_c_up = c.get(Calendar.DAY_OF_MONTH);        //取出日

        int month_diff = month_c - month_c_up;
        int day_diff = day_c - day_c_up;
        //Login everyday
        if(month_diff == 0 && day_diff < 2){
            return true;
        }else return false;

    }
    private boolean haveNetworkConnection() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() &&    conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            System.out.println("Internet Connection Not Present");
            return false;
        }
    }
}
