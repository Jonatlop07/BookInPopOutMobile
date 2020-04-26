package com.example.estructuredataproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    EditText ET_email, ET_pass;
    RadioButton RB_NUser, RB_AUser;
    Button BT_continuar;
    String UID;
    Boolean authError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        ET_email = (EditText) findViewById(R.id.emailEditText);
        ET_pass = (EditText) findViewById(R.id.passEditText);
        RB_NUser = (RadioButton) findViewById(R.id.NUserRadio);
        RB_AUser = (RadioButton) findViewById(R.id.AUserRadio);
        BT_continuar = (Button) findViewById(R.id.botonContinuar);

        BT_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccountCreated();
            }
        });
    }

    private void getAccountCreated() {
        String email = ET_email.getText().toString().trim();
        String password = ET_pass.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            ET_email.setError("Ingresa un correo electrónico");
            return;
        } else if (!email.matches(emailPattern)) {
            ET_email.setError("Ingresa un correo electrónico válido");
            return;
        }

        if (password.isEmpty()) {
            ET_pass.setError("Ingresa una contraseña");
            return;
        } else if (password.length() < 5) {
            ET_pass.setError("Ingresa una contraseña con al menos 5 carácteres");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(MainActivity.this, "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                            authError = true;
                        }
                    }
                });

        if(authError){
            return;
        }

        UID = user.getUid();

        Boolean NUser = RB_NUser.isChecked();

        if(NUser){
            startActivity(new Intent(MainActivity.this, NormalUserRegister.class));
        }
        else{
            startActivity(new Intent(MainActivity.this, null));
        }
    }
}

