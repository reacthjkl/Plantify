package com.example.plantify.helpers;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {
    public static Uri copyToInternalStorage(Context context, Uri sourceUri) {
        try {
            File destFile = new File(context.getFilesDir(), System.currentTimeMillis() + "_plant.jpg");
            try (InputStream in = context.getContentResolver().openInputStream(sourceUri);
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
