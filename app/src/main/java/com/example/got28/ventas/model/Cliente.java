package com.example.got28.ventas.model;

/**
 * Created by Got28 on 05/10/2018.
 * Agregamos un package llamado model en la carpeta de mainactivity
 * agregamos nueva clase
 */

public class Cliente {
    //agregamos nuestras propiedades
    private String uid;
    private String Nombre;
    private String Telefono;
    //agremamos un contructor

    public Cliente() {
    }
    //agregamos los getter y setter
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }
    //agregamos el toString

    @Override
    public String toString() {
        return Nombre + "                   " + Telefono;
    }
}
