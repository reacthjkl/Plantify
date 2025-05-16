package com.example.plantify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvPlants;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvPlants = findViewById(R.id.rvPlants);
        fabAdd = findViewById(R.id.fabAdd);

        // Dummy-Setup RecyclerView
        rvPlants.setLayoutManager(new LinearLayoutManager(this));
        rvPlants.setAdapter(new PlantAdapter()); // PlantAdapter liefert leere/dummy Views

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Gehe zu "Neue Pflanze erfassen"
                Intent intent = new Intent(MainActivity.this, NewPlantActivity.class);
                startActivity(intent);
            }
        });
    }
}
