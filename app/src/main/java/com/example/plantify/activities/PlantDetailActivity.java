package com.example.plantify.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.plantify.R;
import com.example.plantify.models.Plant;
import com.example.plantify.persistance.PlantStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/*
 * Author: Illia Soloviov
 *
 * The PlantDetailActivity allows users to view and edit the details of a selected plant.
 * Users can update the plant’s name, location, watering frequency, and image.
 * Images can be selected either from the device’s gallery or taken directly using the camera.
 * The activity also supports deleting the plant, including its associated image file from internal storage.
 * All changes are saved to persistent storage, and the activity notifies the user accordingly.
 */

public class PlantDetailActivity extends AppCompatActivity {

    private EditText etName;

    private EditText etLocation;
    private Spinner spinnerFreq;
    private ImageView ivImage;
    private Uri selectedImageUri;
    private Plant plant;

    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private Uri cameraImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        plant = (Plant) getIntent().getSerializableExtra("plant");

        etLocation = findViewById(R.id.etLocation);
        etName = findViewById(R.id.etName);
        spinnerFreq = findViewById(R.id.spinnerFreq);
        ivImage = findViewById(R.id.ivImage);
        Button btnDelete = findViewById(R.id.btnDelete);

        Button btnSave = findViewById(R.id.btnSave);
        Button btnChangeImage = findViewById(R.id.btnChangeImage);

        // Init spinner
        String[] freqs = {"täglich", "1× pro Woche", "2× pro Monat"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, freqs);
        spinnerFreq.setAdapter(adapter);

        etName.setText(plant.getName());


        // Set existing values
        if (plant != null) {
            etLocation.setText(plant.getLocation());
            int spinnerPosition = adapter.getPosition(plant.getWateringFrequency());
            spinnerFreq.setSelection(spinnerPosition);

            if (plant.getImagePath() != null && !plant.getImagePath().isEmpty()) {
                File img = new File(plant.getImagePath());
                if (img.exists()) {
                    ivImage.setImageURI(Uri.fromFile(img));
                } else {
                    ivImage.setImageResource(R.drawable.ic_placeholder); // fallback if file doesn't exist
                }
            } else {
                ivImage.setImageResource(R.drawable.ic_placeholder); // fallback if path is null or empty
            }
        }

        btnChangeImage.setOnClickListener(v -> {
            String[] options = {"Foto aufnehmen", "Aus Galerie wählen"};
            new AlertDialog.Builder(this)
                    .setTitle("Bild auswählen")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            // Camera
                            openCamera();
                        } else {
                            // Gallery
                            openGallery();
                        }
                    }).show();
        });

        // Save changes
        btnSave.setOnClickListener(v -> {
            String newLocation = etLocation.getText().toString().trim();
            String newFreq = spinnerFreq.getSelectedItem().toString();
            String newImagePath = selectedImageUri != null ? selectedImageUri.getPath() : plant.getImagePath();

            // Update plant object
            String newName = etName.getText().toString().trim();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Name darf nicht leer sein", Toast.LENGTH_SHORT).show();
                return;
            }
            plant = new Plant(plant.getId(), newName, newImagePath, newLocation, newFreq);

            // Replace updated plant in list and save
            List<Plant> plants = PlantStorage.loadPlants(this);
            for (int i = 0; i < plants.size(); i++) {
                if (plants.get(i).getId().equals(plant.getId())) {
                    plants.set(i, plant);
                    break;
                }
            }

            PlantStorage.savePlants(this, plants);

            Toast.makeText(this, "Pflanze aktualisiert", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Pflanze löschen")
                    .setMessage("Möchtest du diese Pflanze wirklich löschen?")
                    .setPositiveButton("Ja", (dialog, which) -> {
                        // 1. Delete image file if it exists
                        if (plant.getImagePath() != null && !plant.getImagePath().isEmpty()) {
                            File imageFile = new File(plant.getImagePath());
                            if (imageFile.exists()) {
                                boolean deleted = imageFile.delete();
                                if (!deleted) {
                                    Toast.makeText(this, "Bild konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        // 2. Remove plant from storage
                        List<Plant> plants = PlantStorage.loadPlants(this);
                        for (int i = 0; i < plants.size(); i++) {
                            if (plants.get(i).getName().equals(plant.getName())) {
                                plants.remove(i);
                                break;
                            }
                        }
                        PlantStorage.savePlants(this, plants);
                        Toast.makeText(this, "Pflanze gelöscht", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .setNegativeButton("Abbrechen", null)
                    .show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        Uri imageUri = null;

        if (requestCode == REQUEST_GALLERY && data != null) {
            Uri sourceUri = data.getData();
            imageUri = copyToInternalStorage(sourceUri);
        } else if (requestCode == REQUEST_CAMERA) {
            imageUri = copyToInternalStorage(cameraImageUri);
        }

        if (imageUri != null) {
            selectedImageUri = imageUri;
            ivImage.setImageURI(selectedImageUri);
        }
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void openCamera() {
        File imageFile = new File(getFilesDir(), System.currentTimeMillis() + "_camera.jpg");
        cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private Uri copyToInternalStorage(Uri sourceUri) {
        try {
            File destFile = new File(getFilesDir(), System.currentTimeMillis() + "_plant.jpg");
            try (InputStream in = getContentResolver().openInputStream(sourceUri);
                 OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return Uri.fromFile(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
