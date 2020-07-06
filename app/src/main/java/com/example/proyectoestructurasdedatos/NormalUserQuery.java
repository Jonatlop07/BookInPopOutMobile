package com.example.proyectoestructurasdedatos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cz.msebera.android.httpclient.Header;

public class NormalUserQuery extends AppCompatActivity implements DatosConexion {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    CardView BT_CancelarCita, BT_ReservarCita, BT_CitasAntes, BT_Entrar;
    TextView ET_Nombre, ET_Documento, ET_Disca, TV_Encolado;
    ImageButton BT_Perfil;

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
        TV_Encolado = (TextView) findViewById(R.id.textEncolado);

        ET_Disca.setVisibility(View.INVISIBLE);

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
                try {
                    InputStream inputStream = getApplicationContext().openFileInput("hora.txt");

                    if ( inputStream != null ) {
                        Toast.makeText(getApplicationContext(), "Entrada1.", Toast.LENGTH_SHORT).show();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        if ( (receiveString = bufferedReader.readLine()) != null ) {
                            Toast.makeText(getApplicationContext(), "Entrada2.", Toast.LENGTH_SHORT).show();
                            stringBuilder.append(receiveString);

                            RequestParams params = new RequestParams();
                            params.put("id", user.getUid());
                            params.put("hora", stringBuilder.toString().trim().split(":")[0]);
                            params.put("minuto", stringBuilder.toString().trim().split(":")[1]);

                            AsyncHttpClient client = new AsyncHttpClient();

                            client.post(CANCELAR_CITA, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    Toast.makeText(getApplicationContext(), "La cita ha sido cancelada con éxito.", Toast.LENGTH_SHORT).show();
                                    try {
                                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("hora.txt", Context.MODE_PRIVATE));
                                        outputStreamWriter.write("");
                                        outputStreamWriter.close();
                                    }
                                    catch (IOException e) {
                                        Log.e("Exception", "File write failed: " + e.toString());
                                    }
                                    TV_Encolado.setText("No estas en la cola");
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    Toast.makeText(getApplicationContext(), "Error al procesar la petición. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            TV_Encolado.setText("No estas en la cola");
                        }

                        inputStream.close();
                    }
                }
                catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "File not found: " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Can not read file: " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("login activity", "Can not read file: " + e.toString());
                }
            }
        });

        BT_ReservarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputStream inputStream = getApplicationContext().openFileInput("hora.txt");

                    if ( inputStream != null ) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        if ( bufferedReader.readLine() != null ) {
                            Toast.makeText(getApplicationContext(), "Ya tienes reservada una cita.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(NormalUserQuery.this, userReserveDate.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }

                        inputStream.close();
                    }
                } catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }
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

        AsyncHttpClient client = new AsyncHttpClient();

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
                    Toast.makeText(getApplicationContext(), "Here is the error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getApplicationContext(), responseString, Toast.LENGTH_LONG).show();
            }
        });

        try {
            InputStream inputStream = this.openFileInput("hora.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                if ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    TV_Encolado.setText("Estas en la cola. Hora: " + stringBuilder.toString());
                } else {
                    TV_Encolado.setText("No estas en la cola");
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }
}
