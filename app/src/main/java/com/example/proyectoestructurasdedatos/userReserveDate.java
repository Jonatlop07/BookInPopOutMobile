package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;

public class userReserveDate extends AppCompatActivity {

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    public final Calendar c = Calendar.getInstance();
    final int hora = c.get(Calendar.HOUR_OF_DAY);

    EditText etHora;
    Button BT_Reservar;
    ImageButton BT_Hora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reserve_date);

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
                sendReservacion();
            }
        });
    }

    private void obtenerHora(){
        TimePickerDialog recogerFecha = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

                hourOfDay = (hourOfDay == 12 || hourOfDay == 0) ?  12 : hourOfDay % 12;

                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario


                //Muestro la hora con el formato deseado
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }
        }, hora, 0, false);
        recogerFecha.show();
    }

    private void sendReservacion(){
        //Función para enviar los datos de la reservación

    }
}
