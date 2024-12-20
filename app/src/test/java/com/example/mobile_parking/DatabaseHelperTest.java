package com.example.mobile_parking;

import android.content.Context;
import android.database.Cursor;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {33}) // there is issue with 34 so I Force Robolectric to use API level 33
public class DatabaseHelperTest {

    private DatabaseHelper dbHelper;
    private Context context;

    @Before
    public void setUp() {
        // make the  context and DatabaseHelper ready
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
    }

    @After
    public void tearDown() {
        // delete all data and close the database
        dbHelper.deleteAllParkingRecords();
        dbHelper.close();
    }

    @Test
    public void testAddUser() {
        long result = dbHelper.addUser("Abdu123", "Oman@123");
        assertTrue("User should be added successfully", result > 0);
    }

    @Test
    public void testValidateUser() {
        dbHelper.addUser("Abdu123", "Oman@123");
        boolean isValid = dbHelper.validateUser("Abdu123", "Oman@123");
        assertTrue("User validation failed", isValid);
    }

    @Test
    public void testAddParkingRecord() {
        dbHelper.addUser("Abdu123", "Oman@123");
        long recordId = dbHelper.addParkingRecord(
                "Abdu123", "12321", "Abdu", 2, "1", "2024-12-20", "2024-12-22");
        assertTrue("Failed to add parking record", recordId > 0);
    }

    @Test
    public void testGetParkingRecords() {
        dbHelper.addUser("Abdu123", "Oman@123");
        dbHelper.addParkingRecord("Abdu123", "12332", "Ali", 3, "2", "2024-12-21", "2024-12-24");

        Cursor cursor = dbHelper.getParkingRecords("Abdu123");
        assertNotNull("Cursor is null", cursor);
        assertTrue("No records found", cursor.getCount() > 0);
        cursor.close();
    }

    @Test
    public void testUpdateParkingRecord() {
        dbHelper.addUser("Abdu123", "Oman@123");
        long parkingId = dbHelper.addParkingRecord("Abdu123", "56765", "Ahmed", 1, "3", "2024-12-25", "2024-12-25");
        assertTrue("Parking record should be added successfully", parkingId > 0);

        int rowsAffected = dbHelper.updateParkingRecord(
                (int) parkingId, "78987", "Ali", 2, "4", "2024-12-26", "2024-12-28");
        assertEquals("One row should be updated", 1, rowsAffected);

        Cursor cursor = dbHelper.getParkingRecords("Abdu123");
        assertTrue("Cursor should move to the first record", cursor.moveToFirst());
        assertEquals("Vehicle number mismatch", "78987",
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_VEHICLE_NUMBER)));
        assertEquals("Owner name mismatch", "Ali",
                cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OWNER_NAME)));
        cursor.close();
    }

    @Test
    public void testIsParkingAvailable() {
        dbHelper.addUser("Abdu123", "Oman@123");
        dbHelper.addParkingRecord("Abdu123", "12321", "Abdu", 2, "5", "2024-12-20", "2024-12-22");

        boolean isAvailable = dbHelper.isParkingAvailable("5", "2024-12-23", "2024-12-25");
        assertTrue("Parking slot should be available for non-overlapping dates", isAvailable);

        boolean isNotAvailable = dbHelper.isParkingAvailable("5", "2024-12-21", "2024-12-23");
        assertFalse("Parking slot should not be available for overlapping dates", isNotAvailable);
    }

    @Test
    public void testDeleteParkingRecord() {
        dbHelper.addUser("Abdu123", "Oman@123");
        long parkingId = dbHelper.addParkingRecord("Abdu123", "12332", "Ali", 1, "6", "2024-12-23", "2024-12-23");
        assertTrue("Parking record should be added successfully", parkingId > 0);

        int rowsAffected = dbHelper.deleteParkingRecord((int) parkingId);
        assertEquals("One row should be deleted", 1, rowsAffected);
    }

    @Test
    public void testDeleteAllParkingRecords() {
        dbHelper.addUser("Abdu123", "Oman@123");
        dbHelper.addParkingRecord("Abdu123", "12321", "Abdu", 1, "7", "2024-12-20", "2024-12-21");
        dbHelper.addParkingRecord("Abdu123", "56765", "Ahmed", 3, "8", "2024-12-22", "2024-12-25");

        dbHelper.deleteAllParkingRecords();

        Cursor cursor = dbHelper.getParkingRecords("Abdu123");
        assertFalse("Cursor should not have any records after deletion", cursor.moveToFirst());
        cursor.close();
    }
}
