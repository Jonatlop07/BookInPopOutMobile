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
        implements DatosConexion {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    EditText ET_Documento;
    Button BT_ConsultarUsuario;
    TextView TV_NombreUsuario;
    TextView TV_ApellidoUsuario;
    TextView TV_CorreoUsuario;
    Button BT_IrGestionColas;

    RequestQueue colaSolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_query);

        mAuth = FirebaseAuth.getInstance();

        ET_Documento = (EditText) findViewById(R.id.DocConsultaEditText);
        BT_ConsultarUsuario = (Button) findViewById(R.id.BotonConsultarUsuario);
        TV_NombreUsuario = (TextView) findViewById(R.id.NombreTextView);
        TV_ApellidoUsuario = (TextView) findViewById(R.id.ApellidoTextView);
        TV_CorreoUsuario = (TextView) findViewById(R.id.CorreoTextView);
        BT_IrGestionColas = (Button) findViewById(R.id.IrAdminQuery);

        BT_ConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });
        BT_IrGestionColas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NormalUserQuery.this, AdminUserQuery.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    public void cargarWebService () {
        String documento = ET_Documento.getText().toString();

        if (documento.isEmpty()) {
            ET_Documento.setError("Ingrese un número de documento");
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
                    jsonObject = response.getJSONObject(0);
                    TV_NombreUsuario.setText(jsonObject.getString("nombre"));
                    TV_ApellidoUsuario.setText(jsonObject.getString("apellido"));
                    TV_CorreoUsuario.setText(jsonObject.getString("correo"));

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
}
