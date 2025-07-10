package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class myactivity extends AppCompatActivity {

    TextView bookedRoomTextView;
    Button back;
    RecyclerView servicesRecyclerView;
    TextView bookingsTextView;

    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Get user email from SharedPreferences or Intent
        String userEmail = getUserEmail();

        // Fetch and display booked room details
        showBookedRoom(userEmail);

        // Set up services RecyclerView
        setupServicesRecyclerView();
    }

    private void initializeViews() {
        back = findViewById(R.id.back);
        bookedRoomTextView = findViewById(R.id.bookedRoomTextView);
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);
        bookingsTextView = findViewById(R.id.bookingsTextView);
    }

    private void setupClickListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(myactivity.this, user.class);
                startActivity(i);
                finish(); // Close current activity
            }
        });
    }

    private String getUserEmail() {
        // First try to get from Intent
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        // If not found in Intent, try SharedPreferences
        if (userEmail == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            userEmail = sharedPreferences.getString("user_email", "user@example.com");
        }

        return userEmail;
    }

    private void showBookedRoom(String userEmail) {
        try {
            BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    BookingDatabaseHelper.COLUMN_ROOM_NUMBER,
                    BookingDatabaseHelper.COLUMN_START_DATE,
                    BookingDatabaseHelper.COLUMN_END_DATE
            };

            String selection = BookingDatabaseHelper.COLUMN_EMAIL + " = ?";
            String[] selectionArgs = { userEmail };

            Cursor cursor = db.query(
                    BookingDatabaseHelper.TABLE_BOOKINGS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    BookingDatabaseHelper.COLUMN_START_DATE + " DESC" // Show most recent first
            );

            if (cursor != null && cursor.moveToFirst()) {
                StringBuilder bookingInfo = new StringBuilder();

                do {
                    String roomNumber = cursor.getString(cursor.getColumnIndexOrThrow(BookingDatabaseHelper.COLUMN_ROOM_NUMBER));
                    String startDate = cursor.getString(cursor.getColumnIndexOrThrow(BookingDatabaseHelper.COLUMN_START_DATE));
                    String endDate = cursor.getString(cursor.getColumnIndexOrThrow(BookingDatabaseHelper.COLUMN_END_DATE));

                    bookingInfo.append("Room Number: ").append(roomNumber).append("\n");
                    bookingInfo.append("Check-in: ").append(startDate).append("\n");
                    bookingInfo.append("Check-out: ").append(endDate).append("\n");
                    bookingInfo.append("Status: Active").append("\n\n");

                } while (cursor.moveToNext());

                bookedRoomTextView.setText(bookingInfo.toString().trim());
            } else {
                bookedRoomTextView.setText("No active bookings found.\nBook a room to see your reservations here.");
            }

            if (cursor != null) {
                cursor.close();
            }
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            bookedRoomTextView.setText("Error loading booking information.\nPlease try again later.");
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupServicesRecyclerView() {
        serviceList = new ArrayList<>();

        // Add sample services - matching your existing Service constructor (String, String, int)
        serviceList.add(new Service("001", "Room Cleaning", R.drawable.ic_cleaning)); // Replace with your actual drawable
        serviceList.add(new Service("002", "Laundry Service", R.drawable.ic_laundry)); // Replace with your actual drawable
        serviceList.add(new Service("003", "Room Service", R.drawable.ic_room_service)); // Replace with your actual drawable
        serviceList.add(new Service("004", "Spa Services", R.drawable.ic_spa)); // Replace with your actual drawable
        serviceList.add(new Service("005", "Transportation", R.drawable.ic_transport)); // Replace with your actual drawable

        serviceAdapter = new ServiceAdapter(this, serviceList);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        servicesRecyclerView.setAdapter(serviceAdapter);

        // Hide placeholder text since we have services
        bookingsTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh booking information when returning to this activity
        String userEmail = getUserEmail();
        showBookedRoom(userEmail);
    }
}