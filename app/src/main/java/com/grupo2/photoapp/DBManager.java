package com.grupo2.photoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBManager {
    private DBHelper dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertarFoto(byte[] image, String description) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", image);
        values.put("description", description);
        return db.insert("photograph", null, values);
    }

    public ArrayList<Photograph> obtenerFotos() {
        ArrayList<Photograph> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM photograph", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            byte[] img = cursor.getBlob(1);
            String desc = cursor.getString(2);
            lista.add(new Photograph(id, img, desc));
        }
        cursor.close();
        return lista;
    }
}
