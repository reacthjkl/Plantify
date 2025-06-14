package com.example.plantify.adapters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantify.R;
import com.example.plantify.activities.PlantDetailActivity;
import com.example.plantify.interfaces.OnPlantListChangedListener;
import com.example.plantify.models.Plant;
import com.example.plantify.persistance.PlantStorage;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * Author: Illia Soloviov
 *
 * The PlantAdapter is a custom RecyclerView adapter that manages how plant entries are displayed
 * in the Plantify app. Each plant item shows its name, image, and the time since it was last watered.
 *
 * It supports user interactions such as:
 * - Clicking an item to open detailed plant information.
 * - Long-pressing an item to open a context menu for deleting the plant or setting the last watered date.
 *
 * The adapter also handles:
 * - Image loading from internal storage with fallbacks.
 * - Relative time calculation for last watering.
 * - Notifying a listener when the plant list changes (e.g., becomes empty after deletion).
 */
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private final Context context;
    private final List<Plant> plantList;
    private final OnPlantListChangedListener listener;

    public PlantAdapter(Context context, List<Plant> plantList, OnPlantListChangedListener listener) {
        this.context = context;
        this.plantList = plantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);
        holder.tvPlantName.setText(plant.getName());

        // Load image
        if (plant.getImagePath() != null && !plant.getImagePath().isEmpty()) {
            File imgFile = new File(plant.getImagePath());
            if (imgFile.exists()) {
                holder.ivPlant.setImageURI(Uri.fromFile(imgFile));
            } else {
                holder.ivPlant.setImageResource(R.drawable.ic_plant_placeholder);
            }
        } else {
            holder.ivPlant.setImageResource(R.drawable.ic_plant_placeholder);
        }

        // Click and long click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlantDetailActivity.class);
            intent.putExtra("plant", plant);
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            showPopupMenu(v, plant, position);
            return true;
        });

        Long timestamp = plant.getLastWateredTimestamp();

        if (timestamp == null) {
            // no watering yet
            showWateringWarning(holder);
        } else {
            long now = System.currentTimeMillis();
            long diff = now - timestamp;
            long daysSinceWatered = TimeUnit.MILLISECONDS.toDays(diff);

            int recommendedInterval = parseWateringIntervalInDays(plant.getWateringFrequency());

            if (recommendedInterval > 0 && daysSinceWatered >= recommendedInterval) {
                // overdue
                showWateringWarning(holder);
            } else {
                // on time
                String relativeTime = (daysSinceWatered == 0) ? context.getString(R.string.today) : daysSinceWatered + " " + context.getString(R.string.days_ago);
                holder.tvLastWatered.setText(relativeTime);
                holder.ivWaterIcon.setImageResource(R.drawable.ic_water_can);
                holder.tvLastWatered.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            }
        }
    }

    private void showWateringWarning(PlantViewHolder holder) {
        holder.tvLastWatered.setText(context.getString(R.string.to_water));
        holder.ivWaterIcon.setImageResource(R.drawable.ic_warning);
        holder.tvLastWatered.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
    }

    private int parseWateringIntervalInDays(String frequency) {
        if (frequency == null) {
            Log.w("PlantAdapter", "Watering frequency is null for a plant");
            return -1;
        }

        frequency = frequency.toLowerCase();

        //TODO: fix internationalization by using integers for frequency instead of strings
        if (frequency.toLowerCase().contains("täglich") || frequency.toLowerCase().contains("daily")) return 1;
        if (frequency.toLowerCase().contains("1× pro woche") || frequency.toLowerCase().contains("1× per week")) return 7;
        if (frequency.toLowerCase().contains("2× pro woche") || frequency.toLowerCase().contains("2x per week")) return 3;
        if (frequency.toLowerCase().contains("2× pro monat") || frequency.toLowerCase().contains("2x per month")) return 15;

        return -1;
    }

    private void showPopupMenu(View anchor, Plant plant, int position) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.inflate(R.menu.plant_context_menu); // we'll create this menu XML next

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_delete) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.delete_plant))
                        .setMessage(context.getString(R.string.realy_delete_plant))
                        .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                            // Delete image
                            if (plant.getImagePath() != null && !plant.getImagePath().isEmpty()) {
                                File img = new File(plant.getImagePath());
                                if (img.exists()) img.delete();
                            }

                            // Remove plant
                            int index = plantList.indexOf(plant);
                            if (index != -1) {
                                plantList.remove(index);
                                notifyItemRemoved(index);
                            }

                            // Save updated list
                            PlantStorage.savePlants(context, plantList);

                            // Inform listener
                            if (listener != null) {
                                listener.onPlantListChanged(plantList.isEmpty());
                            }

                            Toast.makeText(context, context.getString(R.string.plant_deleted), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(context.getString(R.string.cancel), null)
                        .show();
                return true;

            } else if (item.getItemId() == R.id.action_watered) {
                showWateredDialog(plant, position);
                return true;
            }

            return false;
        });

        popupMenu.show();
    }

    private void showWateredDialog(Plant plant, int position) {
        Context context = this.context;

        Calendar now = Calendar.getInstance();

        if (!(context instanceof AppCompatActivity)) return;

        DatePickerDialog datePicker = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);

                    plant.setLastWateredTimestamp(selectedDate.getTimeInMillis());

                    // Update storage
                    PlantStorage.savePlants(context, plantList);
                    notifyItemChanged(position);

                    Toast.makeText(context, context.getString(R.string.watering_date_updated), Toast.LENGTH_SHORT).show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.setTitle(context.getString(R.string.when_watered));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis()); // forbid future
        datePicker.show();
    }



    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlantName;
        ImageView ivPlant;
        TextView tvLastWatered;
        ImageView ivWaterIcon;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlantName = itemView.findViewById(R.id.tvPlantName);
            tvLastWatered = itemView.findViewById(R.id.tvLastWatered);
            ivWaterIcon = itemView.findViewById(R.id.ivWaterIcon);
            ivPlant = itemView.findViewById(R.id.ivPlant);
        }
    }
}


