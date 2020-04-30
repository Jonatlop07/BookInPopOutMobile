package com.example.proyectoestructurasdedatos;

public class Usuario {
    private String nombre;
    private long documento;
    private double distancia;

    public Usuario(String nombre, long documento, double distancia) {
        this.nombre = nombre;
        this.documento = documento;
        this.distancia = distancia;
    }

    public Usuario() {
        this.nombre = "";
        this.documento = 0;
        this.distancia = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getDocumento() {
        return documento;
    }

    public void setDocumento(long documento) {
        this.documento = documento;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String toString() {
        return this.nombre + '\t' + this.documento;
    }
}
