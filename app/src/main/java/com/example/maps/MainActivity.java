package com.example.maps;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maps.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMainBinding binding;
    private GoogleMap gMap;
    private ArrayList<LatLng> markersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createGMap();
        setUpListeners();
    }

    private void setUpListeners() {
        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (gMap != null){
                     gMap.clear();
                 }
            }
        });

        binding.btnPolyline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gMap != null && markersList.size() > 1) {
                    gMap.addPolyline(
                            new PolylineOptions()
                                    .addAll(markersList)
                    );
                    gMap.animateCamera(animateCamera(
                            markersList.get(0),
                            4F,
                            45F,
                            10F
                    ));
                    gMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                        @Override
                        public void onPolylineClick(@NonNull Polyline polyline) {
                            polyline.setColor(Color.GREEN);
                        }
                    });
                }else {
                    Snackbar.make(binding.getRoot(), "Добавьте еще маркеры", BaseTransientBottomBar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createGMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private CameraUpdate animateCamera(LatLng target, Float zoom, Float bearing, Float tilt) {
        return CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder()
                        .target(target)
                        .zoom(8F)
                        .bearing(90F)
                        .tilt(45F)
                        .build()
        );
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        MarkerOptions markerOptions = new MarkerOptions();

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                markerOptions.position(latLng);
                markerOptions.title(latLng.toString());
                markerOptions.draggable(true);
                markersList.add(latLng);
                gMap.addMarker(markerOptions);

                gMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                                CameraPosition.builder()
                                .target(latLng)
                                .zoom(8F)
                                .bearing(90F)
                                .tilt(45F)
                                .build()
                        )
                );
            }
        });

        gMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                Log.e("TAG", "onMarkerDrag: " + marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                Log.e("TAG", "onMarkerEnd: " + marker.getPosition());

            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                Log.e("TAG", "onMarkerStart: " + marker.getPosition());
            }
        });

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                return false;
            }
        });
    }
}