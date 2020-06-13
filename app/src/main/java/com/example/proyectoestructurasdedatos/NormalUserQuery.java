package com.example.proyectoestructurasdedatos;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoestructurasdedatos.utilidades.DatosConexion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NormalUserQuery extends AppCompatActivity
       /* implements DatosConexion*/ {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button BT_CancelarCita, BT_ReservarCita, BT_CitasAntes, BT_Perfil;

    RequestQueue colaSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_query);

        mAuth = FirebaseAuth.getInstance();

       BT_CancelarCita = (Button) findViewById(R.id.cancelCita);
       BT_ReservarCita = (Button) findViewById(R.id.AddCita);
       BT_CitasAntes = (Button) findViewById(R.id.enterCola);
       BT_Perfil = (Button) findViewById(R.id.enterPerfil);

       BT_CancelarCita.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                cancelarCita();
           }
       });

       BT_ReservarCita.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                startActivity(new Intent(NormalUserQuery.this, userReserveDate.class));
           }
       });

       BT_CitasAntes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       BT_Perfil.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });
    }

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

        //Revisar el archivo para saber si tiene reserva o no
    }

    private void cancelarCita(){
        //Función para enviar un post al servidor para sacar al usuario de la lista

    }


/*

    public void cargarWebService () {
        String documento = "";

        if (documento.isEmpty()) {
            //
        } else {
            consultarUsuario();
        }
    }

    private void consultarUsuario() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_CONSULTA_USUARIO + ET_Documento.getText(),
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                try {
                    //temp
                    return;

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
            }
        });

        colaSolicitud = Volley.newRequestQueue(this);
        colaSolicitud.add(jsonArrayRequest);
    }



 */

}
