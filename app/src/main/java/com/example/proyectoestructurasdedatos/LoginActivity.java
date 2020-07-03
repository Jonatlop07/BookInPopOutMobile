package com.example.proyectoestructurasdedatos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements DatosConexion {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    EditText emailText, passText;
    TextView registerButton;
    Button initButton;

    AsyncHttpClient client;
    final String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailText = (EditText) findViewById(R.id.LoginemailEditText);
        passText = (EditText) findViewById(R.id.LoginpassEditText);
        initButton = (Button) findViewById(R.id.LoginbotonContinuar);
        registerButton = (TextView) findViewById(R.id.botonRegistro);

        String formattedText = "¿Sin cuenta? Registrate oprimiendo <font color='#475299'>aquí</font>";

        registerButton.setText(Html.fromHtml(formattedText));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");

        initButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogIn();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
    }

    private void LogIn() {
        progressDialog.show();
        final String email = emailText.getText().toString().trim();
        String password = passText.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            emailText.setError("Ingresa un correo electrónico");
            progressDialog.dismiss();
            return;
        } else if (!email.matches(emailPattern)) {
            emailText.setError("Ingresa un correo electrónico válido");
            progressDialog.dismiss();
            return;
        }

        if (password.isEmpty()) {
            passText.setError("Ingresa una contraseña");
            progressDialog.dismiss();
            return;
        } else if (password.length() < 6) {
            passText.setError("Ingresa una contraseña con al menos 6 carácteres");
            progressDialog.dismiss();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            //verifyUserType(user.getUid());
                            RequestParams params = new RequestParams();
                            params.put("id", user.getUid());
                            params.put("email", email);
                            client = new AsyncHttpClient();
                            client.post(INICIO_SESION, params, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        boolean esEmpresarial = response.getBoolean("empleado");
                                        if (esEmpresarial) {
                                            startActivity(new Intent(LoginActivity.this, AdminUserQuery.class));
                                        } else {
                                            startActivity(new Intent(LoginActivity.this, NormalUserQuery.class));
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "Problema al intentar iniciar sesión. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    Toast.makeText(getApplicationContext(), "Problema al intentar iniciar sesión. Por favor inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Error al Iniciar sesión." + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}