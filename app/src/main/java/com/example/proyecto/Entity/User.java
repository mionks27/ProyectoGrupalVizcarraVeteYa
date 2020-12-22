package com.example.proyecto.Entity;

import java.util.ArrayList;

public class User {
    private String tipo;
    private String rol;
    private String codigo;
    private ArrayList<DeviceUser> listaDeSolicitudes;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public ArrayList<DeviceUser> getListaDeSolicitudes() {
        return listaDeSolicitudes;
    }

    public void setListaDeSolicitudes(ArrayList<DeviceUser> listaDeSolicitudes) {
        this.listaDeSolicitudes = listaDeSolicitudes;
    }
}
