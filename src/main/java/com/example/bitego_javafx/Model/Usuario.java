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
}
