package com.example.proyectoestructurasdedatos;

import org.w3c.dom.Node;

public class ColaDePrioridad {
    private static class Nodo {
        public Usuario usuario;
        public Nodo next;

        public Nodo(Usuario usuario) {
            this.usuario = usuario;
            this.next = null;
        }
    }

    //Primero en salir y último en entrar
    private Nodo primero;
    private Nodo ultimo;
    private boolean organizada;

    public ColaDePrioridad() {
        this.primero = null;
        this.ultimo = null;
        this.organizada = false;
    }

    public void encolar (Usuario usuario) {
        Nodo nuevoNodo = new Nodo(usuario);
        if (this.estaVacia()) {
            this.primero = this.ultimo = nuevoNodo;
        } else {
            this.ultimo.next = nuevoNodo;
            this.ultimo = nuevoNodo;
        }
    }

    public Usuario desencolar () {
        if (!this.estaVacia()) {
            Nodo usuarioAEliminar = this.primero;
            this.primero = this.primero.next;
            return usuarioAEliminar.usuario;
        } else return null;
    }

    public void organizar () {
        if (this.estaVacia() || this.primero == this.ultimo) {
            this.organizada = false;
        } else {
            Nodo auxiliar = this.primero;
            Nodo anterior = this.primero;
            while (auxiliar.next != null) {
                if (auxiliar.usuario.getDistancia() > auxiliar.next.usuario.getDistancia()) {
                    anterior.next = auxiliar.next;
                    auxiliar.next = auxiliar.next.next;
                    anterior.next.next = auxiliar;
                }
                auxiliar = anterior;
            }
            this.organizada = true;
        }
    }

    public boolean estaVacia () {
        return this.primero == null;
    }
}
