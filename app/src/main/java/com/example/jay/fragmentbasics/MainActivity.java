package com.example.jay.fragmentbasics;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapswithme.maps.api.MapsWithMeApi;
import com.parse.ParseObject;


public class MainActivity extends ActionBarActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener{
    //GoogleMapOptions options = new GoogleMapOptions();
    private GoogleApiClient mGAC ;
    private GoogleMap mmap;
    private double lat = 0;
    private double lon = 0;
    double oldAccuracy = 0;
    Circle oldCircle;
    Marker oldMarker;
    private boolean flag = true;
    MapFragment mapfrag;
    Location mLastLocation;
    TextView mLatitudeText,mLongitudeText,mAccuracy;
    Button btnView;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates = true;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        mLatitudeText = (TextView)findViewById(R.id.txt1);
        mLongitudeText = (TextView)findViewById(R.id.txt2);
        btnView = (Button)findViewById(R.id.btnView);
        btnView.setOnClickListener(btnVListener);
        mAccuracy = (TextView)findViewById(R.id.txt3);
        mapfrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapfrag.getMapAsync(this);
        /**options.mapType(GoogleMap.MAP_TYPE_HYBRID)
                .compassEnabled(true)
                .rotateGesturesEnabled(true)
                .tiltGesturesEnabled(true);
        mapfrag.newInstance(options);*/
        //Connect to Google Play Services
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
            return true;
        }

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
        mmap = map;
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGAC);
        if(mRequestingLocationUpdates)startLocationUpdates();

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
        mLatitudeText.setText("Latitude: "+String.valueOf(lat));
        mLongitudeText.setText("Longitude: "+String.valueOf(lon));
        mAccuracy.setText("Accuracy: "+String.valueOf(mLastLocation.getAccuracy()));
        if (mLastLocation != null) {
            Log.d("TAG", "LastLocation is Not null");
            lat =mLastLocation.getLatitude();
            lon =mLastLocation.getLongitude();
            mLatitudeText.setText("Latitude: "+String.valueOf(lat));
            mLongitudeText.setText("Longitude: "+String.valueOf(lon));
            mAccuracy.setText("Accuracy: "+String.valueOf(mLastLocation.getAccuracy()));
            //show location with maps.me
            //if (flag)showLocationWithMap(mLastLocation);
            //Add location circle
            Log.d("TAG","Circle ~");
            Circle circle = mmap.addCircle(new CircleOptions()
                    .center(new LatLng(lat, lon))
                    .radius(mLastLocation.getAccuracy())
                    .strokeWidth(0)
                    .fillColor(Color.argb(80, 0, 0, 255)));
            Marker marker = mmap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title("Latest Location")
                    .snippet("Snippet of Marker"));
            //Update if a new more accurate position receives
            if(oldAccuracy != 0){
                oldCircle.remove();
                oldMarker.remove();
            }
            oldCircle = circle;
            oldMarker = marker;
            oldAccuracy = mLastLocation.getAccuracy();
            mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17));
            mmap.setOnInfoWindowClickListener(infoWindowClickListener);

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
            Toast.makeText(getApplicationContext(),"エラーです。Error info: "+err.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Maybe your network(WiFi or 3G/4G) was off, please turn it on and try again", Toast.LENGTH_LONG).show();
        }finally {
            //TODO
        }
        //flag = false;
    }
    private Button.OnClickListener btnVListener =new Button.OnClickListener(){
        public void onClick(View v){
            showLocationWithMap(mLastLocation);
        }
    };
    private GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener(){

        @Override
        public void onInfoWindowClick(Marker marker) {
            Toast toast = Toast.makeText(MainActivity.this, "You clicked a marker at:" +
                    " (" + String.valueOf(lat) + "," + String.valueOf(lon) + ")", Toast.LENGTH_SHORT);
            toast.show();
        }
    };
    //Get Location Updates
    protected void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10*1000); //10 seconds
        mLocationRequest.setFastestInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void startLocationUpdates(){
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGAC, mLocationRequest, this);

    }
    protected void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGAC, this);
        mRequestingLocationUpdates = false;
    }
}

