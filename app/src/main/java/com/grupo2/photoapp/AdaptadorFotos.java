package com.grupo2.photoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdaptadorFotos extends BaseAdapter {

    private Context context;
    private List<Photograph> lista;

    public AdaptadorFotos(Context context, List<Photograph> lista) {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lista.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_foto, null);

        ImageView imageView = vista.findViewById(R.id.imgItem);
        TextView textView = vista.findViewById(R.id.txtDescripcion);

        Photograph foto = lista.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(foto.getImage(), 0, foto.getImage().length);
        imageView.setImageBitmap(bitmap);
        textView.setText(foto.getDescription());

        return vista;
    }
}
