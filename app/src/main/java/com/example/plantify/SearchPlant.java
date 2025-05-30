package com.example.plantify;

public class SearchPlant {
    private String name;
    private String frequency;

    public SearchPlant(String name, String frequency) {
        this.name = name;
        this.frequency = frequency;
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

