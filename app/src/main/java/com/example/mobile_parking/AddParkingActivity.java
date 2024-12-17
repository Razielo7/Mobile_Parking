package com.example.mobile_parking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddParkingActivity extends AppCompatActivity {

    private EditText etVehicleNumber, etOwnerName, etParkingDays, etParkingNumber, etStartDate, etEndDate;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        // Initialize views
        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etOwnerName = findViewById(R.id.etOwnerName);
        etParkingDays = findViewById(R.id.etParkingDays);
        etParkingNumber = findViewById(R.id.etParkingNumber); // New field
        etStartDate = findViewById(R.id.etStartDate); // New field
        etEndDate = findViewById(R.id.etEndDate); // New field
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);
        username = getIntent().getStringExtra("username");

        // Save button click listener
        btnSave.setOnClickListener(v -> {
            String vehicleNumber = etVehicleNumber.getText().toString().trim();
            String ownerName = etOwnerName.getText().toString().trim();
            String parkingDaysStr = etParkingDays.getText().toString().trim();
            String parkingNumber = etParkingNumber.getText().toString().trim();
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();

            // Validate fields
            if (vehicleNumber.isEmpty() || ownerName.isEmpty() || parkingDaysStr.isEmpty() ||
                    parkingNumber.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int parkingDays;
            try {
                parkingDays = Integer.parseInt(parkingDaysStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid parking days", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check parking slot availability
            if (!dbHelper.isParkingAvailable(parkingNumber, startDate, endDate)) {
                Toast.makeText(this, "Parking slot not available for the selected dates", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add parking record
            long result = dbHelper.addParkingRecord(username, vehicleNumber, ownerName, parkingDays, parkingNumber, startDate, endDate);
            if (result != -1) {
                Toast.makeText(this, "Parking record added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add parking record", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
