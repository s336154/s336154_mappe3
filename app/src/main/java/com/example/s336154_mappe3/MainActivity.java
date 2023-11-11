package com.example.s336154_mappe3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

// MainActivity.java

    public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

        private GoogleMap googleMap;
    private EditText placeNameEditText;
    private DatabaseHelper dbHelper;
    private List<Place> savedPlaces;
    private ArrayAdapter<Place> placesAdapter;

        private Marker currentMarker; // To keep track of the current marker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        MapView mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        EditText placeNameEditText = findViewById(R.id.placeName);
        Button saveButton = findViewById(R.id.saveButton);
        Button viewSavedButton = findViewById(R.id.viewSavedButton);
        Button zoomINButton = findViewById(R.id.zoomINButton);
        Button zoomOUTButton = findViewById(R.id.zoomOUTButton);

        savedPlaces = new ArrayList<>();
        placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedPlaces);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleMap != null) {
                    LatLng selectedPlace = googleMap.getCameraPosition().target;
                    String placeName = placeNameEditText.getText().toString().trim();

                    if (!placeName.isEmpty()) {
                        long result = dbHelper.insertPlace(placeName, selectedPlace.latitude, selectedPlace.longitude);

                        if (result != -1) {
                            Toast.makeText(MainActivity.this, "Place saved to database", Toast.LENGTH_SHORT).show();
                            savedPlaces.add(new Place(placeName, selectedPlace.latitude, selectedPlace.longitude));
                            placesAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to save place", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter a place name", Toast.LENGTH_SHORT).show();
                    }

                    // Remove the current marker if it exists
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }

                    // Add a new marker for the selected place
                    currentMarker = googleMap.addMarker(new MarkerOptions()
                            .position(selectedPlace)
                            .title(placeName));
                }
            }
        });

        viewSavedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SavedPlacesActivity.class);
                intent.putParcelableArrayListExtra("places", (ArrayList<? extends Parcelable>) savedPlaces);
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
        @Override
        public void onMapReady(GoogleMap map) {
            googleMap = map;
            LatLng oslo = new LatLng(59.9139, 10.7522);

            // Set the camera position to Oslo with a zoom level (you can adjust the zoom level as needed)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oslo, 15)); // The number 15 represents the zoom level

          //  googleMap.addMarker(new MarkerOptions().position(oslo).title("Marker in Oslo"));
            googleMap.setOnMapClickListener(this);
        }

        @Override
        public void onMapClick(LatLng latLng) {
            // Remove the current marker when the map is clicked
            if (currentMarker != null) {
                currentMarker.remove();
            }

            // Add a new marker at the clicked location
            currentMarker = googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Selected Place"));
        }
}

