package com.example.s336154_mappe3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class SaveActivity extends AppCompatActivity {

private GoogleMap googleMap;
private EditText placeEditText, commentEditText;
private DatabaseHelper dbHelper;

private List<Place> savedPlaces;
private ArrayAdapter<Place> placesAdapter;
 private Marker currentMarker; // To keep track of the current marker
         ArrayList<String> addressList;

        @Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        dbHelper = new DatabaseHelper(this);


        placeEditText = findViewById(R.id.placeName);
        commentEditText = findViewById(R.id.placeComment);
        Button backButt = findViewById(R.id.backButton);
        Button saveButt = findViewById(R.id.saveButton);

        savedPlaces = new ArrayList<>();
        placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedPlaces);



        String address = getIntent().getExtras().getString("address", null);
       // addressList = getIntent().getExtras().getStringArrayList("addressList");

                double longitude = getIntent().getExtras().getDouble("longitude", 0);
                double latitude = getIntent().getExtras().getDouble("latitude", 0);

// ... Rest of your code


                int position =  getIntent().getExtras().getInt("position", 0);
        String contactName = getIntent().getExtras().getString("contactName", null);
                Intent IntentMain = new Intent(this, MainActivity.class);


 //       LatLng selectedPlace=googleMap.getCameraPosition().target;


        Log.d("addressList",address);


                saveButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        String place = placeEditText.getText().toString().trim(); // Fetch place value here
                        if (!place.isEmpty()) {
                                String comment = commentEditText.getText().toString().trim();
                                Log.d("DebugAddress", "Place: " + place);
                                Log.d("DebugAddress", "Comment: " + comment);
                                Log.d("DebugAddress", "Address: " + address);
                                Log.d("DebugAddress", "Longitude: " + longitude);
                                Log.d("DebugAddress", "Latitude: " + latitude);

                                long result = dbHelper.insertPlace(place, comment, address, latitude, longitude);

                                Log.d("addressList",String.valueOf(result));

                                if (result != -1) {
                                        Toast.makeText(SaveActivity.this, "Place saved to database", Toast.LENGTH_SHORT).show();
                                        savedPlaces.add(new Place(place, comment, address, latitude, longitude));
                                        placesAdapter.notifyDataSetChanged();
                                        placeEditText.setText("");
                                        commentEditText.setText("");
                                        startActivity(IntentMain);
                                        } else {
                                        Toast.makeText(SaveActivity.this, "Failed to save place", Toast.LENGTH_SHORT).show();
                                        }
                                } else {
                                        Toast.makeText(SaveActivity.this, "Please enter a place name", Toast.LENGTH_SHORT).show();
                                }
                        }
                        });




                backButt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                startActivity(IntentMain);
                        }
                });

        }
        }

