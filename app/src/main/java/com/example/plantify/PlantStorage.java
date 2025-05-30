
package com.example.plantify;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
