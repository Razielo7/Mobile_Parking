package com.example.mobile_parking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MobileParking.db";
    public static final int DATABASE_VERSION = 1;

    // Users_Table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Parking_Records_Table
    public static final String TABLE_PARKING = "parking";
    public static final String COLUMN_PARKING_ID = "id";
    public static final String COLUMN_VEHICLE_NUMBER = "vehicle_number";
    public static final String COLUMN_OWNER_NAME = "owner_name";
    public static final String COLUMN_PARKING_DAYS = "parking_days";
    public static final String COLUMN_PARKING_NUMBER = "parking_number";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_USER_ID_FK = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";

        String createParkingTable = "CREATE TABLE " + TABLE_PARKING + " (" +
                COLUMN_PARKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_VEHICLE_NUMBER + " TEXT, " +
                COLUMN_OWNER_NAME + " TEXT, " +
                COLUMN_PARKING_DAYS + " INTEGER, " +
                COLUMN_PARKING_NUMBER + " TEXT, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_END_DATE + " TEXT, " +
                COLUMN_USER_ID_FK + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "UNIQUE(" + COLUMN_PARKING_NUMBER + ", " + COLUMN_START_DATE + ", " + COLUMN_END_DATE + "))";

        db.execSQL(createUsersTable);
        db.execSQL(createParkingTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING);
        onCreate(db);
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }

    public long addParkingRecord(String username, String vehicleNumber, String ownerName, int parkingDays,
                                 String parkingNumber, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        int userId = getUserId(username);
        if (userId == -1) {
            return -1; // User not found
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_NUMBER, vehicleNumber);
        values.put(COLUMN_OWNER_NAME, ownerName);
        values.put(COLUMN_PARKING_DAYS, parkingDays);
        values.put(COLUMN_PARKING_NUMBER, parkingNumber);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_USER_ID_FK, userId);

        return db.insert(TABLE_PARKING, null, values);
    }

    public Cursor getParkingRecords(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int userId = getUserId(username);
        if (userId == -1) {
            return null; // User not found
        }
        String query = "SELECT * FROM " + TABLE_PARKING + " WHERE " + COLUMN_USER_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }

    public int updateParkingRecord(int id, String vehicleNumber, String ownerName, int parkingDays, String parkingNumber, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_VEHICLE_NUMBER, vehicleNumber);
        values.put(COLUMN_OWNER_NAME, ownerName);
        values.put(COLUMN_PARKING_DAYS, parkingDays);
        values.put(COLUMN_PARKING_NUMBER, parkingNumber);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);

        return db.update(TABLE_PARKING, values, COLUMN_PARKING_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean isParkingAvailable(String parkingNumber, String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PARKING +
                " WHERE " + COLUMN_PARKING_NUMBER + " = ? " +
                " AND (" +
                " (? BETWEEN " + COLUMN_START_DATE + " AND " + COLUMN_END_DATE + ") OR " +
                " (? BETWEEN " + COLUMN_START_DATE + " AND " + COLUMN_END_DATE + ") OR " +
                " (? <= " + COLUMN_START_DATE + " AND ? >= " + COLUMN_END_DATE + ")" +
                " )";

        Cursor cursor = db.rawQuery(query, new String[]{parkingNumber, startDate, endDate, startDate, endDate});
        boolean isAvailable = cursor.getCount() == 0; // True if no conflicts found
        cursor.close();
        return isAvailable;
    }

    public int deleteParkingRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PARKING, COLUMN_PARKING_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAllParkingRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARKING, null, null);
        db.close();
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            cursor.close();
            return id;
        }
        if (cursor != null) {
            cursor.close();
        }
        return -1; // User not found
    }
}
