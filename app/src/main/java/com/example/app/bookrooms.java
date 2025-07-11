package com.example.app;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class bookrooms extends AppCompatActivity {

    Button backBtn;
    RecyclerView roomsRecyclerView;
    RoomAdapter roomAdapter;
    List<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookrooms);

        backBtn = findViewById(R.id.backBtn);
        roomsRecyclerView = findViewById(R.id.roomsRecyclerView);

        // Set up RecyclerView with LayoutManager and Adapter
        roomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roomList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomList, this);
        roomsRecyclerView.setAdapter(roomAdapter);

        // Sample data for rooms with image resource IDs
        loadRoomData();

        // Back button click listener
        backBtn.setOnClickListener(v -> finish());
    }

    private void loadRoomData() {
        // Add room data with image resource IDs
        roomList.add(new Room("101", "Yes", "High", R.drawable.ic_restaurant));
        roomList.add(new Room("102", "No", "Medium", R.drawable.ic_restaurant));
        roomList.add(new Room("103", "Yes", "Luxury", R.drawable.ic_restaurant));
        roomList.add(new Room("104", "No", "Basic", R.drawable.ic_restaurant));
        roomList.add(new Room("105", "Yes", "High", R.drawable.ic_restaurant));
        // Add more rooms as needed...

        roomAdapter.notifyDataSetChanged();
    }
}
