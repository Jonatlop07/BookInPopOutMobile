package com.example.proyectoestructurasdedatos;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText edtDireccion;
    private final String HOST_IP = "";
    private final String CARPETA_SCRIPTS = "archivos_conexion_bd";

    TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerLink = (TextView) findViewById(R.id.tv_Registrese);

        edtDireccion = (EditText) findViewById(R.id.direccion);

    }

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistroDatos.class));
            }
        });

        ejecutarServicio("http://" + this.HOST_IP + "/"+ this.CARPETA_SCRIPTS + "/registrarUsuario.php");
    }

    public void iniciarSesion( View view ) {

    }

    private void ejecutarServicio( String URL ) {
        StringRequest solicitud = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("", edtDireccion.getText().toString()); //El primer argumento indica el
                                                                       // nombre del atributo en la tabla
                //Agregar la cantidad que se reciben de la interfaz
                return parametros;
            }
        };

        RequestQueue colaSolicitud = Volley.newRequestQueue(this);
        colaSolicitud.add(solicitud);
    }
}
