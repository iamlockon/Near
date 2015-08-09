package com.example.jay.fragmentbasics;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;

import java.util.List;

public class UserListActivity extends AppCompatActivity {
    ListView lstUser;
    String[] Username;
    String[] Places = new String[] {"TAF-ZoneA-1F-RoomA-01","TAF-ZoneB-3F-RoomC-02",
            "TAF-ZoneD-2F-Lobby","TAF-ZoneA-1F-RoomA-01","TAF-ZoneB-2F-RoomD-01","TAF-ZoneC-4F-RoomB-01"};
    ParseUser currentUser;
    Myadapter adapter;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        pb = (ProgressBar) findViewById(R.id.progress_bar_list);
        pb.setVisibility(ProgressBar.VISIBLE);
        currentUser = ParseUser.getCurrentUser();
        adapter =new Myadapter(this);
        queryForNear();

        lstUser =(ListView)findViewById(R.id.listUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_list, menu);
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
    protected void queryForNear(){

        ParseGeoPoint userLocation = (ParseGeoPoint) currentUser.get("location");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereNear("location", userLocation);
        query.whereNotEqualTo("name", currentUser.getString("name"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    Username = new String[list.size()];
                    //Places = new String[list.size()];
                    int i = 0;
                    //check usersList availability
                    if(list.size() > 0) {
                        for (ParseObject user : list) {
                            Username[i] = user.getString("name");
                            ParseGeoPoint poi = user.getParseGeoPoint("location");
                            double lat = Math.round(poi.getLatitude() * 1000.0) / 1000.0;
                            double lon = Math.round(poi.getLongitude() * 1000.0 ) / 1000.0;
                            //Places[i] ="("+lat+","+lon+")" ;
                            i++;
                        }
                    }
                    lstUser.setAdapter(adapter);
                    pb.setVisibility(ProgressBar.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "error in query:" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public class Myadapter extends BaseAdapter {
        private LayoutInflater myInflater;
        public Myadapter(Context c) {
            myInflater =LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return Username.length;
        }

        @Override
        public Object getItem(int position) {
            return Username[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.mylistview, null);
            TextView txtUserId =((TextView)convertView.findViewById(R.id.txtUserId));
            TextView txtPlace = ((TextView)convertView.findViewById(R.id.txtPlace));
            txtUserId.setText(Username[position]);
            txtPlace.setText(Places[position]);
            return convertView;
        }
    }
}

