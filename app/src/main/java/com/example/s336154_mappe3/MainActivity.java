package com.example.s336154_mappe3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private GoogleMap googleMap;
    private DatabaseHelper dbHelper;
    private List<Place> savedPlaces;
    private ArrayAdapter<Place> placesAdapter;
    private Marker currentMarker;
    public String commentSaved, addressSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Button saveButton = findViewById(R.id.saveButton);
        Button viewSavedButton = findViewById(R.id.viewSavedButton);
        Button zoomINButton = findViewById(R.id.zoomINButton);
        Button zoomOUTButton = findViewById(R.id.zoomOUTButton);
        Button markAllButton = findViewById(R.id.markAllButton);
        Button unMarkAllButton = findViewById(R.id.unMarkAllButton);

        savedPlaces = new ArrayList<>();
        placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedPlaces);

        dbHelper = new DatabaseHelper(this); // Initialize your database helper

        // Check if extras are available
        if (getIntent().getExtras() != null) {
            addressSaved = getIntent().getStringExtra("address");
            commentSaved = getIntent().getStringExtra("comment");
        }

        Log.d("SavedPlace","Address is "+addressSaved+ " Comment is: " +commentSaved);

        Intent IntentSaveAct = new Intent(MainActivity.this, SaveActivity.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleMap != null) {
                    LatLng selectedPlace = currentMarker.getPosition(); // Get the position of the current marker
                    String address = getAddressFromLatLng(selectedPlace);

                    Intent IntentSaveAct = new Intent(MainActivity.this, SaveActivity.class);
                    IntentSaveAct.putExtra("longitude", selectedPlace.longitude);
                    IntentSaveAct.putExtra("latitude", selectedPlace.latitude);
                    IntentSaveAct.putExtra("address", address);
                    startActivity(IntentSaveAct);

// Remove the current marker if it exists
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }

                    // Remove the current marker if it exists
                        if (currentMarker != null) {
                            currentMarker.remove();
                        }

                        // Add a new marker for the selected place
                        currentMarker = googleMap.addMarker(new MarkerOptions()
                                .position(selectedPlace)
                                .title(""));
                    }
                }

        });


        viewSavedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SavedPlacesActivity.class);
                startActivity(intent);
            }
        });
    }


    public void zoomIn(View view) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    public void zoomOut(View view) {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }


    public void markAll(View view) {
        if (googleMap != null) {
            List<Place> savedPlaces = dbHelper.getSavedPlaces();

            // Check if there are any saved places
            if (!savedPlaces.isEmpty()) {
                for (Place place : savedPlaces) {
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getComment()));
                }
            } else {
                Toast.makeText(this, "No saved places to mark", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void unMarkAll(View view) {
        if (googleMap != null) {
            googleMap.clear();
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        LatLng oslo = new LatLng(59.9139, 10.7522);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oslo, 15));

        if (commentSaved != null || addressSaved != null) {
            addMarkerFromAddress(addressSaved, commentSaved, googleMap);
        }
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }

        currentMarker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Selected Place"));
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        StringBuilder addressStringBuilder = new StringBuilder();

        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressStringBuilder.append(address.getAddressLine(i));
                    if (i < address.getMaxAddressLineIndex()) {
                        addressStringBuilder.append(", ");
                    }
                }

                return addressStringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    private void addMarkerFromAddress(String address, String comment, GoogleMap googleMap) {
        if (googleMap != null) {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses;

            try {
                addresses = geocoder.getFromLocationName(address, 1);
                if (!addresses.isEmpty()) {
                    Address location = addresses.get(0);
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    // Create a LatLng from the latitude and longitude
                    LatLng latLng = new LatLng(latitude, longitude);

                    // Add a marker on the map with the title from the provided comment
                    currentMarker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(comment));
                } else {
                    // Handle the case where the address couldn't be resolved
                    Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}





