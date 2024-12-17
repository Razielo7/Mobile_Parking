package com.example.mobile_parking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateParkingActivity extends AppCompatActivity {

    private EditText etVehicleNumber, etOwnerName, etParkingDays, etParkingNumber, etStartDate, etEndDate;
    private Button btnUpdate, btnDelete;
    private DatabaseHelper dbHelper;
    private int parkingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_parking);

        etVehicleNumber = findViewById(R.id.etVehicleNumber);
        etOwnerName = findViewById(R.id.etOwnerName);
        etParkingDays = findViewById(R.id.etParkingDays);
        etParkingNumber = findViewById(R.id.etParkingNumber);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        parkingId = intent.getIntExtra("parkingId", -1);

        String vehicleNumber = intent.getStringExtra("vehicleNumber");
        String ownerName = intent.getStringExtra("ownerName");
        int parkingDays = intent.getIntExtra("parkingDays", 0);
        String parkingNumber = intent.getStringExtra("parkingNumber");
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");

        etVehicleNumber.setText(vehicleNumber);
        etOwnerName.setText(ownerName);
        etParkingDays.setText(String.valueOf(parkingDays));
        etParkingNumber.setText(parkingNumber);
        etStartDate.setText(startDate);
        etEndDate.setText(endDate);

        btnUpdate.setOnClickListener(v -> {
            String updatedVehicleNumber = etVehicleNumber.getText().toString();
            String updatedOwnerName = etOwnerName.getText().toString();
            int updatedParkingDays = Integer.parseInt(etParkingDays.getText().toString());
            String updatedParkingNumber = etParkingNumber.getText().toString();
            String updatedStartDate = etStartDate.getText().toString();
            String updatedEndDate = etEndDate.getText().toString();

            int rowsAffected = dbHelper.updateParkingRecord(parkingId, updatedVehicleNumber, updatedOwnerName, updatedParkingDays, updatedParkingNumber, updatedStartDate, updatedEndDate);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Parking record updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update parking record", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            int rowsAffected = dbHelper.deleteParkingRecord(parkingId);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Parking record deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete parking record", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
