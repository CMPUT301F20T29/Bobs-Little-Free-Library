package com.example.bobslittlefreelibrary.views.requests;

/**
 * This class is the activity for the map.
 */

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bobslittlefreelibrary.R;
import com.example.bobslittlefreelibrary.models.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int typeOfRequest;
    private Request currentRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get the type of request from the intent and store it for future use
        // 0 for being able to manipulate and add markers and 1 for view only
        // 1 default because that is where they cannot add markers to the map and manipulate it
        Intent intent = getIntent();
        typeOfRequest = intent.getIntExtra("REQUESTTYPE", 1);
        // obtain the request too from the intent
        currentRequest = (Request) intent.getSerializableExtra("REQUEST");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the default location & move the camera
        LatLng defaultLocation = new LatLng(currentRequest.getLatitude(), currentRequest.getLongitude());
        mMap.addMarker(new MarkerOptions().position(defaultLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // If the typeOfRequest is 0, we allow the user to click and move the marker on the map,
        // updating the location information as necessary
        // If it's anything else, it's just view so we don't need to do anything
        if (typeOfRequest == 0) {
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng arg0)
                {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(arg0));
                    currentRequest.setLatitude(arg0.latitude);
                    currentRequest.setLongitude(arg0.longitude);
                }
            });
        }
    }

    // When the user is done and presses back, the last location selected will be saved and
    // sent back.
    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("NEW_REQUEST", currentRequest);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }
}