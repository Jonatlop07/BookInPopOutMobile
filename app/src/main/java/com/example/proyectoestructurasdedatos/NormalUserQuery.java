package com.example.proyectoestructurasdedatos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NormalUserQuery extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Button BT_CancelarCita, BT_ReservarCita, BT_CitasAntes, BT_Perfil;
    String URL = "";
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_query);

        mAuth = FirebaseAuth.getInstance();

        BT_CancelarCita = (Button) findViewById(R.id.cancelCita);
        BT_ReservarCita = (Button) findViewById(R.id.AddCita);
        BT_CitasAntes = (Button) findViewById(R.id.enterCola);
        BT_Perfil = (Button) findViewById(R.id.enterPerfil);

        BT_CancelarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelarCita();
            }
        });

        BT_ReservarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NormalUserQuery.this, userReserveDate.class);
                startActivity(intent);
            }
        });

        BT_CitasAntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalUserQuery.this, userCheckDates.class);
                startActivity(intent);
            }
        });

        BT_Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalUserQuery.this, UserPerfil.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        //Revisar el archivo para saber si tiene reserva o no
    }

    private boolean estaEncolado() {
        //Función para leer el archivo, y si tiene una hora se retorna true, si está vacío se retorna false
        return true;
    }

    private void cancelarCita() {
        //Código para enviar un post al servidor para sacar al usuario de la lista
        RequestParams params = new RequestParams();
        params.put("id", currentUser.getUid());
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(getApplicationContext(), "La cita ha sido cancelada con éxito.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Error al procesar la petición. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
