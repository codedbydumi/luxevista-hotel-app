package com.example.app;

import android.content.Context;
import android.content.Intent;
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

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
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

        // Set service details (without labels since they're in the layout)
        holder.serviceId.setText(service.getServiceId());
        holder.serviceName.setText(service.getServiceName());

        // Load the image from drawable resource
        if (service.getImageResourceId() != 0) {
            holder.serviceImage.setImageResource(service.getImageResourceId());
        } else {
            // Fallback to default image
            holder.serviceImage.setImageResource(R.drawable.ic_room_service);
        }

        // Set description if available
        String description = service.getDescription();
        if (description != null && !description.trim().isEmpty()) {
            holder.serviceDescription.setText(description);
            holder.serviceDescription.setVisibility(View.VISIBLE);
            holder.serviceDescriptionLayout.setVisibility(View.VISIBLE);
        } else {
            holder.serviceDescription.setVisibility(View.GONE);
            holder.serviceDescriptionLayout.setVisibility(View.GONE);
        }

        // Set price if available
        double price = service.getPrice();
        if (price > 0) {
            holder.servicePrice.setText(String.format("$%.2f", price));
            holder.priceLayout.setVisibility(View.VISIBLE);
        } else {
            holder.priceLayout.setVisibility(View.GONE);
        }

        // Set button text (all services are available for booking)
        holder.bookButton.setText("BOOK NOW");
        holder.bookButton.setEnabled(true);
        holder.bookButton.setAlpha(1.0f);

        // Set click listener for book button
        holder.bookButton.setOnClickListener(v -> {
            // Handle booking logic here
            Toast.makeText(context, "Booking " + service.getServiceName() + " - Coming Soon!", Toast.LENGTH_SHORT).show();

            // TODO: Create BookingActivity and uncomment the lines below
            /*
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("SERVICE_ID", service.getServiceId());
            intent.putExtra("SERVICE_NAME", service.getServiceName());
            intent.putExtra("SERVICE_PRICE", service.getPrice());
            intent.putExtra("SERVICE_DESCRIPTION", service.getDescription());
            context.startActivity(intent);
            */
        });

        // Set click listener for the entire card
        holder.itemView.setOnClickListener(v -> {
            // Handle card click - maybe show service details
            Toast.makeText(context, "Selected: " + service.getServiceName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        TextView serviceId, serviceName, serviceDescription, servicePrice;
        LinearLayout priceLayout, serviceDescriptionLayout;
        Button bookButton;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceId = itemView.findViewById(R.id.serviceId);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            priceLayout = itemView.findViewById(R.id.servicePriceLayout);
            serviceDescriptionLayout = itemView.findViewById(R.id.serviceDescriptionLayout);
            bookButton = itemView.findViewById(R.id.bookButton);
        }
    }
}