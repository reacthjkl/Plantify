# Plantify – Android App (Dummy-Implementation)

Dieses Repository enthält das Grundgerüst der „Plantify“-App mit allen Activities und der Navigation zwischen ihnen. Es handelt sich um eine **Dummy-Implementation** ohne echte Geschäftslogik, die nur XML-Templates, Java-Activity-Klassen und das AndroidManifest enthält.

## Projektübersicht

- **MainActivity**: Startseite mit RecyclerView von Dummy-Pflanzen und FloatingActionButton zum Hinzufügen
- **NewPlantActivity**: Seite „Neue Pflanze erfassen“ mit Suchfeld, Ergebnis-Liste und Button zur manuellen Eingabe
- **ManualEntryActivity**: Formularseite zur manuellen Eingabe von Art, Bild, Name, Standort und Gieß-Intervall

Alle weiteren Komponenten (Adapter, Item-Layouts, Manifest) sind auf Dummy-Daten ausgelegt, um Navigation und UI-Aufbau zu demonstrieren.

## Inhalt der Abgabe

Aufgrund des Upload-Limits von 20 MB wurde **nicht** das komplette Android-Studio-Projekt archiviert, sondern nur folgende Dateien:

```
/AndroidManifest.xml
/res/layout/activity_main.xml
/res/layout/item_plant.xml
/res/layout/activity_new_plant.xml
/res/layout/activity_manual_entry.xml

/java/com/example/plantify/MainActivity.java
/java/com/example/plantify/NewPlantActivity.java
/java/com/example/plantify/ManualEntryActivity.java
/java/com/example/plantify/PlantAdapter.java
```

## Hinweise

- **Dummy-Daten**: Alle Daten (z.B. Pflanzennamen) sind Platzhalter.
- **Navigation**: Intents verknüpfen die drei Activities.
- **Erweiterung**: Für echte Implementierung müssen Datenquellen, Bild-Picker, Persistenz (Room/SharedPreferences) etc. ergänzt werden.
