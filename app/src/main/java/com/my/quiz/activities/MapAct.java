package com.my.quiz.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.quiz.R;

public class MapAct extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    Marker[] marker = new Marker[2]; //change length of array according to you

    Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-33, 150);
        mMap.setOnMarkerClickListener(this);
        for(int i = 0 ; i < 2 ; i++ ) {
            if(i==0)
            {
                marker[i] = createMarker(-34, 151, "Marker in Sydney","", R.drawable.ic_loca);
            }
            else
            {
                marker[i] = createMarker(-33, 150, "Marker in Sydney","", R.drawable.ic_loca);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );

    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 9f));
        myMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
        return myMarker;

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        startActivity(new Intent(MapAct.this,PuzzleAct.class));

        return false;
    }
}