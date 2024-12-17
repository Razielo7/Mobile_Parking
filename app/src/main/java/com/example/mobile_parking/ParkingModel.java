package com.example.mobile_parking;

public class ParkingModel {
    private int id;
    private String vehicleNumber;
    private String ownerName;
    private int parkingDays;
    private String parkingNumber; // New field
    private String startDate;     // New field
    private String endDate;       // New field

    // Updated constructor with additional fields
    public ParkingModel(int id, String vehicleNumber, String ownerName, int parkingDays, String parkingNumber, String startDate, String endDate) {
        this.id = id;
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.parkingDays = parkingDays;
        this.parkingNumber = parkingNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getParkingDays() {
        return parkingDays;
    }

    public String getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
