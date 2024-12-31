package com.example.mobile_parking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public RecyclerView rvParkingRecords;
    public DatabaseHelper dbHelper;
    public ArrayList<ParkingModel> parkingList;
    public ParkingAdapter parkingAdapter;
    public String username;
    public Button btnAddRecord;
    public Button btnDeleteAllRecords;
    public Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Views
        rvParkingRecords = findViewById(R.id.rvParkingRecords);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        btnDeleteAllRecords = findViewById(R.id.btnDeleteAllRecords);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DatabaseHelper(this);
        parkingList = new ArrayList<>();
        username = getIntent().getStringExtra("username");

        // Setup RecyclerView
        rvParkingRecords.setLayoutManager(new LinearLayoutManager(this));
        parkingAdapter = new ParkingAdapter(this, parkingList, new ParkingAdapter.OnItemActionListener() {
            @Override
            public void onDelete(ParkingModel parking) {
                showDeleteConfirmationDialog(parking);
            }

            @Override
            public void onEdit(ParkingModel parking) {
                // Pass data to UpdateParkingActivity
                Intent intent = new Intent(HomeActivity.this, UpdateParkingActivity.class);
                intent.putExtra("parkingId", parking.getId());
                intent.putExtra("vehicleNumber", parking.getVehicleNumber());
                intent.putExtra("ownerName", parking.getOwnerName());
                intent.putExtra("parkingDays", parking.getParkingDays());
                intent.putExtra("parkingNumber", parking.getParkingNumber());
                intent.putExtra("startDate", parking.getStartDate());
                intent.putExtra("endDate", parking.getEndDate());
                startActivity(intent);
            }
        });
        rvParkingRecords.setAdapter(parkingAdapter);

        // Add Record Button
        btnAddRecord.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddParkingActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        // Delete All
        btnDeleteAllRecords.setOnClickListener(v -> {
            showDeleteAllConfirmationDialog();
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });

        // Load Records
        loadParkingRecords();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadParkingRecords() {
        parkingList.clear();
        Cursor cursor = dbHelper.getParkingRecords(username);

        if (cursor != null) {
            Log.d("HomeActivity", "Total Records: " + cursor.getCount());
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARKING_ID));
                String vehicleNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VEHICLE_NUMBER));
                String ownerName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OWNER_NAME));
                int parkingDays = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARKING_DAYS));
                String parkingNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARKING_NUMBER));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_DATE));

                parkingList.add(new ParkingModel(id, vehicleNumber, ownerName, parkingDays, parkingNumber, startDate, endDate));
                Log.d("HomeActivity", "Added Record: " + vehicleNumber + ", " + ownerName);
            }
            cursor.close();
        } else {
            Log.d("HomeActivity", "No records found.");
        }
        parkingAdapter.notifyDataSetChanged();
    }

    private void showDeleteConfirmationDialog(ParkingModel record) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Parking Record")
                .setMessage("Are you sure you want to delete this parking record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    int rowsAffected = dbHelper.deleteParkingRecord(record.getId());
                    if (rowsAffected > 0) {
                        Toast.makeText(HomeActivity.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
                        loadParkingRecords();
                    } else {
                        Toast.makeText(HomeActivity.this, "Failed to delete record", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showDeleteAllConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete ALL Records")
                .setMessage("Are you sure you want to delete all parking records?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteAllParkingRecords();
                    Toast.makeText(HomeActivity.this, "All records deleted successfully", Toast.LENGTH_SHORT).show();
                    loadParkingRecords();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadParkingRecords();
    }
}
