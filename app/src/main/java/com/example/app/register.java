package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
    dbHelper database;
    ImageButton bacBtn;
    Button signUpBtn;

    EditText fullNameTxt, emailTxt, passTxt, confirmPassTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        database = new dbHelper(this);
        bacBtn = findViewById(R.id.bacBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        fullNameTxt = findViewById(R.id.fullNameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        passTxt = findViewById(R.id.passTxt);
        confirmPassTxt = findViewById(R.id.confirmPassTxt);

        bacBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(register.this, MainActivity.class);
                startActivity(i);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullNameTxt.getText().toString();
                String email = emailTxt.getText().toString();
                String pass = passTxt.getText().toString();
                String confirmPass = confirmPassTxt.getText().toString();

                // Validate input
                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                    Toast.makeText(register.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!pass.equals(confirmPass)) {
                    Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate email format
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(register.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert user into the database
                if (database.insertUser (name, email, pass)) {
                    Toast.makeText(register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(register.this, MainActivity.class);
                    startActivity(i);
                    finish(); // Close the register activity
                } else {
                    Toast.makeText(register.this, "Email already exists. Try signing in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}