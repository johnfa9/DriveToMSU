package com.ga.drivetomsu;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapCallback {

    private GoogleMap mMap;
    private MarkerOptions starting;
    private MarkerOptions ending;
    Button btGetDirections;
    Polyline tripPolyline;

    public static final CameraPosition HOME = new CameraPosition.Builder()
            .target(new LatLng(40.93487, -74.11806)).zoom(15.5f).bearing(0).tilt(25).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Determine when  the map is available
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btGetDirections = findViewById(R.id.btnDirection);


        //starting address
        starting = new MarkerOptions().position(new LatLng(40.93487, -74.11806)).title("Location 1");
        //Montclair ending address
        ending = new MarkerOptions().position(new LatLng(40.86737, -74.19860)).title("Location 2");

        //Get directions when user presses button
        btGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl(starting.getPosition(), ending.getPosition(), "driving");
                new FetchURL(MapsActivity.this).execute(url, "driving");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(HOME));
        //navigate to markers in map
        mMap.addMarker(starting);
        mMap.addMarker(ending);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
//creating driving URL

        String key = "AIzaSyDVtvZBeHnknYmnLFtqAwbqs8XH2vddXcc";
        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;
        return url;
    }

    @Override
    public void onComplete(Object... values) {
        //add/remove polyline
        if (tripPolyline != null)
            tripPolyline.remove();
        tripPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}