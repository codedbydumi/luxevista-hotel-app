package com.example.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class local extends AppCompatActivity {

    ImageButton back;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            Intent i = new Intent(local.this, user.class);
            startActivity(i);
        });

        // Adding click listeners for each card to show the popup
        findViewById(R.id.cardViewYala).setOnClickListener(v -> showInfoPopup("Yala National Park", "Yala National Park is famous for its wildlife safaris, offering a chance to see elephants, leopards, and more."));
        findViewById(R.id.cardViewKataragama).setOnClickListener(v -> showInfoPopup("Kataragama Temple", "The Kataragama Temple is an important religious site for Buddhists and Hindus, located in a peaceful and spiritual area."));
        findViewById(R.id.cardViewTissa).setOnClickListener(v -> showInfoPopup("Tissamaharama Lake", "Tissamaharama Lake offers scenic boat rides and birdwatching opportunities, perfect for nature lovers."));
    }

    // Method to show the info popup
    private void showInfoPopup(String title, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(local.this);
        builder.setTitle(title)
                .setMessage(description)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())  // Close the dialog when the button is clicked
                .create()
                .show();
    }
}
