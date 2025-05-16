package com.example.plantify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class NewPlantActivity extends AppCompatActivity {
    private EditText etSearch;
    private ListView lvResults;
    private Button btnManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);

        etSearch = findViewById(R.id.etSearch);
        lvResults = findViewById(R.id.lvResults);
        btnManual = findViewById(R.id.btnManual);

        // Dummy-Daten für Liste
        String[] dummyItems = { "Ficus", "Monstera", "Sukkulente" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, dummyItems
        );
        lvResults.setAdapter(adapter);

        btnManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Weiter zur manuellen Eingabe
                Intent intent = new Intent(NewPlantActivity.this, ManualEntryActivity.class);
                startActivity(intent);
            }
        });
    }
}
