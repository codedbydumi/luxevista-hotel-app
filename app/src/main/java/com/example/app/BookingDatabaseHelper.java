package com.example.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookingDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "booking.db";
    private static final int DATABASE_VERSION = 3;

    // Table names
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String TABLE_SERVICES = "booked_services";

    // Common columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_BOOKING_DATE = "booking_date"; // Added this column

    // Bookings columns
    public static final String COLUMN_ROOM_NUMBER = "room_number";
    public static final String COLUMN_ROOM_TYPE = "room_type";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_ROOM_COST = "room_cost";
    public static final String COLUMN_TOTAL_COST = "total_cost";
    public static final String COLUMN_BOOKING_STATUS = "booking_status";

    // Services columns
    public static final String COLUMN_SERVICE_ID = "service_id";
    public static final String COLUMN_SERVICE_NAME = "service_name";
    public static final String COLUMN_SERVICE_PRICE = "service_price";
    public static final String COLUMN_BOOKING_ID = "booking_id";
    public static final String COLUMN_SERVICE_DATE = "service_date"; // Added this column

    public BookingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_ROOM_NUMBER + " TEXT NOT NULL,"
                + COLUMN_ROOM_TYPE + " TEXT,"
                + COLUMN_START_DATE + " TEXT NOT NULL,"
                + COLUMN_END_DATE + " TEXT NOT NULL,"
                + COLUMN_ROOM_COST + " REAL,"
                + COLUMN_TOTAL_COST + " REAL,"
                + COLUMN_BOOKING_STATUS + " TEXT DEFAULT 'Active',"
                + COLUMN_BOOKING_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP)";

        String CREATE_SERVICES_TABLE = "CREATE TABLE " + TABLE_SERVICES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_SERVICE_ID + " TEXT NOT NULL,"
                + COLUMN_SERVICE_NAME + " TEXT NOT NULL,"
                + COLUMN_SERVICE_PRICE + " REAL NOT NULL,"
                + COLUMN_BOOKING_ID + " INTEGER,"
                + COLUMN_SERVICE_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(CREATE_BOOKINGS_TABLE);
        db.execSQL(CREATE_SERVICES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICES);
        onCreate(db);
    }

    public static String getRoomType(String roomNumber) {
        if (roomNumber == null) return "Standard";
        if (roomNumber.startsWith("1")) return "Standard Room";
        if (roomNumber.startsWith("2")) return "Deluxe Room";
        if (roomNumber.startsWith("3")) return "Suite";
        return "Standard Room";
    }

    public static double calculateRoomCost(String roomType, int nights) {
        double rate = 100.0; // Default rate
        if (roomType.contains("Deluxe")) rate = 150.0;
        if (roomType.contains("Suite")) rate = 200.0;
        return rate * nights;
    }
}