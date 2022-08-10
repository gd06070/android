package org.techtown.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gMap;
    // The entry point to the Places API.
    private FusedLocationProviderClient client;
    private final LatLng defaultLocation = new LatLng(35.15691, 129.05606);
    private static final int DEFAULT_ZOOM = 15;
    double latitude, longitude;

    Button btn, gps_btn, refresh_btn;
    MarkerOptions option;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"안녕",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), QR_Activity.class);
                startActivity(intent);
            }
        });

        refresh_btn = (Button) findViewById(R.id.refresh_btn);

        gps_btn = (Button) findViewById(R.id.gps_btn);
        gps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        //부산 롯백 본점 : 35.15691°N, 129.05606
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
//        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
//        option = new MarkerOptions().position(defaultLocation).title("부산 롯데백화점");
//        gMap.addMarker(option);
        getCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        client.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Location location = task.getResult();
                gotoLocation(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void gotoLocation(double latitude, double longtitude) {
        LatLng latLng = new LatLng(latitude, longtitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM);
        gMap.moveCamera(cameraUpdate);
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        option = new MarkerOptions().position(latLng).title("현재 위치");
        gMap.addMarker(option);
    }
}