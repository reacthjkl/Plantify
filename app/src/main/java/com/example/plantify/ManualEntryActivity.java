package com.example.plantify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ManualEntryActivity extends AppCompatActivity {
    private Spinner spinnerFreq;
    private Button btnChooseImage, btnSave;
    private EditText etName, etLocation;
    private Uri selectedImageUri;
    private ImageView ivPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        // Always initialize views after setContentView
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        spinnerFreq = findViewById(R.id.spinnerFreq);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSave = findViewById(R.id.btnSave);
        ivPreview = findViewById(R.id.ivPreview);

        String[] freqs = { "täglich", "1× pro Woche", "2× pro Monat" };
        spinnerFreq.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, freqs));

        ivPreview.setImageResource(R.drawable.ic_placeholder);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1); // request code = 1
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plantName = etName.getText().toString().trim();
                String location = etLocation.getText().toString().trim();
                String wateringFrequency = spinnerFreq.getSelectedItem().toString();
                String imagePath = selectedImageUri != null ? selectedImageUri.getPath() : "";

                if (plantName.isEmpty()) {
                    Toast.makeText(ManualEntryActivity.this, "Bitte gib einen Namen ein", Toast.LENGTH_SHORT).show();
                    return;
                }

                Plant newPlant = new Plant(plantName, imagePath, location, wateringFrequency);
                newPlant.setLastWateredTimestamp(null);


                List<Plant> plants = PlantStorage.loadPlants(ManualEntryActivity.this);
                plants.add(newPlant);
                PlantStorage.savePlants(ManualEntryActivity.this, plants);

                Toast.makeText(ManualEntryActivity.this, "Pflanze gespeichert", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManualEntryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri sourceUri = data.getData();

            try {
                // Create a new file in internal storage
                File destFile = new File(getFilesDir(), System.currentTimeMillis() + "_plant.jpg");
                try (InputStream in = getContentResolver().openInputStream(sourceUri);
                     OutputStream out = new FileOutputStream(destFile)) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // Save the absolute path for later
                selectedImageUri = Uri.fromFile(destFile);

                // Optional: preview
                ImageView ivPreview = findViewById(R.id.ivPreview);
                ivPreview.setImageURI(selectedImageUri);

            } catch (IOException e) {
                Toast.makeText(this, "Fehler beim Speichern des Bildes", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


}
