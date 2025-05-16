package com.example.plantify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy-Adapter für RecyclerView in MainActivity.
 * Zeigt eine statische Liste von Pflanzennamen mit Platzhalter-Bild.
 */
public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    // Dummy-Daten
    private List<String> plantNames = new ArrayList<>();

    public PlantAdapter() {
        // Hier ein paar Platzhalter einfügen
        plantNames.add("Ficus Elastica");
        plantNames.add("Monstera Deliciosa");
        plantNames.add("Sansevieria");
        plantNames.add("Pilea Peperomioides");
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_plant.xml „aufblasen“
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        // Daten ins ViewHolder-Layout eintragen
        String name = plantNames.get(position);
        holder.tvPlantName.setText(name);
        // Bild bleibt Platzhalter – hier könntest du später per Glide/Co. laden
        holder.ivPlantImage.setImageResource(android.R.drawable.ic_menu_gallery);
    }

    @Override
    public int getItemCount() {
        return plantNames.size();
    }

    /**
     * ViewHolder hält References auf die Views jedes Listenelements.
     */
    static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlantImage;
        TextView tvPlantName;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlantImage = itemView.findViewById(R.id.ivPlantImage);
            tvPlantName   = itemView.findViewById(R.id.tvPlantName);
        }
    }
}
