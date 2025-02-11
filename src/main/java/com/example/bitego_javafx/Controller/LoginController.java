package com.example.bitego_javafx.Controller;

import DAO.LoginDAO;
import com.example.bitego_javafx.Model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField correoField;

    @FXML
    private PasswordField contrasenaField;

    @FXML
    private Button loginButton;

    @FXML
    private Label welcomeText;


    @FXML
    private void onLoginButtonClick(ActionEvent event) throws SQLException {

        //welcomeText.setText("Welcome to JavaFX Application!");
        // Obtener los datos de los campos
        String email = correoField.getText();
        String contrasenya = contrasenaField.getText();
        String rol = "";

        if (email.isEmpty() || contrasenya.isEmpty()){
            welcomeText.setText("Por favor complete los campos");
        }

        Usuario usuario = new Usuario();

        try {
            LoginDAO login = new LoginDAO();
            usuario = login.log(email, contrasenya);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("No se puedo realizar la consulta");
        }


        if (usuario != null){

            switch (usuario.getRol()){
                case "Alumno":
                    cambiarDashboard("/com/example/bitego_javafx/dashboardAlumno.fxml", event);
                    break;

                case "Administrador":
                    cambiarDashboard("/com/example/bitego_javafx/dashboardAdmin.fxml", event);
                    break;

                case "Cocina":
                    cambiarDashboard("/com/example/bitego_javafx/dashboardCocina.fxml", event);
                    break;
            }
        }else {
            welcomeText.setText("Usuario no registrado");
        }

    }

    private void cambiarDashboard(String ruta, ActionEvent event){
        try{

            // Cerrar la ventana actual
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            //recoger clase del fichero ruta
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Scene scene = new Scene(loader.load(), 600, 400);

            Stage stageNuevo = new Stage();
            stageNuevo.setTitle("Hello!");
            stageNuevo.setScene(scene);
            stageNuevo.show();


        }catch (IOException e){
            e.printStackTrace();
        }
    }
}