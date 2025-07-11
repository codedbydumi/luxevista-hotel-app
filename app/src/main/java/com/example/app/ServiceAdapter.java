package com.example.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private Context context;
    private List<Service> serviceList;
    private boolean isMyActivity;
    private String userEmail;

    public ServiceAdapter(Context context, List<Service> serviceList, boolean isMyActivity) {
        this.context = context;
        this.serviceList = serviceList;
        this.isMyActivity = isMyActivity;

        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        this.userEmail = sharedPreferences.getString("user_email", "user@example.com");
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service, isMyActivity);
        holder.bookButton.setOnClickListener(v -> bookService(service));
    }

    private void bookService(Service service) {
        new Thread(() -> {
            BookingDatabaseHelper dbHelper = new BookingDatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(BookingDatabaseHelper.COLUMN_EMAIL, userEmail);
            values.put(BookingDatabaseHelper.COLUMN_SERVICE_ID, service.getServiceId());
            values.put(BookingDatabaseHelper.COLUMN_SERVICE_NAME, service.getServiceName());
            values.put(BookingDatabaseHelper.COLUMN_SERVICE_PRICE, service.getPrice());

            long bookingId = getActiveBookingId(db);
            if (bookingId != -1) {
                values.put(BookingDatabaseHelper.COLUMN_BOOKING_ID, bookingId);
            }

            long result = db.insert(BookingDatabaseHelper.TABLE_SERVICES, null, values);
            db.close();

            ((android.app.Activity) context).runOnUiThread(() -> {
                if (result != -1) {
                    Toast.makeText(context, service.getServiceName() + " booked!", Toast.LENGTH_SHORT).show();
                    if (context instanceof services) {
                        ((services) context).setRefreshNeeded(true);
                    }
                } else {
                    Toast.makeText(context, "Booking failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private long getActiveBookingId(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(
                "SELECT " + BookingDatabaseHelper.COLUMN_ID + " FROM " +
                        BookingDatabaseHelper.TABLE_BOOKINGS + " WHERE " +
                        BookingDatabaseHelper.COLUMN_EMAIL + " = ? AND " +
                        BookingDatabaseHelper.COLUMN_BOOKING_STATUS + " = 'Active' LIMIT 1",
                new String[]{userEmail}
        );

        try {
            return cursor.moveToFirst() ? cursor.getLong(0) : -1;
        } finally {
            cursor.close();
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        TextView serviceIcon, serviceId, serviceName, serviceDescription, servicePrice;
        LinearLayout priceLayout, serviceDescriptionLayout;
        Button bookButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceIcon = itemView.findViewById(R.id.serviceIcon);
            serviceId = itemView.findViewById(R.id.serviceId);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            priceLayout = itemView.findViewById(R.id.servicePriceLayout);
            serviceDescriptionLayout = itemView.findViewById(R.id.serviceDescriptionLayout);
            bookButton = itemView.findViewById(R.id.bookButton);
        }

        void bind(Service service, boolean isBooked) {
            serviceId.setText(service.getServiceId());
            serviceName.setText(service.getServiceName());

            if (service.getImageResourceId() != 0) {
                serviceImage.setImageResource(service.getImageResourceId());
                serviceImage.setVisibility(View.VISIBLE);
                serviceIcon.setVisibility(View.GONE);
            } else {
                serviceIcon.setText(service.getServiceIcon());
                serviceIcon.setVisibility(View.VISIBLE);
                serviceImage.setVisibility(View.GONE);
            }

            serviceDescription.setText(service.getDescription());
            servicePrice.setText(String.format("$%.2f", service.getPrice()));

            if (isBooked) {
                bookButton.setText("BOOKED");
                bookButton.setEnabled(false);
                bookButton.setAlpha(0.7f);
            } else {
                bookButton.setText("BOOK NOW");
                bookButton.setEnabled(true);
                bookButton.setAlpha(1.0f);
            }
        }
    }
}