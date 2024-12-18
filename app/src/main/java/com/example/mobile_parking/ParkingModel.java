package com.example.mobile_parking;

public class ParkingModel {
    public int id;
    public String vehicleNumber;
    public String ownerName;
    public int parkingDays;
    public String parkingNumber;
    public String startDate;
    public String endDate;


    public ParkingModel(int parkingid, String vehicleNu, String ownerN, int parkingD, String parkingNu, String startD, String endD) {
        id = parkingid;
        vehicleNumber = vehicleNu;
        ownerName = ownerN;
        parkingDays = parkingD;
        parkingNumber = parkingNu;
        startDate = startD;
        endDate = endD;
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
