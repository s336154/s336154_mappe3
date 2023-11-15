package com.example.s336154_mappe3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "places.db";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_PLACES = "places";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_PLACES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLACE + " TEXT, " +
                COLUMN_COMMENT + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }

    public long insertPlace(String place, String comment, String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLACE, place);
        values.put(COLUMN_COMMENT, comment);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        return db.insert(TABLE_PLACES, null, values);
    }

    public long addPlace(String address, String comment) {
        // Replace the placeholders with actual values
        String place = "Your Place Name";  // You need to specify a place name here
        double latitude = 0.0; // Set the latitude of the place
        double longitude = 0.0; // Set the longitude of the place

        // Insert the place into the database
        return insertPlace(place, comment, address, latitude, longitude);
    }

    public List<Place> getSavedPlaces() {
        List<Place> places = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_PLACE, COLUMN_COMMENT, COLUMN_ADDRESS, COLUMN_LATITUDE, COLUMN_LONGITUDE};
        Cursor cursor = db.query(TABLE_PLACES, columns, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String place = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACE));
                    String comment = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT));
                    String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                    places.add(new Place(place, comment, address, latitude, longitude));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return places;
    }
}
