package com.example.plantify.models;

import java.io.Serializable;
import java.util.UUID;

/*
 * Author: Illia Soloviov
 *
 * The Plant class represents a single plant entry in the Plantify app.
 * It holds key information such as a unique ID (UUID), plant name, image path, location,
 * watering frequency, and the timestamp of the last watering.
 * The class provides constructors for creating new plant instances with or without a predefined ID.
 * It implements Serializable to support easy data storage and transfer.
 * An empty constructor is included for compatibility with JSON serialization tools like Gson.
 */

public class Plant implements Serializable {
    private String id; // UUID
    private String name;
    private String imagePath;
    private String location;
    private String wateringFrequency;
    private Long lastWateredTimestamp;

    public Plant(String name, String imagePath, String location, String wateringFrequency) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.imagePath = imagePath;
        this.location = location;
        this.wateringFrequency = wateringFrequency;
        this.lastWateredTimestamp = System.currentTimeMillis();
    }

    public Plant(String id, String name, String imagePath, String location, String wateringFrequency) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.location = location;
        this.wateringFrequency = wateringFrequency;
        this.lastWateredTimestamp = System.currentTimeMillis();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getLocation() {
        return location;
    }

    public String getWateringFrequency() {
        return wateringFrequency;
    }

    public Long getLastWateredTimestamp() {
        return lastWateredTimestamp;
    }

    public void setLastWateredTimestamp(Long timestamp) {
        this.lastWateredTimestamp = timestamp;
    }

    // Required empty constructor for Gson
    public Plant() {
    }


}
