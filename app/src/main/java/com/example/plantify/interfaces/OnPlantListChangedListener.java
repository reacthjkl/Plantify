package com.example.plantify.interfaces;

/*
 * Author: Illia Soloviov
 *
 * The OnPlantListChangedListener interface provides a callback mechanism
 * to notify when the plant list has changed.
 * It is typically used to update the UI, for example by showing or hiding
 * an empty-state message depending on whether the list is empty.
 */
public interface OnPlantListChangedListener {
    void onPlantListChanged(boolean isEmpty);
}
