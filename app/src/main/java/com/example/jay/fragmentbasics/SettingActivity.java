package com.example.jay.fragmentbasics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ExpandedMenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SettingActivity extends AppCompatActivity {
    /*
    * the file  pro_field_list_child.xml defines the layout of the professional field subtitles
    * the file  pro_field_list_parent.xml defines the layout of the professional field titles
    */

    private ExpandableListView proFieldListView;
    private LinkedHashMap<String,List<String>> proFieldHashmap;
    private List<String> proFieldDetail;        //stores the professional field data(check the proFieldDataProvider.java)
    private proFieldAdapter adapter;            //an adapter linked the data and expandable listview

    private AlertDialog.Builder proFieldDialogBuilder;
    private AlertDialog proFieldDialog;
    private Button btnProField;

    private ListView LVSelected;                                        //a listview displays what you select
    private ArrayList<String> selectedItems=new ArrayList<String>();    //an arraylist stores what you select
    private ArrayAdapter slctItemsAdapter;                              //an adapter links data and listview

    //private List<Integer> childSelected=new ArrayList<Integer>();
    private  List<String> childSelected =new ArrayList<String>();
    private  ArrayList<ArrayList<Boolean>> chkStatus=new ArrayList<ArrayList<Boolean>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //an expandable List View displays the professional fields
        proFieldListView= new ExpandableListView(this);    //the expandable listview displays the professional fields
        proFieldHashmap=proFieldDataProvider.getInfo();                      //get the data
        proFieldDetail=new ArrayList<String>(proFieldHashmap.keySet());    //get the proField-hashmap key and put it into the proFieldDetail-ArrayList
        adapter=new proFieldAdapter(this,proFieldHashmap,proFieldDetail);  //put proField-hashmap and the proFieldDetail-ArrayList into the adapter
        proFieldListView.setAdapter(adapter);

        //initialize the dialog contain the professional field
        proFieldDialogBuilder=new AlertDialog.Builder(SettingActivity.this);
        proFieldDialogBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                proFieldDialog.dismiss();
            }
        });
        proFieldDialogBuilder.setView(proFieldListView).setTitle("Please select : ");
        proFieldDialog=proFieldDialogBuilder.create();

        //childSelected.clear();

        //initialize the listview  which displays the selected items
        LVSelected=(ListView)findViewById(R.id.listViewSelected);
        slctItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectedItems);
        LVSelected.setAdapter(slctItemsAdapter);

        //a button to show a professional field
        btnProField = (Button) findViewById(R.id.btnProField);
        btnProField.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                proFieldDialog.show();
            }
        });

        /*
        *  a listener checks which item(child) is selected
        *  if an item(child) is selected it will toast which item is selected and add to the listview under the professional field.
        */

        proFieldListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
           @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               CheckedTextView chkTextView=(CheckedTextView)v.findViewById(R.id.textView_child);
               chkTextView.setChecked(adapter.setChildClick(groupPosition, childPosition));
               if(childSelected.contains(proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition))){
                    childSelected.remove(proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition));
                    selectedItems.remove(proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition));
                    slctItemsAdapter.notifyDataSetChanged();
                    Toast.makeText(getBaseContext(), proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition) + " is removed.", Toast.LENGTH_SHORT).show();
                }else{
                   childSelected.add(proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition));
                    selectedItems.add(proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition));
                    slctItemsAdapter.notifyDataSetChanged();
                    Toast.makeText(getBaseContext(), proFieldHashmap.get(proFieldDetail.get(groupPosition)).get(childPosition) + " is selected.", Toast.LENGTH_SHORT).show();
                }
               return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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

    public void btnSave_click(View view){
        Intent intent = new Intent(this,UserInfo.class);
        startActivity(intent);
        finish();
    }
}

