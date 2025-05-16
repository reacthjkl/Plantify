package com.example.plantify;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ManualEntryActivity extends AppCompatActivity {
    private Spinner spinnerSpecies, spinnerFreq;
    private Button btnChooseImage, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        spinnerSpecies = findViewById(R.id.spinnerSpecies);
        spinnerFreq    = findViewById(R.id.spinnerFreq);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSave        = findViewById(R.id.btnSave);

        // Dummy-Adapter für Spinner
        String[] species = { "Ficus", "Monstera", "Sukkulente" };
        spinnerSpecies.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, species));

        String[] freqs = { "täglich", "1× pro Woche", "2× pro Monat" };
        spinnerFreq.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, freqs));

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManualEntryActivity.this,
                        "Bild-Picker (noch nicht implementiert)", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hier würdest du speichern – jetzt nur Dummy-Toast
                Toast.makeText(ManualEntryActivity.this,
                        "Pflanze gespeichert (Dummy)", Toast.LENGTH_SHORT).show();
                finish(); // zurück zur Startseite
            }
        });
    }
}
