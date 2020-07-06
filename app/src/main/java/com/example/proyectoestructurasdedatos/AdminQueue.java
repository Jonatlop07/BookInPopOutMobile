package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AdminQueue extends AppCompatActivity implements DatosConexion {

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);

    EditText ET_Size, ET_startHour, ET_endHour, ET_interval, ET_capacity;
    Button BT_CrearCola, BT_EliminarCola;
    ImageButton BT_HoraI, BT_HoraF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_queue);

        ET_Size = (EditText) findViewById(R.id.queueSize);
        ET_startHour = (EditText) findViewById(R.id.hourStart);
        ET_endHour = (EditText) findViewById(R.id.hourEnd);
        ET_interval = (EditText) findViewById(R.id.capacity);
        BT_CrearCola = (Button) findViewById(R.id.btnCrearCola);
        BT_HoraI = (ImageButton) findViewById(R.id.AdminQueueTimeS);
        BT_HoraF = (ImageButton) findViewById(R.id.AdminQueueTimeF);

        BT_CrearCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = ET_Size.getText().toString();
                String startHour = ET_startHour.getText().toString();
                String endHour = ET_endHour.getText().toString();
                String intervalLength = ET_interval.getText().toString();

                if (size.isEmpty()) {
                    ET_Size.setError("Ingrese el tamaño de la cola");
                    return;
                }
                if (startHour.isEmpty()) {
                    ET_startHour.setError("Ingrese la hora de inicio");
                    return;
                }
                if (endHour.isEmpty()) {
                    ET_endHour.setError("Ingrese la hora final");
                    return;
                }
                if (intervalLength.isEmpty()) {
                    ET_interval.setError("Ingrese los minutos por intervalo");
                    return;
                }

                RequestParams params = new RequestParams();
                params.put("tamanioCola", Integer.parseInt(size));
                params.put("horaInicial", startHour);
                params.put("horaFinal", endHour);
                params.put("minutosIntervalo", Integer.parseInt(intervalLength));

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


        BT_HoraI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHoraInicio();
            }
        });

        BT_HoraF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHoraFinal();
            }
        });

        BT_EliminarCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

                client.post(ELIMINAR_COLA, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "La cola ha sido eliminada.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "No hay una cola que eliminar.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void obtenerHoraInicio() {
        TimePickerDialog recogerFecha = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                ET_startHour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
        }, hora, 0, false);
        recogerFecha.show();
    }

    private void obtenerHoraFinal() {
        TimePickerDialog recogerFecha = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10

                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);

                ET_endHour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
        }, hora, 0, false);
        recogerFecha.show();
    }
}
