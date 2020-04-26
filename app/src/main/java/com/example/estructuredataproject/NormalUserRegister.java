package com.example.estructuredataproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.regex.Pattern;

public class NormalUserRegister extends AppCompatActivity {

    EditText ET_Nombre, ET_Apellido, ET_Nacimiento;
    RadioButton RB_Discapacidad;
    Button BT_Finalizar;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String UID;

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    int aStamp, mStamp, dStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_register);

        mAuth = FirebaseAuth.getInstance();

        ET_Nombre = (EditText) findViewById(R.id.NombreEditText);
        ET_Apellido = (EditText) findViewById(R.id.ApellidoEditText);
        ET_Nacimiento = (EditText) findViewById(R.id.NacimientoEditText);
        RB_Discapacidad = (RadioButton) findViewById(R.id.DiscaSiRadio);
        BT_Finalizar = (Button) findViewById(R.id.BotonFinalizar);

        ET_Nacimiento.setText(dia + "/" + (mes + 1) + "/" + anio);

        BT_Finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarRegistro();
            }
        });

        ET_Nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                obtenerFecha();
                closeKeyboard();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        UID = user.getUid();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                ET_Nacimiento.setText(diaFormateado + "/" + mesFormateado + "/" + year);
            }
        },anio, mes, dia);
        recogerFecha.getDatePicker().setMaxDate(System.currentTimeMillis());
        recogerFecha.show();
    }

    private void finalizarRegistro(){
        String nombre = ET_Nombre.getText().toString().trim();
        String apellido = ET_Apellido.getText().toString().trim();
        String nacimiento = ET_Nacimiento.getText().toString().trim();
        Boolean discapacitado = RB_Discapacidad.isChecked();
        String datePattern = "[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]";

        if(nombre.isEmpty()){
            ET_Nombre.setError("Ingrese un nombre");
            return;
        }

        if(apellido.isEmpty()){
            ET_Apellido.setError("Ingrese un apellido");
            return;
        }


        if(nacimiento.isEmpty()){
            ET_Apellido.setError("Ingrese una Fecha de Nacimiento");
            return;
        }
        else if(!nacimiento.matches(datePattern)){
            ET_Nacimiento.setError("Ingrese una Fecha de Nacimiento válida");
            return;
        }

        //Codigo para subir los datos a la base de datos
        //UID es la Primary Key del Usuario (Dada por Authentication)

        Toast.makeText(this, "Funcionó", Toast.LENGTH_SHORT).show();
    }
}
