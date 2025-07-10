package com.example.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "booking.db";
    private static final int DATABASE_VERSION = 1;

    // Table name
    public static final String TABLE_BOOKINGS = "bookings";

    // Column names
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ROOM_NUMBER = "room_number";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";



    public BookingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String CREATE_TABLE_BOOKINGS = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_ROOM_NUMBER + " TEXT,"
                + COLUMN_START_DATE + " TEXT,"
                + COLUMN_END_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_BOOKINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }
}
