package com.example.proyectoestructurasdedatos;

public class Usuario {
    private String nombre;
    private String documento;
    private double distancia;

    public Usuario(String nombre, String documento, double distancia) {
        this.nombre = nombre;
        this.documento = documento;
        this.distancia = distancia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
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
