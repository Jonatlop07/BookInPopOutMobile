package com.example.proyectoestructurasdedatos;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    EditText ET_email, ET_pass;
    RadioButton RB_NUser, RB_AUser;
    Button BT_continuar;
    private ProgressDialog progressDialog;

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

        Intent intent = new Intent(MainActivity.this, NormalUserRegister.class);
        intent.putExtra("email", email);
        intent.putExtra("pass", password);
        startActivity(intent);
    }
}
