package com.example.plantify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantify.adapters.PlantAdapter;
import com.example.plantify.R;
import com.example.plantify.interfaces.OnPlantListChangedListener;
import com.example.plantify.models.Plant;
import com.example.plantify.persistance.PlantData;
import com.example.plantify.persistance.PlantStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/*
 * Author: Illia Soloviov
 *
 * The purpose of the MainActivity is to serve as the entry point of the Plantify app.
 * It displays a list of saved plants and allows the user to add new ones via a FloatingActionButton.
 * Additionally, it shows a "Fact of the Day" section with randomly selected plant-related facts that change on button press.
 * The activity listens for changes in the plant list and updates the UI accordingly.
 */
public class MainActivity extends AppCompatActivity implements OnPlantListChangedListener {
    private FloatingActionButton fabAdd;
    private List<String> plantFacts;
    private TextView tvFact;
    private String lastFact = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPlantList();
        initFactOfTheDay();

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gehe zu "Neue Pflanze erfassen"
                Intent intent = new Intent(MainActivity.this, NewPlantActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPlantList(); // reload list every time this screen is shown
    }

    @Override
    public void onPlantListChanged(boolean isEmpty) {
        TextView tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        RecyclerView rvPlants = findViewById(R.id.rvPlants);

        if (isEmpty) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvPlants.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            rvPlants.setVisibility(View.VISIBLE);
        }
    }

    private void initPlantList() {
        RecyclerView rvPlants = findViewById(R.id.rvPlants);
        fabAdd = findViewById(R.id.fabAdd);
        TextView tvEmptyMessage = findViewById(R.id.tvEmptyMessage);

        List<Plant> plantList = PlantStorage.loadPlants(this);

        if (plantList.isEmpty()) {
            // Show the empty message
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvPlants.setVisibility(View.GONE);
        } else {
            // Show the plant list
            tvEmptyMessage.setVisibility(View.GONE);
            rvPlants.setVisibility(View.VISIBLE);
            rvPlants.setLayoutManager(new LinearLayoutManager(this));
            rvPlants.setAdapter(new PlantAdapter(this, plantList, this)); // "this" als Listener

        }
    }

    private void initFactOfTheDay() {
        tvFact = findViewById(R.id.tvFact);
        Button btnNextFact = findViewById(R.id.btnNextFact);

        plantFacts = PlantData.getFactsOfTheDay(this);

        showRandomFact(); // initial random fact

        btnNextFact.setOnClickListener(v -> {
            showRandomFact();
        });
    }

    private void showRandomFact() {
        if (plantFacts.isEmpty()) return;

        String randomFact;
        do {
            randomFact = plantFacts.get((int) (Math.random() * plantFacts.size()));
        } while (randomFact.equals(lastFact) && plantFacts.size() > 1); // prevent immediate repeat

        lastFact = randomFact;
        tvFact.setText(randomFact);
    }
}