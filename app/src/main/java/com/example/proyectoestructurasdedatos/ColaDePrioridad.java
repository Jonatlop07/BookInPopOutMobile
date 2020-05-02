package com.example.proyectoestructurasdedatos;

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

    public Usuario consultarPrimero() {
        if (!this.estaVacia()) {
            return this.primero.usuario;
        } else return null;
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
        if (this.estaVacia()) {
            this.organizada = true;
        } else if (this.primero == this.ultimo) {
            this.organizada = true;
        } else {
            Nodo i, j;
            for (i = this.primero; i!= null; i = i.next) {
                for (j = i.next; j != null; j = j.next) {
                    if (i.usuario.getDistancia() > j.usuario.getDistancia()) {
                        Usuario temp = i.usuario;
                        i.usuario = j.usuario;
                        j.usuario = temp;
                    }
                }
            }

            this.organizada = true;
        }
    }

    public String[][] devolverInformacionUsuarios () {
        if (this.estaVacia()) {
            return null;
        } else {
            String[][] infoUsuarios = new String[this.getLongitud()][2];
            Nodo auxiliar = this.primero;
            for (int i = 0; i < infoUsuarios.length; i++) {
                infoUsuarios[i][0] = auxiliar.usuario.getNombre();
                infoUsuarios[i][1] = String.valueOf(auxiliar.usuario.getDocumento());
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
                auxiliar = auxiliar.next;
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
