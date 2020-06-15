package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class AdminQueue extends AppCompatActivity implements DatosConexion {

    EditText ET_Size, ET_startHour, ET_endHour, ET_startMinute, ET_endMinute, ET_interval, ET_capacity;
    Button BT_CrearCola, BT_Desencolar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_queue);

        ET_Size = (EditText) findViewById(R.id.queueSize);
        ET_startHour = (EditText) findViewById(R.id.hourStart);
        ET_endHour = (EditText) findViewById(R.id.hourEnd);
        ET_startMinute = (EditText) findViewById(R.id.minuteStart);
        ET_endMinute = (EditText) findViewById(R.id.minuteEnd);
        ET_interval = (EditText) findViewById(R.id.interval);
        ET_capacity = (EditText) findViewById(R.id.capacity);
        BT_CrearCola = (Button) findViewById(R.id.btnCrearCola);
        BT_Desencolar = (Button) findViewById(R.id.btnDesencolar);

        BT_CrearCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = ET_Size.getText().toString();
                String startHour = ET_startHour.getText().toString();
                String startMinute = ET_startMinute.getText().toString();
                String endHour = ET_endHour.getText().toString();
                String endMinute = ET_endMinute.getText().toString();
                String intervalLength = ET_interval.getText().toString();
                String capacity = ET_capacity.getText().toString();

                if (size.isEmpty()) {
                    ET_Size.setError("Ingrese el tamaño de la cola");
                    return;
                }
                if (startHour.isEmpty()) {
                    ET_startHour.setError("Ingrese la hora de inicio");
                    return;
                }
                if (startMinute.isEmpty()) {
                    ET_startMinute.setError("Ingrese el  minuto de inicio");
                    return;
                }
                if (endHour.isEmpty()) {
                    ET_endHour.setError("Ingrese la hora final");
                    return;
                }
                if (endMinute.isEmpty()) {
                    ET_endMinute.setError("Ingrese el minuto de la hora final");
                    return;
                }
                if (intervalLength.isEmpty()) {
                    ET_interval.setError("Ingrese los minutos por intervalo");
                    return;
                }
                if (capacity.isEmpty()) {
                    ET_capacity.setError("Ingrese la capacidad máxima por intervalo");
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("tamanioCola", Integer.parseInt(size));
                params.put("horaInicial", startHour + ":" + startMinute);
                params.put("horaFinal", endHour + ":" + endMinute);
                params.put("minutosIntervalo", Integer.parseInt(intervalLength));
                params.put("capacidadTurno", Integer.parseInt(capacity));

                AsyncHttpClient client = new AsyncHttpClient();

                client.post(SOLICITUD_CREACION_COLA, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "La cola ha sido creada.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "Problema al crear la cola. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        BT_Desencolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
}

/*

 */