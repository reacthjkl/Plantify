package com.example.plantify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPlantListChangedListener {
    private RecyclerView rvPlants;
    private FloatingActionButton fabAdd;
    private List<String> plantFacts;
    private TextView tvFact;
    private Button btnNextFact;
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
        rvPlants = findViewById(R.id.rvPlants);
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
        btnNextFact = findViewById(R.id.btnNextFact);

        plantFacts = PlantData.getFactsOfTheDay();

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
