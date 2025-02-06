package com.example.bitego_javafx.Clases;

public class Usuario {
    private String email;
    private String contrasena;
    private String rol;

    public Usuario(String email, String contrasena, String rol) {
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }
}
