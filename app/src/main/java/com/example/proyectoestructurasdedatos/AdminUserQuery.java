package com.example.proyectoestructurasdedatos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminUserQuery extends AppCompatActivity {

    Button BT_regUser, BT_actualizarCola;
    ListView listaCola;
    ImageButton BT_Config;

    String URL = "";
    AsyncHttpClient client;

    String[][] listadoOrdenado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_query);

        client = new AsyncHttpClient();

        BT_regUser = (Button) findViewById(R.id.adminRegisUser);
        BT_actualizarCola = (Button) findViewById(R.id.adminActuLista);
        BT_Config = (ImageButton) findViewById(R.id.configCola);
        listaCola = (ListView) findViewById(R.id.listaColas);

        listaCola.setAdapter(new listAdapter(this, 2));

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

            }
        });

        BT_Config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /*private void ingresarDatosCola(){
         listadoOrdenado = new String[][2];
         //Interfaz de cargando lista
         listaCola.setAdapter(new listAdapter(this,0));
         //Interfaz en caso que ocurra un error
         // listaCola.setAdapter(new listAdapter(this,1));
         listadoOrdenado = colaDePrioridad.devolverInformacionUsuarios();
         if(listadoOrdenado == null){
             listaCola.setAdapter(new listAdapter(this, 2));
         }
         else{
             listaCola.setAdapter(new listAdapter(this, listadoOrdenado));
         }
     }*/

    private void solicitarCreacionCola () {
        RequestParams params = new RequestParams();
        params.put("tamanioCola", 0);
        params.put("horaInicial", 0);
        params.put("horaFinal", 0);
        params.put("minutosIntervalo", 0);
        params.put("capacidadTurno", 0);
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(getApplicationContext(), "La cola ha sido creada.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Problema al crear la cola. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void solicitarInfoUsuariosCola () {
        RequestParams params = new RequestParams();
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for ( int i = 0; i < response.length(); i++ ) {
                        JSONObject userInfo = response.getJSONObject( i );
                        String documento = userInfo.getString("documento");
                        String nombre = userInfo.getString("nombre");
                        String horaAtención = userInfo.getString("hora");
                        //Aquí iría el código para llenar el componente que muestra los usuarios encolados
                    }
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
    private void desencolarUsuario () {
        RequestParams params = new RequestParams();
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(getApplicationContext(), "El usuario ha sido atendido.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error al procesar la petición..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void actualizarCola(){
        listadoOrdenado = new String[colaDePrioridad.getLongitud()][2];
        //Interfaz de cargando lista
        listaCola.setAdapter(new listAdapter(this,0));
        //Interfaz en caso que ocurra un error
        // listaCola.setAdapter(new listAdapter(this,1));
        if (!colaDePrioridad.estaVacia()) {
            colaDePrioridad.alterarDistancia();
            colaDePrioridad.organizar();
            listadoOrdenado = colaDePrioridad.devolverInformacionUsuarios();
        }
        if(listadoOrdenado == null){
            listaCola.setAdapter(new listAdapter(this, 2));
        }
        else{
            listaCola.setAdapter(new listAdapter(this, listadoOrdenado));
        }
    }*/
}
/*
Constcola.getBufferQueueInstance().insertUser( new User(LocalTime.of(9, 10), "dsdasd", 3));
Constcola.getBufferQueueInstance().insertUser( new User(LocalTime.of(9, 15), "dsdasd", 3));
Constcola.getPriorityQueueInstance().insert(Constcola.getBufferQueueInstance().users[0].pushBack());
Constcola.getPriorityQueueInstance().insert(Constcola.getBufferQueueInstance().users[0].pushBack());
respuesta.println(Constcola.getPriorityQueueInstance().heap[0]);
respuesta.println(Constcola.getPriorityQueueInstance().heap[1]);

 */