package com.example.jay.fragmentbasics;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;

import java.util.Arrays;

public class Welcome extends Activity{
    //Created by Jean for Achievement Test
    Button btnJean;

    Button btnWelcome,btnLogin;
    boolean loginYest =false;
    boolean isSuccessive = false;
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
        //TODO Get yesterday login (boolean)
        ParseUser user = ParseUser.getCurrentUser();

        btnJean = (Button) findViewById(R.id.btnJean);
        btnJean.setText("View Jean's Activity");
        btnJean.setOnClickListener(new Button.OnClickListener(){
            Intent i = new Intent();
            public void onClick(View v){
                v.clearAnimation();
                i.setClass(Welcome.this, HotmapActivity.class);
                    startActivity(i);

            }
        });

    }
    private Button.OnClickListener btnWelcomeClickListener =new Button.OnClickListener(){
        Intent i = new Intent();
        public void onClick(View v){
            v.clearAnimation();
            i.setClass(Welcome.this, MainActivity.class);
            if(isSuccessive)showBonusDialog(i);
            else{
                startActivity(i);
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
        AlertDialog.Builder adNotice =new AlertDialog.Builder(Welcome.this);
        adNotice.setTitle("Congratulations")
                .setIcon(R.drawable.notification_template_icon_bg)
                .setMessage("You've got bonus achievement points for successive login.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(intent);
                    }
                })
                .show();
    }
}
