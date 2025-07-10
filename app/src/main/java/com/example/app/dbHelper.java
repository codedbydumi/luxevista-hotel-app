package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String dbName = "hello.db";
    private static final int dbVersion = 1;
    private static final String table = "users";
    private static final String id = "id";
    private static final String userName = "userName";
    private static final String userEmail = "userEmail";
    private static final String userPass = "userPassword";
    private static final String userType = "userType";

    public dbHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Fixed the syntax error with missing comma
        String query =
                "CREATE TABLE " + table +
                        " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        userName + " TEXT, " +
                        userEmail + " TEXT, " +
                        userPass + " TEXT, " + // Added missing comma
                        userType + " TEXT);"; // Corrected closing parenthesis
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table);
        onCreate(db); // Recreate the table on upgrade
    }

    // Checks if the email already exists
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            cursor = db.query(table, new String[]{userEmail}, userEmail + "=?", new String[]{email}, null, null, null);
            exists = (cursor != null && cursor.getCount() > 0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return exists;
    }

    // Insert user into the database
    public boolean insertUser(String name, String email, String password) {
        if (emailExists(email)) {
            return false; // Email already exists
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(userName, name);
        values.put(userEmail, email);
        values.put(userPass, password);

        // Ensure proper handling of database insert operation
        long result = db.insert(table, null, values);
        db.close(); // Close the database after operation

        return result != -1; // Return true if insertion was successful
    }
}
