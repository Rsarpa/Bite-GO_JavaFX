package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;

public class AdminController {
    private Usuario usuario;
    @FXML
    private void añadirAlumno() {
        // Lógica para abrir un formulario de añadir alumno
    }

    @FXML
    private void editarAlumno() {
        // Lógica para editar el alumno seleccionado
    }

    @FXML
    private void borrarAlumno() {
        // Lógica para eliminar el alumno seleccionado
    }

    @FXML
    private void mostrarInformacion() {
        // Lógica para mostrar detalles del alumno seleccionado
    }

    @FXML
    private void filtrarPorNombre() {
        // Lógica para filtrar los alumnos en la tabla
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
    }
}
