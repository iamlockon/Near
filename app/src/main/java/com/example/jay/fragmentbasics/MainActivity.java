package com.example.jay.fragmentbasics;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapswithme.maps.api.MapsWithMeApi;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener{
    //GoogleMapOptions options = new GoogleMapOptions();
    private GoogleApiClient mGAC ;
    private GoogleMap mmap;
    private double lat = 0;
    private double lon = 0;
    double oldAccuracy = 800;
    Circle circle;
    Marker marker;
    private boolean flag = true;
    MapFragment mapfrag;
    Location mLastLocation;
    TextView mLatitudeText,mLongitudeText,mAccuracy;
    Button btnView,btnShow;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = true;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    //Parse User registration
    ParseUser user;
    ParseGeoPoint point;
    boolean isOtherUserInitialized = false;
    ArrayList<Marker> myMarkersList = new ArrayList<Marker>();
    boolean isShowOtherUsersOk = false;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mLatitudeText = (TextView)findViewById(R.id.txt1);
        //mLongitudeText = (TextView)findViewById(R.id.txt2);
        //btnView = (Button)findViewById(R.id.btnView);
        //btnView.setOnClickListener(btnVListener);
        //mAccuracy = (TextView)findViewById(R.id.txt3);
        mapfrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapfrag.getMapAsync(this);
        /**options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);
        mapfrag.newInstance(options);*/
        pb = (ProgressBar) findViewById(R.id.progress_bar);
        //Connect to Google Play Services
        pb.setVisibility(ProgressBar.VISIBLE);
        buildGoogleApiClient();
        createLocationRequest();
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(!mResolvingError){
            mGAC.connect();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        stopLocationUpdates();
    }
    @Override
    protected void onStop(){
        super.onStop();
        mGAC.disconnect();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(mGAC.isConnected() && !mRequestingLocationUpdates)startLocationUpdates();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            try {
                showLocationWithMap(mLastLocation);
            }catch(Exception err){
                Toast.makeText(getApplicationContext(), "Error: "+err.toString(),Toast.LENGTH_LONG).show();
            }finally {

            }
            return true;
        }
        if(id ==R.id.update_location)setOtherUserOnMap(user);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        UiSettings Ui = map.getUiSettings();
        Ui.setMyLocationButtonEnabled(true);
        Ui.setZoomControlsEnabled(true);
        Ui.setCompassEnabled(true);
        Ui.setMapToolbarEnabled(true);
        Ui.setAllGesturesEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        mmap = map;
        //Initialize User's circle and marker
        circle = mmap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lon))
                .strokeWidth(0)
                .fillColor(Color.argb(80, 0, 0, 255)));
        /*marker = mmap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Latest Location")
                .snippet("default"));*/
    }
    protected synchronized void buildGoogleApiClient() {

        mGAC = new GoogleApiClient.Builder(this)
             .addConnectionCallbacks(this)
             .addOnConnectionFailedListener(this)
             .addApi(LocationServices.API)
             .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Connected to Google Play services!
        // The good stuff goes here.
        Log.d("TAG", "Connect!");
        //Get Location!
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGAC);
            if (mRequestingLocationUpdates) startLocationUpdates();
        }catch(NullPointerException NPE){
            Log.d("TAG","NPE detected!");
        }catch(Exception er){
            Log.d("TAG","error: "+er.toString());
        }finally{

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
        Log.d("TAG", "Connect Suspended!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // This callback is important for handling errors that
        // may occur while attempting to connect with Google.
        //
        // More about this in the 'Handle Connection Failures' section.
        Log.d("TAG","Connect Failed!");
        if (mResolvingError) {
            // Already attempting to resolve an error.

        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGAC.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }
    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        lat =mLastLocation.getLatitude();
        lon =mLastLocation.getLongitude();
        // on some click or some loading we need to wait for...
        // run a background job and once complete
        //mLatitudeText.setText("Latitude: "+String.valueOf(lat));
        //mLongitudeText.setText("Longitude: "+String.valueOf(lon));
        //mAccuracy.setText("Accuracy: "+String.valueOf(mLastLocation.getAccuracy()));
        if (mLastLocation != null) {
            Log.d("TAG", "LastLocation is Not null");
            lat =mLastLocation.getLatitude();
            lon =mLastLocation.getLongitude();
            //mLatitudeText.setText("Latitude: "+String.valueOf(lat));
            //mLongitudeText.setText("Longitude: "+String.valueOf(lon));
            //mAccuracy.setText("Accuracy: "+String.valueOf(mLastLocation.getAccuracy()));
            Toast.makeText(getApplicationContext(), String.valueOf(lat)+","+String.valueOf(lon),Toast.LENGTH_SHORT).show();

            //Set Location Accuracy and User location
            circle.setRadius(mLastLocation.getAccuracy());
            circle.setCenter(new LatLng(lat, lon));
            //marker.setPosition(new LatLng(lat, lon));
            //Set user's view
            if(!isShowOtherUsersOk)mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17));
            mmap.setOnInfoWindowClickListener(infoWindowClickListener);
            //Send User Location to Parse
            point = new ParseGeoPoint(lat, lon);
            user = ParseUser.getCurrentUser();
            if (user != null) {
                // do stuff with the user
                Toast.makeText(getApplicationContext(), "Get Current User",Toast.LENGTH_SHORT).show();
                user.put("location", point);
                user.saveInBackground();
                isShowOtherUsersOk = true;
                oldAccuracy =mLastLocation.getAccuracy();
                if(oldAccuracy < 25){
                    stopLocationUpdates();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }
            } else {
                // show the signup or login screen
                Toast.makeText(getApplicationContext(),"Cannot configure user status....",Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(MainActivity.this, ParseLoginActivity.class);
                startActivity(intent);
            }
        }
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity)getActivity()).onDialogDismissed();
        }
    }
    void showLocationWithMap(Location location){
        //For users who have no Network accessible
        //Ask maps.me to show location
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            final String name = location.getProvider();
            MapsWithMeApi.showPointOnMap(this, lat, lon, name);
        }catch(Exception err){
            Toast.makeText(getApplicationContext(),"Error!Error info: "+err.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Maybe your network(WiFi or 3G/4G) was off, please turn it on and try again", Toast.LENGTH_LONG).show();
        }finally {
            //TODO
        }
        //flag = false;
    }
    /*private Button.OnClickListener btnVListener =new Button.OnClickListener(){
        public void onClick(View v){
            showLocationWithMap(mLastLocation);
        }
    }*/
    private GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener(){

        @Override
        public void onInfoWindowClick(Marker marker) {
            Toast toast = Toast.makeText(MainActivity.this, "You clicked a marker at:" +
                    " (" + String.valueOf(lat) + "," + String.valueOf(lon) + ")", Toast.LENGTH_SHORT);
            toast.show();
        }
    };
    //Get Location Updates
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10*1000); //10 seconds
        mLocationRequest.setFastestInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGAC, mLocationRequest, this);

    }
    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGAC, this);
        mRequestingLocationUpdates = false;
    }

    protected void addMarkerOfOtherUser(ParseObject object) {
        ParseGeoPoint poi = object.getParseGeoPoint("location");
        Marker mar = mmap.addMarker(new MarkerOptions()
                    .position(new LatLng(poi.getLatitude(), poi.getLongitude()))
                    .title(object.getString("name"))
                    .snippet("default snippet"));
            myMarkersList.add(mar);
            object.put("markerId",mar.getId());
            object.saveInBackground();
    }
    protected void setMarkerOfOtherUser(ParseObject use, Marker m){
        ParseGeoPoint geoPoint = use.getParseGeoPoint("location");
        double lat = geoPoint.getLatitude();
        double lon = geoPoint.getLongitude();
        m.setPosition(new LatLng(lat, lon));
    }
    protected void setOtherUserOnMap(ParseObject userObject){
        startLocationUpdates();
        ParseGeoPoint userLocation = (ParseGeoPoint) userObject.get("location");
        //get queries of Users' locations...
        //The user class is not "User" but "_User".....WTF
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        Toast.makeText(getApplicationContext(), "setOther!~", Toast.LENGTH_SHORT).show();
        query.whereNear("location", userLocation);
        query.whereNotEqualTo("name",userObject.getString("name"));
        //set 10 nearest user as a limit.
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), "error.....", Toast.LENGTH_LONG).show();
                } else {
                    //Set user locations on Map
                    Toast.makeText(getApplicationContext(), "no error", Toast.LENGTH_SHORT).show();
                    //myMarkersList = new ArrayList<Marker>(list.size());
                    //Handle users respectively
                    for (ParseObject use : list) {
                        //Initialize other users' marker if haven't
                        if (!isOtherUserInitialized) {
                            addMarkerOfOtherUser(use);
                        }
                    }
                    //Stop adding marker from now on
                    isOtherUserInitialized = true;
                    //Now each user has one marker.
                    //So let's update their position
                    //get Marker id from Parse(don't know which marker is of current user)
                    for (ParseObject use : list) {
                        int i = 0;
                        String str = use.getString("markerId");
                        //Find marker, if right >>break
                        while (i < list.size()) {
                            if (myMarkersList.get(i).getId().equals(str)) {
                                setMarkerOfOtherUser(use, myMarkersList.get(i));
                                break;
                            }
                            i++;
                        }
                    }
                    stopLocationUpdates();
                }
            }
        });
    }
}

