package com.example.mobile_parking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddParkingActivity extends AppCompatActivity {

    public EditText etVehicleNumber, etOwnerName, etParkingDays, etParkingNumber, etStartDate, etEndDate; // UserInterface component that will enterned to be saved in the dashboard
    public Button btnSave; //save the input
    public DatabaseHelper dbHelper; // connect to database
    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        // Initialize views
        etVehicleNumber = findViewById(R.id.etVehicleNumber);// input vehicle number
        etOwnerName = findViewById(R.id.etOwnerName);// input owner name
        etParkingDays = findViewById(R.id.etParkingDays);// input parking day
        etParkingNumber = findViewById(R.id.etParkingNumber); // input parking number
        etStartDate = findViewById(R.id.etStartDate); //  input start day
        etEndDate = findViewById(R.id.etEndDate); // input end day
        btnSave = findViewById(R.id.btnSave);// button to save th input

        dbHelper = new DatabaseHelper(this); //for database operation
        username = getIntent().getStringExtra("username"); // get the username is passed from the last activity

        // Save button click listener
        btnSave.setOnClickListener(v -> {
            //here is to get  value from edittext
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
                return; //here to stop execution if validation fails
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
                return; //if the data is already there this message will show
            }

            // Add parking record to the databasehelper
            long result = dbHelper.addParkingRecord(username, vehicleNumber, ownerName, parkingDays, parkingNumber, startDate, endDate);
            if (result != -1) {
                Toast.makeText(this, "Parking record added successfully", Toast.LENGTH_SHORT).show();
                finish(); // this is for the record has successfully added
            } else {
                Toast.makeText(this, "Failed to add parking record", Toast.LENGTH_SHORT).show();
            } //add failed
        });
    }
}
