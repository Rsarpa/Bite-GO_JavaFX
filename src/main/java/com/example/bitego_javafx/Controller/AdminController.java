package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.Model.Usuario;

public class AdminController {
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
    }
}
