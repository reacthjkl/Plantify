package com.example.plantify.models;

/*
 * Author: Illia Soloviov
 *
 * The SearchPlant class represents a simplified plant entry used in the search interface
 * of the Plantify app. It stores the plant’s name and its predefined watering frequency.
 * This class is primarily used for selecting and creating new plant entries from a predefined list.
 * The toString() method returns the plant's name for display in list-based UI components.
 */
public class SearchPlant {
    private String name;
    private String frequency;
    public SearchPlant(String name, String frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    // Required for Gson
    public SearchPlant() {
    }

    public String getName() {
        return name;
    }
    public String getFrequency() {
        return frequency;
    }
    @Override
    public String toString() {
        return name;
    }
}

