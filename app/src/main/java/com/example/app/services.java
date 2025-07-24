package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class services extends AppCompatActivity {
    private boolean refreshNeeded = false;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Button backBtn = findViewById(R.id.backBtn);
        RecyclerView servicesRecyclerView = findViewById(R.id.servicesRecyclerView);

        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(this, serviceList, false);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        servicesRecyclerView.setAdapter(serviceAdapter);

        loadServiceData();

        backBtn.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("refreshNeeded", refreshNeeded);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    public void setRefreshNeeded(boolean needed) {
        this.refreshNeeded = needed;
    }

    private void loadServiceData() {
        serviceList.clear();
        serviceList.add(new Service("001", "Room Cleaning", R.drawable.ic_cleaning,
                "Daily professional cleaning", 25.00));
        serviceList.add(new Service("002", "Laundry Service", R.drawable.ic_laundry,
                "Same-day laundry service", 35.00));
        serviceList.add(new Service("003", "Spa Services", R.drawable.ic_spa,
                "Relaxing spa treatments", 80.00));
        serviceList.add(new Service("004", "Transportation", R.drawable.ic_transport,
                "Airport transfers", 45.00));
        serviceList.add(new Service("005", "Restaurant", R.drawable.ic_restaurant,
                "Priority reservations", 15.00));

        serviceAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            refreshNeeded = data.getBooleanExtra("refreshNeeded", false);
        }
    }
}