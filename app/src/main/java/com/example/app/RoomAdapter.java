package com.example.app;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> roomList;
    private Context context;

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

        // Set different details based on room position/number
        setRoomDetails(holder, position, room.getRoomNumber());

        // Handle the "Book" button click
        holder.bookBtn.setOnClickListener(v -> {
            showDatePickerDialog(v, room);
        });
    }

    // Method to set different details for each room
    private void setRoomDetails(RoomViewHolder holder, int position, String roomNumber) {
        switch (position % 6) { // Cycle through 6 different room types
            case 0:
                holder.roomDescription.setText("Luxurious ocean view suite with king bed, private balcony, marble bathroom, and complimentary champagne service.");
                holder.roomPrice.setText("$450 per night");
                holder.availabilityStatus.setText("Available - Premium Suite");
                break;
            case 1:
                holder.roomDescription.setText("Cozy standard room with queen bed, city view, free WiFi, and continental breakfast included.");
                holder.roomPrice.setText("$120 per night");
                holder.availabilityStatus.setText("Available - Great Value");
                break;
            case 2:
                holder.roomDescription.setText("Spacious family room with two double beds, kitchenette, living area, and kids' entertainment center.");
                holder.roomPrice.setText("$280 per night");
                holder.availabilityStatus.setText("Available - Family Special");
                break;
            case 3:
                holder.roomDescription.setText("Executive business suite with work desk, conference setup, premium internet, and 24/7 concierge.");
                holder.roomPrice.setText("$380 per night");
                holder.availabilityStatus.setText("Available - Business Class");
                break;
            case 4:
                holder.roomDescription.setText("Romantic honeymoon suite with jacuzzi, rose petals, champagne, candlelit dinner, and spa access.");
                holder.roomPrice.setText("$520 per night");
                holder.availabilityStatus.setText("Available - Romance Package");
                break;
            case 5:
                holder.roomDescription.setText("Budget-friendly single room with twin bed, shared bathroom, basic amenities, and free parking.");
                holder.roomPrice.setText("$80 per night");
                holder.availabilityStatus.setText("Available - Budget Option");
                break;
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

    // Method to show date picker dialog
    private void showDatePickerDialog(View view, Room room) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDatePicker = new DatePickerDialog(view.getContext(),
                (view1, year1, month1, day1) -> {
                    String startDate = year1 + "-" + (month1 + 1) + "-" + day1;
                    DatePickerDialog endDatePicker = new DatePickerDialog(view.getContext(),
                            (view12, year2, month2, day2) -> {
                                String endDate = year2 + "-" + (month2 + 1) + "-" + day2;
                                storeBookingInfo(room.getRoomNumber(), startDate, endDate);
                            }, year, month, day);
                    endDatePicker.show();
                }, year, month, day);

        startDatePicker.show();
    }

    // Method to store booking information in SQLite
    private void storeBookingInfo(String roomNumber, String startDate, String endDate) {
        String userEmail = "user@example.com";  // This should be retrieved dynamically, like from shared preferences or a login screen.

        BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookingDatabaseHelper.COLUMN_EMAIL, userEmail);
        values.put(BookingDatabaseHelper.COLUMN_ROOM_NUMBER, roomNumber);
        values.put(BookingDatabaseHelper.COLUMN_START_DATE, startDate);
        values.put(BookingDatabaseHelper.COLUMN_END_DATE, endDate);

        db.insert(BookingDatabaseHelper.TABLE_BOOKINGS, null, values);
        db.close();
    }
}