package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUserDate extends AppCompatActivity {

    Button BT_ConsultarUsuario, BT_EncolarUsuario;
    TextView TV_ExisteUsuario;
    EditText ET_DocumentoUsuario;

    String uid;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_date);

        client = new AsyncHttpClient();

        BT_ConsultarUsuario = (Button) findViewById(R.id.BtnConsultarUsuario);
        BT_EncolarUsuario= (Button) findViewById(R.id.BtnEncolarUsuario);
        TV_ExisteUsuario = (TextView) findViewById(R.id.TextoExisteUsuario);
        ET_DocumentoUsuario = (EditText) findViewById(R.id.DocumentoEditText);

        BT_ConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String documento = ET_DocumentoUsuario.getText().toString().trim();
                if (documento.isEmpty()) {
                    ET_DocumentoUsuario.setError("Por favor, ingrese un número de documento.");
                } else {
                    uid = "";
                    checkIfUserExists( documento );
                }
            }
        });

        BT_ConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uid.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Para encolar un usuario primero debe consultar que está registrado.", Toast.LENGTH_SHORT).show();
                } else {
                    enqueueInstantUser();
                }
            }
        });
    }

    private void checkIfUserExists(String documento) {
        RequestParams params = new RequestParams();
        params.put("documento", documento);

        client.post("" , params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    uid = response.getString("id");
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(), "El usuario se encuentra registrado.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Problema al hacer la solicitud.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enqueueInstantUser() {
        RequestParams params = new RequestParams();
        params.put("id", uid);

        client.post("" , params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(getApplicationContext(), "El usuario ha sido registrado.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), "Ocurrió un error con el proceso. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}