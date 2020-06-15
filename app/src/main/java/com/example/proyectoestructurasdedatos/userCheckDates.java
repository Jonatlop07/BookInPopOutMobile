package com.example.proyectoestructurasdedatos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class userCheckDates extends AppCompatActivity implements DatosConexion {

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    int aStamp, mStamp, dStamp;

    private FirebaseAuth mAuth;
    FirebaseUser user;

    final String URL = "";
    AsyncHttpClient client;

    EditText etFecha;
    Button BT_Consultar;
    ImageButton BT_Fecha;
    ListView listaCola;
    String[] listadoHoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_dates);

        mAuth = FirebaseAuth.getInstance();

        etFecha = (EditText) findViewById(R.id.timeText2);
        BT_Fecha = (ImageButton) findViewById(R.id.dateButton);
        BT_Consultar = (Button) findViewById(R.id.checkDateButton);
        listaCola = (ListView) findViewById(R.id.listaColas);

        listaCola.setAdapter(new listAdapter(this, 2));

        BT_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });

        BT_Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fecha = etFecha.getText().toString().split("/");
                RequestParams params = new RequestParams();
                params.put("id", user.getUid());
                params.put("fecha", fecha[0] + fecha[1] + fecha[2]);

                client = new AsyncHttpClient();

                client.post(SOLICITUD_HISTORIAL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            listadoHoras = new String[response.length()];

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject attentionRegister = response.getJSONObject(i);
                                //Aquí iría el código para llenar el componente que muestra el registro de citas

                                String hora = "Hora: " + attentionRegister.getString("hora") + "\nAtendido: " + attentionRegister.getString("atendido");
                                listadoHoras[i] = hora;
                            }
                            listaCola.setAdapter(new listAdapter(getApplicationContext(), listadoHoras));
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Toast.makeText(getApplicationContext(), "Problema al realizar el registro. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
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


    private void obtenerFecha() {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual) : String.valueOf(mesActual);

                aStamp = year;
                mStamp = month;
                dStamp = dayOfMonth;
                etFecha.setText(year + "/" + mesFormateado + "/" + diaFormateado);
            }
        }, anio, mes, dia);
        recogerFecha.getDatePicker().setMaxDate(System.currentTimeMillis());
        recogerFecha.show();
    }
}
