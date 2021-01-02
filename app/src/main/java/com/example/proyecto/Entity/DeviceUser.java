package com.example.proyecto.Entity;

public class DeviceUser {
    private Device device;
    private String estado;
    private String motivo;
    private String direccionUsuario;
    private String direccionGPS;
    private String enviarCorreo;
    private String pkSolicitud;
    private String nombreUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    private String uidUser;

    public String getUidUser() {
        return uidUser;
    }

    public void setUidUser(String uidUser) {
        this.uidUser = uidUser;
    }

    public String getPkSolicitud() {
        return pkSolicitud;
    }

    public void setPkSolicitud(String pkSolicitud) {
        this.pkSolicitud = pkSolicitud;
    }

    public String getEnviarCorreo() {
        return enviarCorreo;
    }

    public void setEnviarCorreo(String enviarCorreo) {
        this.enviarCorreo = enviarCorreo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        this.direccionUsuario = direccionUsuario;
    }

    public String getDireccionGPS() {
        return direccionGPS;
    }

    public void setDireccionGPS(String direccionGPS) {
        this.direccionGPS = direccionGPS;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
