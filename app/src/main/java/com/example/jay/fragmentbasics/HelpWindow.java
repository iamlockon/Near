package com.example.jay.fragmentbasics;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class HelpWindow extends Activity {
    private Spinner helpTypeSpinner;
    private ArrayAdapter helpTypeAdapter;
    private String[] helpTypes = {"Medicine", "Car", "Hardware check", "PHP debug", "Moving things to other place"};
    private RadioGroup helpColorRadioGroup;
    private LinearLayout ly;
    private Button sendHelpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_window);

        ly = (LinearLayout) findViewById(R.id.helpWindowLy);
        //Send Help Button
        sendHelpBtn = (Button) findViewById(R.id.sendHelpBtn);
        sendHelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ly.getContext(), "Your help message is sent!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //Spinner
        helpTypeSpinner = (Spinner) findViewById(R.id.helpTypeSpinner);
        helpTypeAdapter= new ArrayAdapter(this,android.R.layout.simple_spinner_item, helpTypes);
        helpTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        helpTypeSpinner.setAdapter(helpTypeAdapter);

        //Radio Buttons
        helpColorRadioGroup = (RadioGroup) findViewById(R.id.helpColorRadioGroup);
        helpColorRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Toast.makeText(null, "id:"+checkedId, Toast.LENGTH_SHORT).show();
                int alpha = 100;
                switch (checkedId){
                    case R.id.redRadioButton:
                        ly.setBackgroundColor(Color.argb(alpha,255,0,0));
                        break;
                    case R.id.greenRadioButton:
                        ly.setBackgroundColor(Color.argb(alpha,0,255,0));
                        break;
                    case R.id.blueRadioButton:
                        ly.setBackgroundColor(Color.argb(alpha,0,0,255));
                        break;
                    case R.id.whiteRadioButton:
                        ly.setBackgroundColor(Color.WHITE);
                        break;
                    default:
                        return;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
