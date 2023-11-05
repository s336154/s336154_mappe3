package com.example.s336154_mappe3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;


// DatabaseHelper.java
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "places.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_PLACES = "places";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_PLACES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);
        onCreate(db);
    }


    public long insertPlace(String name, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        return db.insert(TABLE_PLACES, null, values);
    }

    public List<Place> getSavedPlaces() {
        List<Place> places = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE};
        Cursor cursor = db.query(TABLE_PLACES, columns, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
                    places.add(new Place(name, latitude, longitude));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return places;
    }
}
