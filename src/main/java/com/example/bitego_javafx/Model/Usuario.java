package com.example.bitego_javafx.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    private int id_usuario;
    @Column(name = "mac", nullable = true)
    private String mac;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "contrasenya",nullable = false)
    private String contrasenya;
    @Column(name = "rol", nullable = false)
    private String rol;

    public Usuario(String email, String contrasena, String rol) {
        this.email = email;
        this.contrasenya = contrasena;
        this.rol = rol;
    }

    public Usuario() {}

    public String getEmail() {
        return email;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public String getRol() {
        return rol;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
