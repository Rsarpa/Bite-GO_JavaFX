package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    public void menuBocadillo(ActionEvent event){
        String tipo="Bocadillo";
        cerrarVentana(event, tipo);
    }

    public void menuAlumno(ActionEvent event){
        String tipo="Alumno";
        cerrarVentana(event, tipo);
    }

    private void cerrarVentana(ActionEvent event, String tipo){
        try {
            // Cerrar la ventana actual
            .getScene().getWindow().hide();
            stage.close();

            if (tipo.equals("Bocadillo")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/resourcesAdmin/crudBocadillo.fxml"));
                // Parent root = loader.load();
                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load(), 800, 600);

                mainStage.setTitle("Hello!");
                mainStage.setScene(scene);
                mainStage.show();
            }

            if (tipo.equals("Alumno")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/dashboardAdmin.fxml"));
                // Parent root = loader.load();
                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load(), 320, 240);

                mainStage.setTitle("Hello!");
                mainStage.setScene(scene);
                mainStage.show();
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
