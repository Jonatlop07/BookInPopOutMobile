package com.example.proyectoestructurasdedatos;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserPerfil extends AppCompatActivity {

    EditText ET_Nombre, ET_Documento, ET_Cuenta;
    FirebaseUser user;

    String URL = "";
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);

        client = new AsyncHttpClient();

        ET_Nombre = (EditText) findViewById(R.id.PerfilNombre);
        ET_Documento = (EditText) findViewById(R.id.PerfilDocumento);
        ET_Cuenta = (EditText) findViewById(R.id.PerfilCuenta);

        user = (FirebaseUser) getIntent().getExtras().get("user");
    }

    @Override
    protected void onStart() {
        super.onStart();
        consultarInfoPerfil();
    }

    private void consultarInfoPerfil() {
        //Aquí se debe tomar la hora y el minuto del tiempo seleccionado
        RequestParams params = new RequestParams();
        params.put("id", user.getUid());
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    //Mostrar estos datos en pantalla
                    response.getString("nombre");
                    response.getString("apellidos");
                    response.getString("documento");
                    response.getString("fechaNacimiento");
                    response.getString("direccion");
                    response.getBoolean("discapacitado");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
