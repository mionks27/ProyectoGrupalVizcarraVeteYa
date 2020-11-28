package com.example.proyecto.Entity;

public class User {
    private String tipo;
    private String rol;
    private String codigo;
    private DeviceUser[] listaDeSolicitudes;

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

    public DeviceUser[] getListaDeSolicitudes() {
        return listaDeSolicitudes;
    }

    public void setListaDeSolicitudes(DeviceUser[] listaDeSolicitudes) {
        this.listaDeSolicitudes = listaDeSolicitudes;
    }
}
