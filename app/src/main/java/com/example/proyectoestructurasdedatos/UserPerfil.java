package com.example.proyectoestructurasdedatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

public class UserPerfil extends AppCompatActivity {

    EditText ET_Nombre, ET_Documento, ET_Cuenta;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_perfil);

        ET_Nombre = (EditText) findViewById(R.id.PerfilNombre);
        ET_Documento = (EditText) findViewById(R.id.PerfilDocumento);
        ET_Cuenta = (EditText) findViewById(R.id.PerfilCuenta);

        user = (FirebaseUser) getIntent().getExtras().get("user");
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadProfile(user.getUid());
    }

    private void loadProfile(String UID) {
        //Función que toma la ID para cargar los datos
    }
}
