package com.grupo2.photoapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class VerFotosActivity extends AppCompatActivity {

    ListView listView;
    DBManager dbManager;
    ArrayList<Photograph> listaFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_fotos);

        listView = findViewById(R.id.listView);
        dbManager = new DBManager(this);

        listaFotos = dbManager.obtenerFotos();

        AdaptadorFotos adaptador = new AdaptadorFotos(this, listaFotos);
        listView.setAdapter(adaptador);
    }
}
