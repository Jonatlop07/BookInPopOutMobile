package com.example.proyectoestructurasdedatos;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class userReserveDate extends AppCompatActivity implements DatosConexion {

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);

    EditText etHora;
    Button BT_Reservar;
    ImageButton BT_Hora;

    private FirebaseAuth mAuth;
    FirebaseUser user;

    String URL = "";
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reserve_date);

        mAuth = FirebaseAuth.getInstance();

        etHora = (EditText) findViewById(R.id.timeText);
        BT_Hora = (ImageButton) findViewById(R.id.hourButton);
        BT_Reservar = (Button) findViewById(R.id.reservoirButton);

        BT_Hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerHora();
            }
        });

        BT_Reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                params.put("id", user.getUid());
                params.put("hora", etHora.getText().toString().split(":")[0]);
                params.put("minuto", etHora.getText().toString().split(":")[1]);

                client = new AsyncHttpClient();

                client.post(SOLICITUD_ENCOLAMIENTO, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            boolean encolado = response.getBoolean("encolado");
                            if (encolado) {
                                //Aquí va el código para guardar la hora en el archivo

                                Toast.makeText(getApplicationContext(), "Tu cita ha sido agendada para la hora que indicaste.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "La hora ingresada se encuentra entre una franja de horario ocupada. Por favor ingrese otra hora o intente de nuevo más tarde.", Toast.LENGTH_LONG).show();
                            }
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
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        //Revisar el archivo para saber si tiene reserva o no
    }

    private void obtenerHora() {
        TimePickerDialog recogerFecha = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10

                hourOfDay = (hourOfDay == 24 || hourOfDay == 0) ? 24 : hourOfDay % 24;

                String horaFormateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario


                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }
        }, hora, 0, false);
        recogerFecha.show();
    }
}
