package com.example.proyectoestructurasdedatos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class NormalUserRegister extends AppCompatActivity implements DatosConexion {

    EditText ET_Nombre, ET_Apellido, ET_Nacimiento, ET_Documento;
    RadioButton RB_Discapacidad;
    Button BT_Finalizar;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    int aStamp, mStamp, dStamp;

    private ProgressDialog progressDialog;
    private String correo;

    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_register);

        this.correo = this.getIntent().getExtras().getString("email");

        mAuth = FirebaseAuth.getInstance();

        ET_Nombre = (EditText) findViewById(R.id.NombreEditText);
        ET_Apellido = (EditText) findViewById(R.id.ApellidoEditText);
        ET_Nacimiento = (EditText) findViewById(R.id.NacimientoEditText);
        ET_Documento = (EditText) findViewById(R.id.DocumentoEditText);
        RB_Discapacidad = (RadioButton) findViewById(R.id.DiscaSiRadio);
        BT_Finalizar = (Button) findViewById(R.id.BotonFinalizar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Finalizando registro");

        ET_Nacimiento.setText(anio + "/" + (mes + 1) + "/" + dia);
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


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
                ET_Nacimiento.setText(year + "/" + mesFormateado + "/" + diaFormateado);
            }
        }, anio, mes, dia);
        recogerFecha.getDatePicker().setMaxDate(System.currentTimeMillis());
        recogerFecha.show();
    }

    private void finalizarRegistro() {

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("pass");
        String nombre = ET_Nombre.getText().toString().trim();
        String apellido = ET_Apellido.getText().toString().trim();
        String nacimiento = ET_Nacimiento.getText().toString().trim();
        String documento = ET_Documento.getText().toString().trim();
        Boolean discapacitado = RB_Discapacidad.isChecked();
        String datePattern = "[0-9][0-9][0-9][0-9]/[0-9][0-9]/[0-9][0-9]";

        if (nombre.isEmpty()) {
            ET_Nombre.setError("Ingrese un nombre");
            return;
        }

        if (apellido.isEmpty()) {
            ET_Apellido.setError("Ingrese un apellido");
            return;
        }

        if (nacimiento.isEmpty()) {
            ET_Apellido.setError("Ingrese una Fecha de Nacimiento");
            return;
        } else if (!nacimiento.matches(datePattern)) {
            ET_Nacimiento.setError("Ingrese una Fecha de Nacimiento válida");
            return;
        }

        if (documento.isEmpty()) {
            ET_Apellido.setError("Ingrese un Número de Identificación");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            RequestParams params = new RequestParams();
                            params.put("id_usuario", user.getUid());
                            params.put("id_documento", ET_Documento.getText().toString().trim());
                            params.put("nombre", ET_Nombre.getText().toString().trim());
                            params.put("apellido", ET_Apellido.getText().toString().trim());
                            params.put("fecha_nacimiento", ET_Nacimiento.getText().toString().trim());
                            params.put("discapacitado", RB_Discapacidad.isChecked());
                            params.put("correo", correo);
                            client = new AsyncHttpClient();
                            client.post(REGISTRO_USUARIO_NORMAL, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    startActivity(new Intent(NormalUserRegister.this, NormalUserQuery.class));
                                    finish();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    user.delete();

                                    Toast.makeText(getApplicationContext(), "Problema al realizar el registro. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(NormalUserRegister.this, "Ha ocurrido un error." + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}