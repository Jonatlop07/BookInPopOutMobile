package com.example.proyectoestructurasdedatos.utilidades;

public interface DatosConexion {
    String MAIN_URL = "http://a29aa323bf90.ngrok.io/ProyectoEstructurasServlets/";
    String INICIO_SESION = MAIN_URL + "InicioSesion";
    String REGISTRO_USUARIO_NORMAL =  MAIN_URL + "RegUsuarioNormal";
    String REGISTRO_USUARIO_EMPRESARIAL = MAIN_URL + "RegistroUsuarioEmpresarial";
    String SOLICITUD_CREACION_COLA = MAIN_URL + "CreacionCola";
    String SOLICITUD_ENCOLAMIENTO = MAIN_URL + "Encolamiento";
    String SOLICITUD_DESENCOLAMIENTO = MAIN_URL + "Desencolamiento";
    String SOLICITUD_HISTORIAL = MAIN_URL + "HistorialCitas";
    String REQUERIR_INFORMACION_COLA = MAIN_URL + "InfoCola";
    String CONSULTAR_EXISTENCIA_USUARIO = MAIN_URL + "ExistenciaUsuario";
    String REGISTRAR_CITA_USUARIO_INSTANTANEO = MAIN_URL + "RegistroCita";
    String INFORMACION_USUARIO = MAIN_URL + "InfoUsuario";
    String ACTUALIZAR_USUARIO_NORMAL = MAIN_URL + "ActualizacionUsuario";
    String CANCELAR_CITA = MAIN_URL + "CancelarCita";
    String INFORMAR_CITA_PERDIDA = MAIN_URL + "CitaPerdida";
    String ELIMINAR_COLA = MAIN_URL + "EliminacionCola";
}
