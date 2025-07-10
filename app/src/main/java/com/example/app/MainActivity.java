package com.example.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText emailTxt, passTxt;
    Button signInBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = findViewById(R.id.signInBtn);
        passTxt = findViewById(R.id.passTxt);
        emailTxt = findViewById(R.id.emailTxt);
        loginBtn = findViewById(R.id.loginBtn); // Initialize loginBtn

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, register.class);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String pass = passTxt.getText().toString();

                // Validate input
                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the user exists in the database
                dbHelper database = new dbHelper(MainActivity.this);
                SQLiteDatabase db = database.getReadableDatabase();
                Cursor cursor = db.query("users", new String[]{"userPassword"}, "userEmail=?", new String[]{email}, null, null, null);

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // Get the stored password
                        int passwordIndex = cursor.getColumnIndex("userPassword");
                        if (passwordIndex != -1) { // Check if the index is valid
                            String storedPassword = cursor.getString(passwordIndex);
                            if (storedPassword.equals(pass)) {
                                Intent i = new Intent(MainActivity.this, user.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error retrieving password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "User  not found. Please register.", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close(); // Close the cursor
                }
            }
        });
    }
}