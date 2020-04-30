package com.example.proyectoestructurasdedatos;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NormalUserQuery extends AppCompatActivity {
    EditText ET_Documento;
    Button BT_ConsultarUsuario;
    TextView TF_NombreUsuario;
    TextView TF_ApellidoUsuario;
    TextView TF_CorreoUsuario;

    RequestQueue colaSolicitud;

    private final String HOST_IP = "192.168.1.15";
    private final String CARPETA_SCRIPTS = "archivos_conexion_bd";
    private final String PUERTO = "80";
    private final String NOMBRE_SCRIPT = "consultarUsuarios";
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_query);

        ET_Documento = (EditText) findViewById(R.id.DocConsultaEditText);
        BT_ConsultarUsuario = (Button) findViewById(R.id.BotonConsultarUsuario);
        TF_NombreUsuario = (TextView) findViewById(R.id.NombreTextView);
        TF_ApellidoUsuario = (TextView) findViewById(R.id.ApellidoTextView);
        TF_CorreoUsuario = (TextView) findViewById(R.id.CorreoTextView);
        BT_ConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDocumento();
            }
        });
    }

    private void validarDocumento() {
        String documento = ET_Documento.getText().toString();

        if (documento.isEmpty()) {
            ET_Documento.setError("Ingrese un número de documento");
        } else {
            this.URL = "http://" + HOST_IP + ":" + PUERTO + "/" + CARPETA_SCRIPTS
                        + "/" + NOMBRE_SCRIPT + ".php?id_documento=" + ET_Documento.getText() + "";
            consultarUsuario();
        }
    }

    private void consultarUsuario() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(this.URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                try {
                    for (int i = 0; i < response.length(); i++) {
                        jsonObject = response.getJSONObject(i);
                        TF_NombreUsuario.setText(jsonObject.getString("nombre"));
                        TF_ApellidoUsuario.setText(jsonObject.getString("apellido"));
                        TF_CorreoUsuario.setText(jsonObject.getString("correo"));
                    }
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
