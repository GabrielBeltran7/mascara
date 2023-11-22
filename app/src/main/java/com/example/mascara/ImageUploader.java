package com.example.mascara;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import java.util.Map;

public class ImageUploader {

    public static void uploadImageToCloudinary(Context context, Uri imageUri) {
        MediaManager.get().upload(imageUri)
                .option("public_id", "image")  // Nombre único para la imagen
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // La carga ha comenzado
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Actualizar el progreso de la carga
                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        // Imagen subida exitosamente
                        Toast.makeText(context, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        // Error al subir la imagen
                        Toast.makeText(context, "Error al subir la imagen: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        // Reintentar la carga
                    }
                })
                .dispatch();
    }
}
