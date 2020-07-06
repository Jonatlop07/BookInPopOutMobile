package com.example.proyectoestructurasdedatos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
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
import com.loopj.android.http.RequestParams;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements DatosConexion {
    private FirebaseAuth mAuth;

    public final Calendar c = Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    int aStamp, mStamp, dStamp;

    EditText ET_Nombre,ET_Apellido,ET_Nacimiento,ET_Documento, ET_Correo, ET_Contra;
    RadioButton RB_AdminUser;
    Switch SW_disc;
    Button BT_registrar;
    ImageButton BT_fecha;

    private ProgressDialog progressDialog;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ET_Nombre = (EditText) findViewById(R.id.RegisterNombre);
        ET_Apellido = (EditText) findViewById(R.id.RegisterApellido);
        ET_Nacimiento = (EditText) findViewById(R.id.RegisterNacimiento);
        ET_Documento = (EditText) findViewById(R.id.RegisterIdentificar);
        ET_Correo = (EditText) findViewById(R.id.RegisterCorreo);
        ET_Contra = (EditText) findViewById(R.id.RegisterPassword);
        RB_AdminUser = (RadioButton) findViewById(R.id.RegisterAdminRadio);
        SW_disc = (Switch) findViewById(R.id.RegisterDiscapacidad);
        BT_registrar = (Button) findViewById(R.id.RegisterButton);
        BT_fecha = (ImageButton) findViewById(R.id.RegisterFechaButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");

        BT_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAccountCreated();
            }
        });

        BT_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

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

    private void getAccountCreated() {
        progressDialog.show();

        String name = ET_Nombre.getText().toString().trim();
        String lastname = ET_Apellido.getText().toString().trim();
        String nacimiento = ET_Nacimiento.getText().toString().trim();
        String document = ET_Documento.getText().toString().trim();
        String email = ET_Correo.getText().toString().trim();
        String password = ET_Contra.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(name.isEmpty()){
            ET_Nombre.setError("Ingresa un nombre.");
            progressDialog.dismiss();
            return;
        }
        if(lastname.isEmpty()){
            ET_Apellido.setError("Ingresa un apellido.");
            progressDialog.dismiss();
            return;
        }
        if(nacimiento.isEmpty()){
            ET_Nacimiento.setError("Ingresa una fecha de nacimiento.");
            progressDialog.dismiss();
            return;
        }
        if(document.isEmpty()){
            ET_Documento.setError("Ingresa un número de documento.");
            progressDialog.dismiss();
            return;
        }
        if(email.isEmpty()){
            ET_Correo.setError("Ingresa un correo electrónico.");
            progressDialog.dismiss();
            return;
        }
        else if(!email.matches(emailPattern)){
            ET_Correo.setError("Ingresa un correo electrónico válido.");
            progressDialog.dismiss();
            return;
        }
        if(password.isEmpty()){
            ET_Contra.setError("Ingresa un contraseña.");
            progressDialog.dismiss();
            return;
        }
        else if(password.length() < 6){
            ET_Contra.setError("Ingresa una contraseña con al menos 6 carácteres.");
            progressDialog.dismiss();
            return;
        }

        if (RB_AdminUser.isChecked()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();

                                RequestParams params = new RequestParams();
                                params.put("id_usuario", user.getUid());
                                params.put("correo", email);

                                AsyncHttpClient client = new AsyncHttpClient();
                                client.post(REGISTRO_USUARIO_EMPRESARIAL, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        startActivity(new Intent(RegisterActivity.this, AdminUserQuery.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        user.delete();
                                        Toast.makeText(getApplicationContext(), "Problema al realizar el registro. Por favor inténtelo de nuevo. " + error, Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Ha ocurrido un error." + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();

                                RequestParams params = new RequestParams();
                                params.put("id_usuario", user.getUid());
                                params.put("id_documento", document);
                                params.put("nombre", name);
                                params.put("apellido", lastname);
                                params.put("fecha_nacimiento", nacimiento);
                                params.put("discapacitado", SW_disc.isChecked());
                                params.put("correo", email);

                                AsyncHttpClient client = new AsyncHttpClient();

                                client.post(REGISTRO_USUARIO_NORMAL, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        startActivity(new Intent(RegisterActivity.this, NormalUserQuery.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        user.delete();
                                        Toast.makeText(getApplicationContext(), "Problema al realizar el registro. Por favor inténtelo de nuevo." + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Ha ocurrido un error." + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }
}