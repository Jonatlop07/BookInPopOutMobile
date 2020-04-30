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
    private int longitud;
    private boolean organizada;

    public ColaDePrioridad() {
        this.primero = null;
        this.ultimo = null;
        this.organizada = false;
        this.longitud = 0;
    }

    public void encolar (Usuario usuario) {
        Nodo nuevoNodo = new Nodo(usuario);
        if (this.estaVacia()) {
            this.primero = this.ultimo = nuevoNodo;
        } else {
            this.ultimo.next = nuevoNodo;
            this.ultimo = nuevoNodo;
        }

        this.longitud++;
    }

    public Usuario desencolar () {
        if (!this.estaVacia()) {
            Nodo usuarioAEliminar = this.primero;
            this.primero = this.primero.next;
            this.longitud--;
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

    public String[] devolverInformaciónUsuarios () {
        if (this.estaVacia()) {
            return null;
        } else {
            String[] infoUsuarios = new String[this.getLongitud()];
            Nodo auxiliar = this.primero;
            for (int i = 0; i < infoUsuarios.length; i++) {
                infoUsuarios[i] = auxiliar.usuario.toString();
                auxiliar = auxiliar.next;
            }

            return infoUsuarios;
        }
    }

    public void alterarDistancia() {
        if (!this.estaVacia()) {
            Nodo auxiliar = this.primero;
            for (int i = 0; i < this.getLongitud(); i++) {
                double deltaDist = Math.random() * 2 * (Math.random() > 0.5 ? 1 : -1);
                auxiliar.usuario.setDistancia(auxiliar.usuario.getDistancia() + deltaDist);
            }
        }
    }

    public boolean estaVacia () {
        return longitud == 0;
    }

    public boolean estaOrganizada () {
        return organizada;
    }

    public int getLongitud() {
        return longitud;
    }
}
