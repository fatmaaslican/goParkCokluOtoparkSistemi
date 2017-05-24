package com.example.android.asliengintez;

import java.io.Serializable;

public class CarPark implements Serializable {
    private Location location;
    private String name;
    private String workingHours;
    private int capacity;
    private int carNumber;
    private int dailyFees;
    private int hourlyFees;
    private int subscriptionFees;

    public CarPark() {
    }

    public CarPark(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public CarPark(Location location, String name, String workingHours, int capacity,
                   int carNumber, int dailyFees, int hourlyFees, int subscriptionFees) {
        this.location = location;
        this.name = name;
        this.workingHours = workingHours;
        this.capacity = capacity;
        this.carNumber = carNumber;
        this.dailyFees = dailyFees;
        this.hourlyFees = hourlyFees;
        this.subscriptionFees = subscriptionFees;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public int getDailyFees() {
        return dailyFees;
    }

    public int getHourlyFees() {
        return hourlyFees;
    }

    public int getSubscriptionFees() {
        return subscriptionFees;
    }
}
