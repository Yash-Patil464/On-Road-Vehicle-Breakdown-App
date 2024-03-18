package com.example.orvb;

public class Car {
    private String plateNumber;
    private String gearType;
    private String fuelType;

    public Car(String plateNumber, String gearType, String fuelType) {
        this.plateNumber = plateNumber;
        this.gearType = gearType;
        this.fuelType = fuelType;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getGearType() {
        return gearType;
    }

    public void setGearType(String gearType) {
        this.gearType = gearType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}
