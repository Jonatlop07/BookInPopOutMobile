package com.example.proyectoestructurasdedatos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class userCheckDates extends AppCompatActivity {

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    int aStamp, mStamp, dStamp;

    FirebaseUser currentUser;
    final String URL = "";
    AsyncHttpClient client;

    EditText etFecha;
    Button BT_Consultar;
    ImageButton BT_Fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_dates);

        etFecha = (EditText) findViewById(R.id.timeText2);
        BT_Fecha = (ImageButton) findViewById(R.id.dateButton);
        BT_Consultar = (Button) findViewById(R.id.checkDateButton);

        BT_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });

        BT_Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendConsulta(currentUser.getUid());
            }
        });

        currentUser = (FirebaseUser) this.getIntent().getExtras().get("user");
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

    private void sendConsulta(String uid) {
        RequestParams params = new RequestParams();
        params.put("id", uid);
        params.put("fecha", etFecha.getText());
        client.post(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject attentionRegister = response.getJSONObject(i);
                        //Aquí iría el código para llenar el componente que muestra el registro de citas
                        // String hora = attentionRegister.getString("hora");
                    }
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
}
