package com.example.proyectoestructurasdedatos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class listAdapter extends BaseAdapter {


    private static LayoutInflater inflater = null;
    private ProgressDialog progressDialog;

    private Context contexto;
    private ArrayList<String[]> datos;
    private int state = -1;

    public listAdapter(Context contexto, ArrayList<String[]> datos) {
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
        if(datos.size() == 0){
            if(state == 0){
                vista = inflater.inflate(R.layout.elem_lista2, null);
            }
            else if(state == 1){
                vista = inflater.inflate(R.layout.elem_lista1, null);
            }
            else{
                vista = inflater.inflate(R.layout.elem_lista0, null);
            }
        }
        else{
            vista = inflater.inflate(R.layout.elem_lista, null);

            TextView Nombre = (TextView) vista.findViewById(R.id.textoPerfil);
            TextView Documento = (TextView) vista.findViewById(R.id.textoAsign);
            TextView Pos = (TextView) vista.findViewById(R.id.textoFecha);

            Nombre.setText(datos.get(i)[0]);
            Documento.setText(datos.get(i)[1]);
            Pos.setText(i);
        }

        return vista;
    }

    @Override
    public int getCount() {
        return (datos.size() == 0) ? 1 : datos.size();
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