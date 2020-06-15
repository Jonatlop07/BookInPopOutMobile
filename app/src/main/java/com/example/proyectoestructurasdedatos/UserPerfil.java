package com.example.proyectoestructurasdedatos;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class UserPerfil extends AppCompatActivity implements DatosConexion {

    EditText ET_Nombre, ET_Documento, ET_FechaNacimiento;
    RadioButton RB_Discapacidad, RB_NoDiscapacidad;
    Button BT_Editar, BT_EnviarCambios;

    public final Calendar c = Calendar.getInstance();
    int aStamp, mStamp, dStamp;

    private FirebaseAuth mAuth;
    FirebaseUser user;

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);

        mAuth = FirebaseAuth.getInstance();

        ET_Nombre = (EditText) findViewById(R.id.PerfilNombre);
        ET_FechaNacimiento = (EditText) findViewById(R.id.PerfilNacimiento);
        ET_Documento = (EditText) findViewById(R.id.DocumentoEditText);
        RB_Discapacidad = (RadioButton) findViewById(R.id.DiscaSiRadio);
        RB_NoDiscapacidad = (RadioButton) findViewById(R.id.DiscaNoRadio);
        BT_Editar = (Button) findViewById(R.id.editProfile);
        BT_EnviarCambios = (Button) findViewById(R.id.sendProfile);

        BT_Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BT_Editar.setVisibility(View.INVISIBLE);
                BT_EnviarCambios.setVisibility(View.VISIBLE);
                editableComponents();
            }
        });

        BT_EnviarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BT_EnviarCambios.setVisibility(View.INVISIBLE);
                BT_Editar.setVisibility(View.VISIBLE);

                RequestParams params = new RequestParams();
                params.put("id_usuario", user.getUid());
                params.put("id_documento", ET_Documento.getText().toString().trim());
                params.put("nombre", ET_Nombre.getText().toString().trim());
                params.put("fecha_nacimiento", ET_FechaNacimiento.getText().toString().trim());
                params.put("discapacitado", RB_Discapacidad.isChecked());
                client = new AsyncHttpClient();

                client.post(ACTUALIZAR_USUARIO_NORMAL, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show();
                        uneditableComponents();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        Toast.makeText(getApplicationContext(), "Problema al modificar el perfil. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        RequestParams params = new RequestParams();
        params.put("id", user.getUid());

        client = new AsyncHttpClient();

        client.post(INFORMACION_USUARIO, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    //Mostrar estos datos en pantalla
                    BT_EnviarCambios.setVisibility(View.INVISIBLE);
                    //String[] fecha = response.getString("fecha_nacimiento").split("-");
                    //ET_FechaNacimiento.setText(fecha[0] + "/" + fecha[1] + "/" + fecha[2]);
                    ET_Nombre.setText(response.getString("nombre"));
                    ET_Documento.setText(response.getString("documento"));
                    if (response.getBoolean("discapacitado")){
                        RB_Discapacidad.setChecked(true);
                        RB_NoDiscapacidad.setChecked(false);
                    } else {
                        RB_NoDiscapacidad.setChecked(true);
                        RB_Discapacidad.setChecked(false);
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
    }

    private void uneditableComponents() {
        ET_Nombre.setFocusable(false);
        ET_Nombre.setEnabled(false);
        ET_Nombre.setCursorVisible(false);
        ET_Nombre.setBackgroundColor(Color.TRANSPARENT);
        ET_FechaNacimiento.setFocusable(false);
        ET_FechaNacimiento.setEnabled(false);
        ET_FechaNacimiento.setCursorVisible(false);
        ET_FechaNacimiento.setBackgroundColor(Color.TRANSPARENT);
        ET_Documento.setFocusable(false);
        ET_Documento.setEnabled(false);
        ET_Documento.setCursorVisible(false);
        ET_Documento.setBackgroundColor(Color.TRANSPARENT);
        RB_Discapacidad.setEnabled(false);
        RB_NoDiscapacidad.setEnabled(false);
    }

    private void editableComponents() {
        ET_Nombre.setFocusableInTouchMode(true);
        ET_Nombre.setEnabled(true);
        ET_Nombre.setCursorVisible(true);
        ET_Nombre.setBackgroundColor(Color.TRANSPARENT);
        ET_FechaNacimiento.setFocusableInTouchMode(true);
        ET_FechaNacimiento.setEnabled(true);
        ET_FechaNacimiento.setCursorVisible(true);
        ET_FechaNacimiento.setBackgroundColor(Color.TRANSPARENT);
        ET_Documento.setFocusableInTouchMode(true);
        ET_Documento.setEnabled(true);
        ET_Documento.setCursorVisible(true);
        ET_Documento.setBackgroundColor(Color.TRANSPARENT);
        RB_Discapacidad.setEnabled(true);
        RB_NoDiscapacidad.setEnabled(true);
    }
}