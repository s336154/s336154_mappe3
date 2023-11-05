package com.example.s336154_mappe3;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// SavedPlacesActivity.java
public class SavedPlacesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        ListView savedPlacesList = findViewById(R.id.savedPlacesList);

        ArrayList<Place> savedPlaces = getIntent().getParcelableArrayListExtra("places");
        ArrayAdapter<Place> placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedPlaces);
        savedPlacesList.setAdapter(placesAdapter);
    }
}
