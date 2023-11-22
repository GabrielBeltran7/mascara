package com.example.mascara;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log; // Importa la clase Log
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar ImageView
        imageView = findViewById(R.id.imageView);

        // Verificar y solicitar permisos si es necesario
        checkAndRequestPermissions();

        // Agregar un registro de consola en el onCreate
        Log.d("MainActivity", "onCreate executed");
    }

    public void onSelectImageClick(View view) {
        // Crea un intent para abrir la galería de imágenes
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Lanza el intent y espera el resultado
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

        // Agregar un registro de consola en el onSelectImageClick
        Log.d("MainActivity", "onSelectImageClick executed");
    }

    // Método para manejar el resultado del intent (seleccionar imagen)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Obtener la URI de la imagen seleccionada
            Uri selectedImageUri = data.getData();

            // Mostrar la imagen en el ImageView
            imageView.setImageURI(selectedImageUri);
            imageView.setVisibility(View.VISIBLE);

            // Agregar un registro de consola en el onActivityResult
            Log.d("MainActivity", "onActivityResult executed");
        }
    }

    // Método para verificar y solicitar permisos en tiempo de ejecución (si es necesario)
    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);

                // Agregar un registro de consola al solicitar permisos
                Log.d("MainActivity", "Permission requested");
            }
        }
    }

    // Método para manejar la respuesta de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            // Verificar si el permiso fue concedido
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes realizar las acciones que requieren el permiso
                Log.d("MainActivity", "Storage permission granted");
            } else {
                // Permiso denegado, puedes informar al usuario o realizar alguna otra acción
                Log.d("MainActivity", "Storage permission denied");
            }
        }
    }
}
