package com.example.proyectoestructurasdedatos.utilidades;

public interface DatosConexion {
    String HOST_IP = "192.168.1.11";
    String CARPETA_SCRIPTS = "archivos_conexion_bd";
    String PUERTO = "80";
    String SCRIPT_CONSULTA_USUARIO = "consultarUsuario";
    String SCRIPT_USUARIOS_COLA = "retornarUsuariosCola";
    String URL_CONSULTA_USUARIO = "http://" + HOST_IP + ":" + PUERTO + "/" + CARPETA_SCRIPTS
            + "/" + SCRIPT_CONSULTA_USUARIO + ".php?id_documento=";
    String URL_USUARIOS_COLA = "http://" + HOST_IP + ":" + PUERTO + "/" + CARPETA_SCRIPTS
            + "/"+ SCRIPT_USUARIOS_COLA + ".php";
    void cargarWebService();
}
