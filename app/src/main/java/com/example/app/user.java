package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class user extends AppCompatActivity {

    Button bookRooms,aboutBtn,localBtn,serviceBtn,activityBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        activityBtn = findViewById(R.id.activityBtn);
        serviceBtn = findViewById(R.id.serviceBtn);
        bookRooms = findViewById(R.id.bookRooms);
        aboutBtn = findViewById(R.id.aboutBtn);
        localBtn = findViewById(R.id.localBtn);

        activityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this, myactivity.class);
                startActivity(i);
            }
        });

        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this, services.class);
                startActivity(i);
            }
        });

        localBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this, local.class);
                startActivity(i);
            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this, aboutus.class);
                startActivity(i);
            }
        });

        bookRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(user.this, bookrooms.class);
                startActivity(i);
            }
        });
    }
}