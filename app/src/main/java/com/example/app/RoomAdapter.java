package com.example.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> roomList;
    private Context context;  // Now properly recognized with the import

    public RoomAdapter(List<Room> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.roomNumber.setText(room.getRoomNumber());
        setRoomDetails(holder, position, room.getRoomNumber());

        holder.bookBtn.setOnClickListener(v -> showDatePickerDialog(v, room));
    }

    private void setRoomDetails(RoomViewHolder holder, int position, String roomNumber) {
        String roomType = BookingDatabaseHelper.getRoomType(roomNumber);
        double basePrice = roomType.contains("Deluxe") ? 150 : roomType.contains("Suite") ? 200 : 100;

        holder.roomDescription.setText(getRoomDescription(roomType));
        holder.roomPrice.setText(String.format(Locale.getDefault(), "$%.2f per night", basePrice));
        holder.availabilityStatus.setText("Available - " + roomType);
    }

    private String getRoomDescription(String roomType) {
        switch (roomType) {
            case "Deluxe Room": return "Spacious room with premium amenities";
            case "Suite": return "Luxurious suite with separate living area";
            default: return "Comfortable standard room with all basic amenities";
        }
    }

    private void showDatePickerDialog(View view, Room room) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(context,
                (startView, startYear, startMonth, startDay) -> {
                    String startDate = String.format(Locale.getDefault(), "%d-%d-%d", startYear, startMonth+1, startDay);
                    new DatePickerDialog(context,
                            (endView, endYear, endMonth, endDay) -> {
                                String endDate = String.format(Locale.getDefault(), "%d-%d-%d", endYear, endMonth+1, endDay);
                                storeBookingInfo(room.getRoomNumber(), startDate, endDate);
                            }, year, month, day).show();
                }, year, month, day).show();
    }

    private void storeBookingInfo(String roomNumber, String startDate, String endDate) {
        new Thread(() -> {
            try {
                SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String userEmail = prefs.getString("user_email", "user@example.com");

                BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String roomType = BookingDatabaseHelper.getRoomType(roomNumber);
                int nights = calculateNights(startDate, endDate);
                double roomCost = BookingDatabaseHelper.calculateRoomCost(roomType, nights);

                ContentValues values = new ContentValues();
                values.put(BookingDatabaseHelper.COLUMN_EMAIL, userEmail);
                values.put(BookingDatabaseHelper.COLUMN_ROOM_NUMBER, roomNumber);
                values.put(BookingDatabaseHelper.COLUMN_ROOM_TYPE, roomType);
                values.put(BookingDatabaseHelper.COLUMN_START_DATE, startDate);
                values.put(BookingDatabaseHelper.COLUMN_END_DATE, endDate);
                values.put(BookingDatabaseHelper.COLUMN_ROOM_COST, roomCost);
                values.put(BookingDatabaseHelper.COLUMN_TOTAL_COST, roomCost);

                long result = db.insert(BookingDatabaseHelper.TABLE_BOOKINGS, null, values);

                ((Activity) context).runOnUiThread(() -> {
                    if (result != -1) {
                        Toast.makeText(context, roomType + " booked successfully!", Toast.LENGTH_SHORT).show();
                        ((Activity) context).recreate();
                    } else {
                        Toast.makeText(context, "Booking failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int calculateNights(String startDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long diff = format.parse(endDate).getTime() - format.parse(startDate).getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNumber, roomDescription, roomPrice, availabilityStatus;
        Button bookBtn;

        public RoomViewHolder(View itemView) {
            super(itemView);
            roomNumber = itemView.findViewById(R.id.roomNumber);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            availabilityStatus = itemView.findViewById(R.id.availabilityStatus);
            bookBtn = itemView.findViewById(R.id.bookBtn);
        }
    }
}