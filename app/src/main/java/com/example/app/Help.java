package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Help extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "helper.db";  // Renamed database
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_BOOKINGS = "bookings";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_BOOKING_DATE = "booking_date";

    public Help(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BOOKINGS = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_SERVICE_ID + " TEXT,"
                + COLUMN_BOOKING_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_BOOKINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    // Insert a new booking
    public void insertBooking(String email, String serviceId, String bookingDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SERVICE_ID, serviceId);
        values.put(COLUMN_BOOKING_DATE, bookingDate);

        db.insert(TABLE_BOOKINGS, null, values);
        db.close();
    }

    // Retrieve all bookings for a specific email
    public Cursor getBookingsForEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKINGS + " WHERE " + COLUMN_EMAIL + "=?";
        return db.rawQuery(query, new String[]{email});
    }
}
