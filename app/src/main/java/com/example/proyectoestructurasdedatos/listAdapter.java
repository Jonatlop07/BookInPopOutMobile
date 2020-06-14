package com.example.proyectoestructurasdedatos;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class listAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private ProgressDialog progressDialog;

    private Context contexto;
    private String[][] datos;
    private int state = -1;

    public listAdapter(Context contexto, String[][] datos) {
        this.contexto = contexto;
        this.datos = datos;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    public listAdapter(Context contexto, int state) {
        this.contexto = contexto;
        this.state = state;

        inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View vista;
        if (state == 0) {
            vista = inflater.inflate(R.layout.elem_lista2, null);
        } else if (state == 1) {
            vista = inflater.inflate(R.layout.elem_lista1, null);
        } else if (state == 2) {
            vista = inflater.inflate(R.layout.elem_lista0, null);
        } else {
            if (datos == null) {
                vista = inflater.inflate(R.layout.elem_lista0, null);
            } else if (datos.length == 0) {
                vista = inflater.inflate(R.layout.elem_lista0, null);
            } else {
                vista = inflater.inflate(R.layout.elem_lista, null);

                TextView Nombre = (TextView) vista.findViewById(R.id.textoPerfil);
                TextView Documento = (TextView) vista.findViewById(R.id.textoAsign);
                TextView Pos = (TextView) vista.findViewById(R.id.textoFecha);

                Nombre.setText(datos[i][0]);
                Documento.setText(datos[i][1]);
                Pos.setText(String.valueOf(i + 1));
            }
        }
        return vista;
    }

    @Override
    public int getCount() {
        if (state < 0) {
            return (datos.length == 0) ? 1 : datos.length;
        } else {
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}