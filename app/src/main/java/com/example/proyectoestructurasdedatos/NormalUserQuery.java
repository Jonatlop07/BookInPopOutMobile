package com.example.proyectoestructurasdedatos;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NormalUserQuery extends AppCompatActivity implements DatosConexion {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    CardView BT_CancelarCita, BT_ReservarCita, BT_CitasAntes, BT_Entrar;
    TextView ET_Nombre, ET_Documento, ET_Disca;
    ImageButton BT_Perfil;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_query);

        mAuth = FirebaseAuth.getInstance();

        BT_CancelarCita = (CardView) findViewById(R.id.outButton);
        BT_ReservarCita = (CardView) findViewById(R.id.RegisterDateButton);
        BT_CitasAntes = (CardView) findViewById(R.id.PastButton);
        BT_Entrar = (CardView) findViewById(R.id.EnterButton);
        BT_Perfil = (ImageButton) findViewById(R.id.ProfileButton);

        ET_Nombre = (TextView) findViewById(R.id.NombreUsuario);
        ET_Documento = (TextView) findViewById(R.id.documentoUsuario);
        ET_Disca = (TextView) findViewById(R.id.discapacitadoOnOff);

        ET_Disca.setVisibility(View.INVISIBLE);
        BT_CancelarCita.setEnabled(false);

        BT_Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalUserQuery.this, UserPerfil.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        BT_CancelarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.put("id", user.getUid());

                client = new AsyncHttpClient();

                client.post(CANCELAR_CITA, params, new JsonHttpResponseHandler() {
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
        });

        BT_ReservarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalUserQuery.this, userReserveDate.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        BT_CitasAntes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NormalUserQuery.this, userCheckDates.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        BT_Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Codigo para entrar a la cola
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        RequestParams params = new RequestParams();
        String id = user.getUid();
        params.put("id", id);

        client = new AsyncHttpClient();

        client.post(INFORMACION_USUARIO, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    ET_Nombre.setText(response.getString("nombre"));
                    ET_Documento.setText(response.getString("documento"));
                    if (response.getBoolean("discapacitado")){
                        ET_Disca.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });

        //Revisar el archivo para saber si tiene reserva o no
    }

    private boolean estaEncolado() {
        //Función para leer el archivo, y si tiene una hora se retorna true, si está vacío se retorna false
        return true;
    }
}
