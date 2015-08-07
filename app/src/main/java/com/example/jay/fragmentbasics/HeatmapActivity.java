package com.example.jay.fragmentbasics;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import android.view.ViewGroup.LayoutParams;

public class HeatmapActivity extends Activity {
    private ToggleButton btnSwitchToList;
    private ImageView hotmap;

    private ListView listMap;
    private ArrayAdapter listMapAdapter;
    private String[] listDis = {"North", "Central", "South", "East"};

    private String[] hotTopics = {"Illegal parking problem!", "Some keep dumping cigarette ends on street!", "Elderly live alone"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);

        btnSwitchToList = (ToggleButton) findViewById(R.id.btnSwitchToList);
        hotmap = (ImageView) findViewById(R.id.hotmap);
        listMap = (ListView) findViewById(R.id.listView);
        listMapAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listDis);
        listMap.setAdapter(listMapAdapter);
        //a hack for ListView inside ScrollView
        if (listMapAdapter != null) {

            int totalHeight = 0;
            for (int i = 0, len = listMapAdapter.getCount(); i < len; i++) {
                View listItem = listMapAdapter.getView(i, null, listMap);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listMap.getLayoutParams();
            params.height = totalHeight + (listMap.getDividerHeight() * (listMapAdapter.getCount() - 1));
            listMap.setLayoutParams(params);
        }


        ToggleButton toggle = (ToggleButton) findViewById(R.id.btnSwitchToList);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    hotmap.setVisibility(View.GONE);
                    listMap.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    hotmap.setVisibility(View.VISIBLE);
                    listMap.setVisibility(View.GONE);
                }
            }
        });

        TableLayout hotTopicsTable = (TableLayout) findViewById(R.id.hotTopicsTable);
        //Hot topics
        for(int i = 0; i<hotTopics.length; i++){
            TableRow tr = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tr.setLayoutParams(lp);

            TextView topicName = new TextView(this);
            topicName.setText(hotTopics[i]);
            topicName.setGravity(Gravity.CENTER);

//            ImageView starIcon = new ImageView(this);
//            starIcon.setMaxHeight(20);
//            starIcon.setMaxWidth(20);
//            starIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            starIcon.setImageResource(R.drawable.star_icon);

            Button voteBtn = new Button(this);
            voteBtn.setText("VOTE!");
            voteBtn.setOnClickListener(new VoteOnClickListener(hotTopics[i]));

//            tr.addView(starIcon);
            tr.addView(topicName);
            tr.addView(voteBtn);

            hotTopicsTable.addView(tr);
        }

    }

   private void showVoteWindow(View v, String topicName){
       LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View popUpView = inflater.inflate(R.layout.activity_heatmap_popup, null, false);
       TextView topicNameForPopup = (TextView) popUpView.findViewById(R.id.topicNameTextView);
       Button sendCommentForPopup = (Button) popUpView.findViewById(R.id.sendCommentForPopup);

       final PopupWindow window = new PopupWindow(popUpView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);

       topicNameForPopup.setText(topicName);
       sendCommentForPopup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast toast = Toast.makeText(v.getContext(), "Thank you!\n Your comment is sent!", Toast.LENGTH_LONG);
               toast.setGravity(Gravity.CENTER, 0, 0);
               toast.show();
               window.dismiss();
           }
       });
       window.setTouchable(true);
       window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       window.showAtLocation(v, Gravity.CENTER, 0, 0);
   }

   class  VoteOnClickListener implements View.OnClickListener{
       private String topicName;
       public VoteOnClickListener(String topicName){
           this.topicName = topicName;
       }

       @Override
       public void onClick(View v) {
          // Toast.makeText(v.getContext(),this.topicName, Toast.LENGTH_SHORT).show();
           showVoteWindow(v, topicName);
       }
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hotmap, menu);
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
