package com.example.plantify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plantify.R;
import com.example.plantify.models.Plant;
import com.example.plantify.models.SearchPlant;
import com.example.plantify.persistance.PlantData;
import com.example.plantify.persistance.PlantStorage;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Illia Soloviov
 *
 * The NewPlantActivity provides an interface for users to quickly add a new plant from a predefined list.
 * It features a searchable list of plants with predefined watering frequencies.
 * Users can filter the list via a search field, and selecting a plant automatically adds it to their collection.
 * Alternatively, users can navigate to a manual entry screen to add a plant with custom details.
 */

public class NewPlantActivity extends AppCompatActivity {

    private List<SearchPlant> displayList;
    private ArrayAdapter<SearchPlant> adapter;
    private List<SearchPlant> allPlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);

        allPlants = PlantData.getPredefinedSearchPlants(this);

        EditText etSearch = findViewById(R.id.etSearch);
        ListView lvResults = findViewById(R.id.lvResults);
        Button btnManual = findViewById(R.id.btnManual);

        displayList = new ArrayList<>(allPlants);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        lvResults.setAdapter(adapter);

        lvResults.setOnItemClickListener((parent, view, position, id) -> {
            SearchPlant selected = (SearchPlant) parent.getItemAtPosition(position);
            Plant newPlant = new Plant(selected.getName(), "", "", selected.getFrequency());
            newPlant.setLastWateredTimestamp(null);
            List<Plant> existingPlants = PlantStorage.loadPlants(this);
            existingPlants.add(newPlant);
            PlantStorage.savePlants(this, existingPlants);
            finish();
        });

        btnManual.setOnClickListener(view -> {
            startActivity(new Intent(NewPlantActivity.this, ManualEntryActivity.class));
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().toLowerCase();
                displayList.clear();
                for (SearchPlant plant : allPlants) {
                    if (plant.getName().toLowerCase().contains(searchText)) {
                        displayList.add(plant);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
