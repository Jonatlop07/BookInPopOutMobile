package com.example.proyectoestructurasdedatos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements DatosConexion {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    EditText ET_email, ET_pass;
    RadioButton RB_NUser, RB_AUser;
    Button BT_continuar;
    private ProgressDialog progressDialog;

    final String URL = "";
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        ET_email = (EditText) findViewById(R.id.emailEditText);
        ET_pass = (EditText) findViewById(R.id.passEditText);
        RB_NUser = (RadioButton) findViewById(R.id.NUserRadio);
        RB_AUser = (RadioButton) findViewById(R.id.AUserRadio);
        BT_continuar = (Button) findViewById(R.id.botonContinuar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Configurando datos");
        BT_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccountCreated();
            }
        });

    }

    private void getAccountCreated() {
        progressDialog.show();
        final String email = ET_email.getText().toString().trim();
        String password = ET_pass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            ET_email.setError("Ingresa un correo electrónico");
            progressDialog.dismiss();
            return;
        } else if (!email.matches(emailPattern)) {
            ET_email.setError("Ingresa un correo electrónico válido");
            progressDialog.dismiss();
            return;
        }

        if (password.isEmpty()) {
            ET_pass.setError("Ingresa una contraseña");
            progressDialog.dismiss();
            return;
        } else if (password.length() < 6) {
            ET_pass.setError("Ingresa una contraseña con al menos 6 carácteres");
            progressDialog.dismiss();
            return;
        }

        if (RB_AUser.isChecked()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = mAuth.getCurrentUser();

                                RequestParams params = new RequestParams();
                                params.put("id_usuario", user.getUid());
                                params.put("correo", email);

                                client = new AsyncHttpClient();
                                client.post(REGISTRO_USUARIO_EMPRESARIAL, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        startActivity(new Intent(RegisterActivity.this, AdminUserQuery.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        user.delete();
                                        Toast.makeText(getApplicationContext(), "Problema al realizar el registro. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Ha ocurrido un error." + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
        } else {
            Intent intent = new Intent(RegisterActivity.this, NormalUserRegister.class);
            intent.putExtra("email", email);
            intent.putExtra("pass", password);
            startActivity(intent);
        }
    }
}