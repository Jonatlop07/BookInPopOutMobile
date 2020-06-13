package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class userCheckDates extends AppCompatActivity {

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    int aStamp, mStamp, dStamp;

    EditText etFecha;
    Button BT_Consultar;
    ImageButton BT_Fecha;

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check_dates);

        mAuth = FirebaseAuth.getInstance();

        etFecha = (EditText) findViewById(R.id.timeText);
        BT_Fecha = (ImageButton) findViewById(R.id.hourButton);
        BT_Consultar = (Button) findViewById(R.id.reservoirButton);

        BT_Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFecha();
            }
        });

        BT_Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendConsulta();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int mesActual = month + 1;
                String diaFormateado = (dayOfMonth < 10) ? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10) ? "0" + String.valueOf(mesActual):String.valueOf(mesActual);

                aStamp = year;
                mStamp = month;
                dStamp = dayOfMonth;
                etFecha.setText(year + "/" + mesFormateado + "/" + diaFormateado);
            }
        },anio, mes, dia);
        recogerFecha.getDatePicker().setMaxDate(System.currentTimeMillis());
        recogerFecha.show();
    }

    private void sendConsulta(){
        //Función
    }
}