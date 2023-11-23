package com.example.mascara;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.Map;

public class ImageUploader {

    public static void uploadImageToCloudinaryAndCosmosDB(Context context, Uri imageUri) {
        // Genera un nombre único para la imagen utilizando la marca de tiempo actual
        String uniquePublicId = "image_" + System.currentTimeMillis();

        MediaManager.get().upload(imageUri)
                .option("public_id", uniquePublicId)
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

                        // Extraer la URL de la imagen desde el resultado de Cloudinary
                        String imageUrl = (String) resultData.get("url");

                        // Almacenar la URL en Azure Cosmos DB usando MongoDB
                        storeImageUrlInCosmosDB("NombreDeLaImagen", imageUrl);
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

    private static void storeImageUrlInCosmosDB(String imageName, String imageUrl) {
        // Usa tu cadena de conexión real
        String connectionString = "mongodb://nascrewdb:3ZOeHj8Jsr6IiO2kTar7fbuoY32H54Vvm5jVv70dW2B3WlTR3eK4wbZuUfNsvjlEQXHxLaJbaqLcACDbGmCzog==@nascrewdb.mongo.cosmos.azure.com:10255/?ssl=true&retrywrites=false&replicaSet=globaldb&maxIdleTimeMS=120000&appName=@nascrewdb@";

        // Conéctate a la base de datos
        try (com.mongodb.client.MongoClient mongoClient = MongoClients.create(connectionString)) {
            // Selecciona la base de datos
            MongoDatabase database = mongoClient.getDatabase("nascrewdb");

            // Selecciona la colección
            MongoCollection<Document> collection = database.getCollection("images");

            // Crea un documento con la información de la imagen
            Document imageDocument = new Document("name", imageName)
                    .append("url", imageUrl);

            // Inserta el documento en la colección
            collection.insertOne(imageDocument);
        } catch (Exception e) {
            // Maneja las excepciones aquí
            e.printStackTrace();
        }
    }
}

