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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    EditText emailText, passText;
    Button initButton, registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailText = (EditText) findViewById(R.id.LoginemailEditText);
        passText = (EditText) findViewById(R.id.LoginpassEditText);
        initButton = (Button) findViewById(R.id.LoginbotonContinuar);
        registerButton = (Button) findViewById(R.id.botonRegistro);

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
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(this, NormalUserQuery.class));
        }
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
                            startActivity(new Intent(LoginActivity.this, NormalUserQuery.class));
                            progressDialog.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Error al Iniciar sesión." + task.getException(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });
        }

}
