package com.grupo2.photoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView imgFoto;
    EditText etDescripcion;
    Button  btnGuardar, btnVerFotos, btnTomarFoto;
    DBManager dbManager;

    ActivityResultLauncher<Intent> selectorImagenLauncher;

    private Uri fotoUri;
    ActivityResultLauncher<Uri> tomarFotoLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgFoto = findViewById(R.id.imgFoto);
        etDescripcion = findViewById(R.id.etDescripcion);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnVerFotos = findViewById(R.id.btnVerFotos);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);


        dbManager = new DBManager(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA}, 100);
        }


        // Selección de imagen
        selectorImagenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imagenUri = result.getData().getData();
                        try {
                            InputStream is = getContentResolver().openInputStream(imagenUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            imgFoto.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Lanzador para tomar foto con cámara
        tomarFotoLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        imgFoto.setImageURI(fotoUri);
                    } else {
                        Toast.makeText(this, "No se tomó la foto", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnTomarFoto.setOnClickListener(v -> {
            try {
                File file = File.createTempFile("foto_", ".jpg", getExternalCacheDir());
                fotoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                tomarFotoLauncher.launch(fotoUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al crear archivo", Toast.LENGTH_SHORT).show();
            }
        });




        btnGuardar.setOnClickListener(v -> {
            String descripcion = etDescripcion.getText().toString();
            if (imgFoto.getDrawable() == null || descripcion.isEmpty()) {
                Toast.makeText(this, "Debe seleccionar una foto y escribir una descripción", Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] imagenBytes = imageToByte(imgFoto);
            long resultado = dbManager.insertarFoto(imagenBytes, descripcion);
            if (resultado != -1) {
                Toast.makeText(this, "Foto guardada correctamente", Toast.LENGTH_SHORT).show();
                imgFoto.setImageDrawable(null);
                etDescripcion.setText("");
            } else {
                Toast.makeText(this, "Error al guardar la foto", Toast.LENGTH_SHORT).show();
            }
        });

        btnVerFotos.setOnClickListener(v -> {
            startActivity(new Intent(this, VerFotosActivity.class));
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] imageToByte(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
