package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class services extends AppCompatActivity {

    Button backBtn;
    RecyclerView servicesRecyclerView;
    ServiceAdapter serviceAdapter;
    List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        backBtn = findViewById(R.id.backBtn);
        servicesRecyclerView = findViewById(R.id.servicesRecyclerView);

        // Set up RecyclerView with LayoutManager and Adapter
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, serviceList);  // Pass 'this' as Context
        servicesRecyclerView.setAdapter(serviceAdapter);

        // Sample data for services with image resources from drawable
        loadServiceData();

        // Back button click listener
        backBtn.setOnClickListener(v -> finish());
    }

    private void loadServiceData() {
        // Use resource IDs for images stored in the drawable folder
        serviceList.add(new Service("001", "Room Cleaning", R.drawable.kk));
        serviceList.add(new Service("002", "Laundry", R.drawable.kk));
        serviceList.add(new Service("003", "Spa", R.drawable.kk));
        serviceList.add(new Service("004", "Gym", R.drawable.kk));
        serviceList.add(new Service("005", "Restaurant Reservation", R.drawable.kk));
        // Add more services as needed...

        serviceAdapter.notifyDataSetChanged();
    }
}