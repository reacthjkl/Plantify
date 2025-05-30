package com.example.plantify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewPlantActivity extends AppCompatActivity {
    private EditText etSearch;
    private ListView lvResults;
    private Button btnManual;

    private List<SearchPlant> displayList;
    private ArrayAdapter<SearchPlant> adapter;
    private List<SearchPlant> allPlants = PlantData.getPredefinedSearchPlants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);

        etSearch = findViewById(R.id.etSearch);
        lvResults = findViewById(R.id.lvResults);
        btnManual = findViewById(R.id.btnManual);

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
