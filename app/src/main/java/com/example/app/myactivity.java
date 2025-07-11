package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class myactivity extends AppCompatActivity {
    private TextView roomNumber, roomType, checkInDate, checkOutDate, roomCost, totalCost;
    private TextView bookedRoomTextView, noServicesTextView;
    private LinearLayout noBookingsLayout;
    private RecyclerView servicesRecyclerView;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    // For lambda variable access
    private double currentRoomPrice;
    private double currentServicesTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);

        initializeViews();
        setupServicesRecyclerView();
        loadBookingData();
    }

    private void initializeViews() {
        roomNumber = findViewById(R.id.roomNumber);
        roomType = findViewById(R.id.roomType);
        checkInDate = findViewById(R.id.checkInDate);
        checkOutDate = findViewById(R.id.checkOutDate);
        roomCost = findViewById(R.id.roomCost);
        totalCost = findViewById(R.id.totalCost);
        bookedRoomTextView = findViewById(R.id.bookedRoomTextView);
        noServicesTextView = findViewById(R.id.noServicesTextView);
        noBookingsLayout = findViewById(R.id.noBookingsLayout);
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);

        Button back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            startActivity(new Intent(myactivity.this, user.class));
            finish();
        });
    }

    private void setupServicesRecyclerView() {
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, serviceList, true);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        servicesRecyclerView.setAdapter(serviceAdapter);
    }

    private void loadBookingData() {
        String userEmail = getUserEmail();
        BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(this);

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     BookingDatabaseHelper.TABLE_BOOKINGS,
                     new String[]{
                             BookingDatabaseHelper.COLUMN_ID,
                             BookingDatabaseHelper.COLUMN_ROOM_NUMBER,
                             BookingDatabaseHelper.COLUMN_ROOM_TYPE,
                             BookingDatabaseHelper.COLUMN_START_DATE,
                             BookingDatabaseHelper.COLUMN_END_DATE,
                             BookingDatabaseHelper.COLUMN_ROOM_COST
                     },
                     BookingDatabaseHelper.COLUMN_EMAIL + " = ? AND " +
                             BookingDatabaseHelper.COLUMN_BOOKING_STATUS + " = ?",
                     new String[]{userEmail, "Active"},
                     null, null,
                     BookingDatabaseHelper.COLUMN_START_DATE + " DESC",
                     "1")) {

            if (cursor.moveToFirst()) {
                showBookingDetails(cursor);
            } else {
                showNoBookingsState();
            }
        } catch (Exception e) {
            showErrorState(e.getMessage());
        }
    }

    private void showBookingDetails(Cursor cursor) {
        String roomNum = cursor.getString(1);
        String roomTypeText = cursor.getString(2);
        String startDate = formatDate(cursor.getString(3));
        String endDate = formatDate(cursor.getString(4));
        currentRoomPrice = cursor.getDouble(5); // Store in class field
        long bookingId = cursor.getLong(0);

        runOnUiThread(() -> {
            roomNumber.setText(roomNum);
            roomType.setText(roomTypeText);
            checkInDate.setText(startDate);
            checkOutDate.setText(endDate);
            roomCost.setText(String.format(Locale.getDefault(), "$%.2f", currentRoomPrice));
            bookedRoomTextView.setText("Your current booking");
            bookedRoomTextView.setVisibility(View.VISIBLE);
            noBookingsLayout.setVisibility(View.GONE);
        });

        loadBookedServices(bookingId);
    }

    private void loadBookedServices(long bookingId) {
        new Thread(() -> {
            try {
                BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(myactivity.this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        BookingDatabaseHelper.TABLE_SERVICES,
                        new String[]{
                                BookingDatabaseHelper.COLUMN_SERVICE_ID,
                                BookingDatabaseHelper.COLUMN_SERVICE_NAME,
                                BookingDatabaseHelper.COLUMN_SERVICE_PRICE
                        },
                        BookingDatabaseHelper.COLUMN_BOOKING_ID + " = ? OR " +
                                BookingDatabaseHelper.COLUMN_EMAIL + " = ?",
                        new String[]{
                                String.valueOf(bookingId),
                                getUserEmail()
                        },
                        null, null,
                        BookingDatabaseHelper.COLUMN_SERVICE_DATE + " DESC");

                serviceList.clear();
                currentServicesTotal = 0;

                while (cursor.moveToNext()) {
                    String serviceId = cursor.getString(0);
                    String serviceName = cursor.getString(1);
                    double servicePrice = cursor.getDouble(2);

                    serviceList.add(new Service(
                            serviceId,
                            serviceName,
                            getServiceIcon(serviceName),
                            "Booked service",
                            servicePrice
                    ));
                    currentServicesTotal += servicePrice;
                }

                runOnUiThread(() -> {
                    totalCost.setText(String.format(Locale.getDefault(), "$%.2f",
                            currentRoomPrice + currentServicesTotal));
                    serviceAdapter.notifyDataSetChanged();
                    noServicesTextView.setVisibility(serviceList.isEmpty() ? View.VISIBLE : View.GONE);
                });

                cursor.close();
                db.close();
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(myactivity.this, "Error loading services", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private String formatDate(String dbDate) {
        try {
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return displayFormat.format(dbFormat.parse(dbDate));
        } catch (ParseException e) {
            return dbDate;
        }
    }

    private String getServiceIcon(String serviceName) {
        if (serviceName.contains("Clean")) return "🧹";
        if (serviceName.contains("Laundry")) return "👕";
        if (serviceName.contains("Spa")) return "💆";
        if (serviceName.contains("Transport")) return "🚗";
        if (serviceName.contains("Restaurant")) return "🍽️";
        return "🛎️";
    }

    private String getUserEmail() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = getIntent().getStringExtra("USER_EMAIL");
        return email != null ? email : prefs.getString("user_email", "user@example.com");
    }

    private void showNoBookingsState() {
        runOnUiThread(() -> {
            roomNumber.setText("--");
            roomType.setText("No active booking");
            checkInDate.setText("--");
            checkOutDate.setText("--");
            roomCost.setText("$0.00");
            totalCost.setText("$0.00");
            bookedRoomTextView.setText("No active bookings found");
            bookedRoomTextView.setVisibility(View.VISIBLE);
            noBookingsLayout.setVisibility(View.VISIBLE);
            serviceList.clear();
            serviceAdapter.notifyDataSetChanged();
            noServicesTextView.setVisibility(View.GONE);
        });
    }

    private void showErrorState(String message) {
        runOnUiThread(() -> {
            roomNumber.setText("Error");
            roomType.setText("Error");
            checkInDate.setText("--");
            checkOutDate.setText("--");
            roomCost.setText("$0.00");
            totalCost.setText("$0.00");
            bookedRoomTextView.setText("Error loading booking information");
            bookedRoomTextView.setVisibility(View.VISIBLE);
            noBookingsLayout.setVisibility(View.GONE);
            Toast.makeText(myactivity.this, message, Toast.LENGTH_LONG).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookingData();
    }
}