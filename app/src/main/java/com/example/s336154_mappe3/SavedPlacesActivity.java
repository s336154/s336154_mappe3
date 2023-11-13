package com.example.s336154_mappe3;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

// SavedPlacesActivity.java
public class SavedPlacesActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        dbHelper = new DatabaseHelper(this);

        ListView savedPlacesList = findViewById(R.id.savedPlacesList);
        Button viewPlaceButt = findViewById(R.id.viewButton);

    //    ArrayList<Place> savedPlaces = getIntent().getParcelableArrayListExtra("places");
        List<Place> savedPlaces = dbHelper.getSavedPlaces();
        ArrayAdapter<Place> placesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedPlaces);
        savedPlacesList.setAdapter(placesAdapter);

        Intent IntentMain = new Intent(this, MainActivity.class);


        savedPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Place selectedItem = (Place) savedPlaces.get(position);

                Log.d("DebugAddress", "Address selected is: " + String.valueOf(selectedItem.getAddress()) +
                        "  Place name is: " + String.valueOf(selectedItem.getPlace()));



           //     Log.d("meeting Id", "ID is: " + String.valueOf(meetingId));

                viewPlaceButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = selectedItem.getAddress();
                        String comment = selectedItem.getComment();
                        IntentMain.putExtra("address", address);
                        IntentMain.putExtra("comment", comment);
                        Log.d("DebugAddress", "Address selected is: " + String.valueOf(selectedItem.getAddress()) +
                                "  Place name is: " + String.valueOf(selectedItem.getPlace()));
                        startActivity(IntentMain);

                    }
                });
            }
        });
    }
}
