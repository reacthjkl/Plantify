package com.example.plantify.persistance;

import android.content.Context;
import android.util.Log;

import com.example.plantify.models.SearchPlant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/*
 * Author: Illia Soloviov
 *
 * The PlantData class provides utility methods for loading plant-related data from JSON files
 * stored in the app's assets directory.
 *
 * - getFactsOfTheDay(Context): Loads a list of plant-related facts from "facts.json".
 * - getPredefinedSearchPlants(Context): Loads a list of predefined plants along with their watering
 *   frequencies from "search_plants.json".
 *
 * Both methods use Gson for JSON deserialization and return an empty list if an error occurs.
 *
 * This class helps separate static data from code, making the app more maintainable and easier to update.
 */
public class PlantData {
    public static List<String> getFactsOfTheDay(Context context) {
        try (InputStream is = context.getAssets().open("facts.json");
             InputStreamReader reader = new InputStreamReader(is)) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // empty fallback
        }
    }

    public static List<SearchPlant> getPredefinedSearchPlants(Context context) {
        try (InputStream is = context.getAssets().open("search_plants.json");
             InputStreamReader reader = new InputStreamReader(is)) {
            Type listType = new TypeToken<List<SearchPlant>>() {}.getType();
            return new Gson().fromJson(reader, listType);
        } catch (Exception e) {
            Log.e("PlantData", "Failed to load search_plants.json", e);
            return List.of();
        }
    }
}
