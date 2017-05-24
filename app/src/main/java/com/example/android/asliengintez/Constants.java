package com.example.android.asliengintez;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    private static String names[] = {"Optimum Outlet Ve Eğlence Merkezi", "Forum Bornova", "Alsancak Katlı Otopark", "Bilgiç Otopark"};
    private static Double latitudes[] = {38.338694, 38.4500515, 38.4410142, 38.4232332};
    private static Double longitudes[] = {27.1323287, 27.2101629, 27.1434832, 27.1425723};
    private static String workingHours[] = {"08:00-22:00", "08:30-23:00", "08:00-00:00", "09:00-23:00"};
    private static int capacities[] = {300, 250, 280, 200};
    private static int carNumbers[] = {100, 120, 200, 200};
    private static int dailyFees[] = {10, 15, 10, 20};
    private static int hourlyFees[] = {3, 2, 4, 5};
    private static int subscriptionFees[] = {100, 120, 80, 90};

    public static Map<String, CarPark> getLocationMap() {
        Map<String, CarPark> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            LatLng latLng = new LatLng(latitudes[i], longitudes[i]);
            CarPark carPark = new CarPark(new Location(latitudes[i], longitudes[i]), names[i],
                    workingHours[i], capacities[i], carNumbers[i], dailyFees[i],
                    hourlyFees[i], subscriptionFees[i]);
            map.put(names[i], carPark);
        }
        return map;
    }

    public static List<String> getCarParksNames() {
        List<String> list = new ArrayList<>();
        for (String name : names) {
            list.add(name);
        }
        return list;
    }

    public static void saveToDB() {
        Firebase mealListRef = new Firebase(Constants.FIREBASE_URL).child("parks");
        Map<String, CarPark> map = getLocationMap();
        for (Map.Entry<String, CarPark> entity : map.entrySet()) {
            mealListRef.child(entity.getKey()).setValue(entity.getValue());
        }
    }
}
