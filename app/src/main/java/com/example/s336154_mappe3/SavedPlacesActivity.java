package com.example.s336154_mappe3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SavedPlacesActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    private ArrayAdapter<Place> placesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        dbHelper = new DatabaseHelper(this);

        ListView savedPlacesList = findViewById(R.id.savedPlacesList);

        List<Place> savedPlaces = dbHelper.getSavedPlaces();
        placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedPlaces);
        savedPlacesList.setAdapter(placesAdapter);

        savedPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place selectedItem = savedPlaces.get(position);

                Intent intent = new Intent(SavedPlacesActivity.this, MainActivity.class);
                intent.putExtra("address", selectedItem.getAddress());
                intent.putExtra("comment", selectedItem.getComment());
                startActivity(intent);
            }
        });
    }
}
