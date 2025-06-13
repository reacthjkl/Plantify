
package com.example.plantify.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.plantify.models.Plant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/*
 * Author: Illia Soloviov
 *
 * The PlantStorage class handles saving, loading, and clearing the list of plants using SharedPreferences.
 * Plant data is serialized and deserialized using Gson to store it as JSON.
 * This class provides a lightweight persistence mechanism to retain plant data across app sessions.
 */
public class PlantStorage {

    private static final String PREF_NAME = "plantify";
    private static final String KEY_PLANT_LIST = "plantList";

    public static void savePlants(Context context, List<Plant> plants) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(plants);
        editor.putString(KEY_PLANT_LIST, json);
        editor.apply();
    }

    public static List<Plant> loadPlants(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PLANT_LIST, null);
        if (json != null) {
            Type listType = new TypeToken<List<Plant>>() {}.getType();
            return new Gson().fromJson(json, listType);
        }
        return new ArrayList<>();
    }

    public static void clearPlants(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_PLANT_LIST).apply();
    }
}
