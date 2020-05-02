package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminUserQuery extends AppCompatActivity {

    Button crearCola, actualizarCola, desencolarBTN;
    ListView listaCola;

    ArrayList<String[]> listadoOrdenado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_query);

        crearCola = (Button) findViewById(R.id.botonCrearCola);
        actualizarCola = (Button) findViewById(R.id.BotonActualizarLista);
        desencolarBTN = (Button) findViewById(R.id.BTNDesencolar);
        listaCola = (ListView) findViewById(R.id.listaColas);

        listaCola.setAdapter(new listAdapter(this,2));

        crearCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCola();
            }
        });

        actualizarCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCola();
            }
        });

        desencolarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desencolar();
            }
        });
    }

    private void createCola(){
        //ArrayList de Visualización vacío
        listadoOrdenado = new ArrayList<>();

        //Interfaz de cargando lista
        listaCola.setAdapter(new listAdapter(this,0));

        //Interfaz en caso que ocurra un error
        // listaCola.setAdapter(new listAdapter(this,1));

        //Inserte el código aquí :D



        listaCola.setAdapter(new listAdapter(this, listadoOrdenado));
    }

    private void updateCola(){
        //ArrayList de Visualización vacío
        listadoOrdenado = new ArrayList<>();

        //Interfaz de cargando lista
        listaCola.setAdapter(new listAdapter(this,0));

        //Interfaz en caso que ocurra un error
        // listaCola.setAdapter(new listAdapter(this,1));

        //Inserte el código aquí :D



        listaCola.setAdapter(new listAdapter(this, listadoOrdenado));
    }

    private void desencolar(){
        //Interfaz de cargando lista
        listaCola.setAdapter(new listAdapter(this,0));

        //Interfaz en caso que ocurra un error
        // listaCola.setAdapter(new listAdapter(this,1));

        //Inserte el código aquí :D



        listaCola.setAdapter(new listAdapter(this, listadoOrdenado));
    }

}
