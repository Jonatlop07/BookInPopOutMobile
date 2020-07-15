package com.example.proyectoestructurasdedatos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class listAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private ProgressDialog progressDialog;

    private Context contexto;
    private String[] datos;
    private int state = -1;

    public listAdapter(Context contexto, String[] datos) {
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

                TextView Dato = (TextView) vista.findViewById(R.id.textoPerfil);
                TextView Pos = (TextView) vista.findViewById(R.id.textoFecha);
                ImageButton Sacar = (ImageButton) vista.findViewById(R.id.unHeapListButton);

                Dato.setTextColor(Color.BLACK);
                Pos.setTextColor(Color.BLACK);

                Dato.setText(datos[i]);
                Pos.setText(String.valueOf(i + 1));

                final int usuarioPos = i;
                Sacar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(contexto)
                                .setTitle("Sacar usuario")
                                .setMessage("¿Está seguro de sacar este usuario?")

                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Codigo para sacar a una persona en específico
                                        //Utilice el usuarioPos para buscar, en el array del listado, la ID del usuario.
                                    }
                                })

                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                })
                                .setIcon(R.drawable.ic_delete)
                                .show();
                    }
                });


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