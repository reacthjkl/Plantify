package com.example.plantify.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.plantify.R;
import com.example.plantify.helpers.FileHelper;
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
 * The ManualEntryActivity allows users to manually create and save a new plant entry.
 * Users can enter the plant’s name and location, select a watering frequency from a dropdown,
 * and optionally pick an image from their device’s gallery.
 * The selected image is copied to internal storage, and a preview is displayed.
 * Once saved, the plant is added to the persistent list and the user is redirected to the main screen.
 */

public class ManualEntryActivity extends AppCompatActivity {
    private Spinner spinnerFreq;
    private EditText etName, etLocation;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_CAMERA = 2;
    private Uri selectedImageUri;
    private Uri cameraImageUri;
    private ImageView ivPreview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        // Always initialize views after setContentView
        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        spinnerFreq = findViewById(R.id.spinnerFreq);
        ivPreview = findViewById(R.id.ivPreview);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        Button btnSave = findViewById(R.id.btnSave);

        String[] freqs = {
                getString(R.string.daily),
                getString(R.string.one_time_per_week),
                getString(R.string.two_times_per_month),
        };

        spinnerFreq.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, freqs));

        ivPreview.setImageResource(R.drawable.ic_placeholder);

        btnChooseImage.setOnClickListener(v -> {
            String[] options = {
                    getString(R.string.take_photo),
                    getString(R.string.choose_from_gallery)
            };
            new AlertDialog.Builder(this)
                    .setTitle(R.string.choose_from_gallery)
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            openCamera();
                        } else {
                            openGallery();
                        }
                    }).show();
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plantName = etName.getText().toString().trim();
                String location = etLocation.getText().toString().trim();
                String wateringFrequency = spinnerFreq.getSelectedItem().toString();
                String imagePath = selectedImageUri != null ? selectedImageUri.getPath() : "";

                if (plantName.isEmpty()) {
                    Toast.makeText(ManualEntryActivity.this, getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                Plant newPlant = new Plant(plantName, imagePath, location, wateringFrequency);
                newPlant.setLastWateredTimestamp(null);


                List<Plant> plants = PlantStorage.loadPlants(ManualEntryActivity.this);
                plants.add(newPlant);
                PlantStorage.savePlants(ManualEntryActivity.this, plants);

                Toast.makeText(ManualEntryActivity.this, getString(R.string.plant_saved), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManualEntryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        Uri imageUri = null;

        if (requestCode == REQUEST_GALLERY && data != null) {
            Uri sourceUri = data.getData();
            imageUri = FileHelper.copyToInternalStorage(this, sourceUri);
        } else if (requestCode == REQUEST_CAMERA) {
            imageUri = FileHelper.copyToInternalStorage(this, cameraImageUri);
        }

        if (imageUri != null) {
            selectedImageUri = imageUri;
            ivPreview.setImageURI(selectedImageUri);
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


}
