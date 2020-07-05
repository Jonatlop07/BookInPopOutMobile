package com.example.proyectoestructurasdedatos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminUserQuery extends AppCompatActivity implements DatosConexion {

    ListView listaCola;
    ImageButton BT_actualizarCola, BT_Config, BT_regUser, BT_sacarUser;

    AsyncHttpClient client;

    String[] listadoOrdenado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_query);

        BT_regUser = (ImageButton) findViewById(R.id.registerAdminPersonButton);
        BT_actualizarCola = (ImageButton) findViewById(R.id.updateHeapAdminButton);
        BT_Config = (ImageButton) findViewById(R.id.ConfigHeapButton);
        BT_sacarUser = (ImageButton) findViewById(R.id.takeOutAdminButton);
        listaCola = (ListView) findViewById(R.id.listaColas);

        listaCola.setAdapter(new listAdapter(this, 2));

        BT_sacarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();

                client.get(SOLICITUD_DESENCOLAMIENTO, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "El usuario ha sido desencolado.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error al procesar la petición..", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        BT_regUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ir a la actividad de Registrar Usuario
                startActivity(new Intent(AdminUserQuery.this, RegisterUserDate.class));
            }
        });

        BT_actualizarCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = new AsyncHttpClient();
                client.get(REQUERIR_INFORMACION_COLA, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            listaCola.setAdapter(new listAdapter(getApplicationContext(), 2));
                            listadoOrdenado = new String[response.length()];

                            for ( int i = 0; i < response.length(); i++ ) {
                                JSONObject userInfo = response.getJSONObject( i );
                                String documento = userInfo.getString("documento");
                                String nombre = userInfo.getString("nombre");
                                String hora = userInfo.getString("hora");;
                                //Aquí iría el código para llenar el componente que muestra los usuarios encolados
                                listadoOrdenado[i] = "Documento: " + documento + "\nNombre: " + nombre + "\nHora: " + hora;
                            }

                            listaCola.setAdapter(new listAdapter(getApplicationContext(), listadoOrdenado));
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error al procesar la petición.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        BT_Config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminUserQuery.this, AdminQueue.class));
            }
        });

    }
}