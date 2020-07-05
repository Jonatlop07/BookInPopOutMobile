package com.example.proyectoestructurasdedatos.utilidades;

public interface DatosConexion {
    String MAIN_URL = "http://2ea40f96bcaf.ngrok.io/Proyecto/";
    String INICIO_SESION = MAIN_URL + "Inisession";
    String REGISTRO_USUARIO_NORMAL =  MAIN_URL + "Registro";
    String REGISTRO_USUARIO_EMPRESARIAL = MAIN_URL + "Regempresarial";
    String SOLICITUD_CREACION_COLA = MAIN_URL + "Creacola";
    String SOLICITUD_ENCOLAMIENTO = MAIN_URL + "Encolamiento";
    String SOLICITUD_DESENCOLAMIENTO = MAIN_URL + "Desencolar";
    String CONFIRMACION = MAIN_URL + "";
    String SOLICITUD_HISTORIAL = MAIN_URL + "Historial";
    String REQUERIR_INFORMACION_COLA = MAIN_URL + "Infocola";
    String CONSULTAR_EXISTENCIA_USUARIO = MAIN_URL + "Existencia_usuario";
    String REGISTRAR_CITA_USUARIO_INSTANTANEO = MAIN_URL + "Regcita";
    String INFORMACION_USUARIO = MAIN_URL + "Infousuario";
    String ACTUALIZAR_USUARIO_NORMAL = MAIN_URL + "";
    String CANCELAR_CITA = MAIN_URL + "";
}
