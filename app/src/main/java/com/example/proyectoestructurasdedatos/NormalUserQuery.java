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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NormalUserQuery extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    EditText ET_Documento;
    Button BT_ConsultarUsuario;
    TextView TV_NombreUsuario;
    TextView TV_ApellidoUsuario;
    TextView TV_CorreoUsuario;
    Button BT_EncolarUsuarios;
    Button BT_ActualizarCola;
    Button BT_Desencolar;

    TextView[] TV_ListaUsuariosEncolados;

    RequestQueue colaSolicitud;

    private final String HOST_IP = "192.168.1.11";
    private final String CARPETA_SCRIPTS = "archivos_conexion_bd";
    private final String PUERTO = "80";
    private final String SCRIPT_CONSULTA_USUARIO = "consultarUsuario";
    private final String SCRIPT_USUARIOS_COLA = "retornarUsuariosCola";

    public static final int CONSULTA_USUARIO = 0;
    public static final int ENCOLAR_USUARIO = 1;

    private ColaDePrioridad colaDePrioridad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user_query);

        mAuth = FirebaseAuth.getInstance();
        colaDePrioridad = new ColaDePrioridad();

        ET_Documento = (EditText) findViewById(R.id.DocConsultaEditText);
        BT_ConsultarUsuario = (Button) findViewById(R.id.BotonConsultarUsuario);
        TV_NombreUsuario = (TextView) findViewById(R.id.NombreTextView);
        TV_ApellidoUsuario = (TextView) findViewById(R.id.ApellidoTextView);
        TV_CorreoUsuario = (TextView) findViewById(R.id.CorreoTextView);
        BT_EncolarUsuarios = (Button) findViewById(R.id.BotonEncolarUsuarios);
        BT_ActualizarCola = (Button) findViewById(R.id.BotonActualizarCola);
        BT_Desencolar = (Button) findViewById(R.id.BotonDesencolar);

        BT_ActualizarCola.setEnabled(false);
        BT_Desencolar.setEnabled(false);

        TV_ListaUsuariosEncolados = new TextView[10];
        TV_ListaUsuariosEncolados[0] = (TextView) findViewById(R.id.TV_Usuario1);
        TV_ListaUsuariosEncolados[1] = (TextView) findViewById(R.id.TV_Usuario2);
        TV_ListaUsuariosEncolados[2] = (TextView) findViewById(R.id.TV_Usuario3);
        TV_ListaUsuariosEncolados[3] = (TextView) findViewById(R.id.TV_Usuario4);
        TV_ListaUsuariosEncolados[4] = (TextView) findViewById(R.id.TV_Usuario5);
        TV_ListaUsuariosEncolados[5] = (TextView) findViewById(R.id.TV_Usuario6);
        TV_ListaUsuariosEncolados[6] = (TextView) findViewById(R.id.TV_Usuario7);
        TV_ListaUsuariosEncolados[7] = (TextView) findViewById(R.id.TV_Usuario8);
        TV_ListaUsuariosEncolados[8] = (TextView) findViewById(R.id.TV_Usuario9);
        TV_ListaUsuariosEncolados[9] = (TextView) findViewById(R.id.TV_Usuario10);


        BT_ConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDocumento();
            }
        });
        BT_EncolarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url_2 = "http://" + HOST_IP + ":" + PUERTO + "/" + CARPETA_SCRIPTS
                        + "/"+ SCRIPT_USUARIOS_COLA + ".php";
                ejecutarPeticionPost(url_2, 1);
                actualizarLista();

                BT_ActualizarCola.setEnabled(true);
                BT_Desencolar.setEnabled(true);
                BT_EncolarUsuarios.setEnabled(false);
            }
        });

        BT_ActualizarCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!colaDePrioridad.estaOrganizada()) {
                    colaDePrioridad.organizar();
                }
                actualizarLista();
            }
        });

        BT_Desencolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colaDePrioridad.desencolar();
                actualizarLista();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
    }

    private void validarDocumento() {
        String documento = ET_Documento.getText().toString();

        if (documento.isEmpty()) {
            ET_Documento.setError("Ingrese un número de documento");
        } else {
            String url_1 = "http://" + HOST_IP + ":" + PUERTO + "/" + CARPETA_SCRIPTS
                        + "/"+ SCRIPT_CONSULTA_USUARIO + ".php?id_documento=" + ET_Documento.getText() + "";
            ejecutarPeticionPost(url_1, 0);
        }
    }

    private void ejecutarPeticionPost(String url, final int codigoAccion) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    switch (codigoAccion) {
                        case CONSULTA_USUARIO:
                            consultarUsuario(response);
                            break;
                        case ENCOLAR_USUARIO:
                            retornarYRegistrarUsuariosCola(response);
                            break;
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

    private void consultarUsuario (JSONArray response) throws JSONException{
        JSONObject jsonObject = response.getJSONObject(0);
        TV_NombreUsuario.setText(jsonObject.getString("nombre"));
        TV_ApellidoUsuario.setText(jsonObject.getString("apellido"));
        TV_CorreoUsuario.setText(jsonObject.getString("correo"));
    }

    private void retornarYRegistrarUsuariosCola (JSONArray response) throws JSONException{
        JSONObject jsonObject = null;
        for (int i = 0; i < response.length(); i++) {
            jsonObject = response.getJSONObject(i);
            encolarUsuario(jsonObject);
        }
    }

    private void encolarUsuario (JSONObject jsonObject) throws JSONException{
        Usuario usuario = new Usuario();
        usuario.setNombre(jsonObject.getString("nombre"));
        usuario.setDocumento(jsonObject.getInt("id_documento"));
        usuario.setDistancia(
                calcularDistancia(
                        jsonObject.getDouble("latitud"),
                        jsonObject.getDouble("longitud")
                )
            );
        colaDePrioridad.encolar(usuario);
    }

    private double calcularDistancia(double latitud, double longitud) {
        return Math.sqrt(Math.pow(latitud, 2) + Math.pow(longitud, 2));
    }

    private void actualizarLista() {
        if (!colaDePrioridad.estaVacia()) {
            colaDePrioridad.alterarDistancia();
            colaDePrioridad.organizar();
            String[] infoUsuarios = colaDePrioridad.devolverInformacionUsuarios();

            if (infoUsuarios == null) return;

            for(int i = 0; i < TV_ListaUsuariosEncolados.length; i++) {
                if (i < infoUsuarios.length) {
                    TV_ListaUsuariosEncolados[i].setText(infoUsuarios[i]);
                } else {
                    TV_ListaUsuariosEncolados[i].setText("");
                }
            }
        }
    }
}
