package com.example.bitego_javafx.Controller;

import DAO.LoginDAO;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    private void onLoginButtonClick() throws SQLException {

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
                    //todo redirigir ventana alumno
                    break;

                case "Administrador":
                    //todo redirigir ventana admin
                    break;

                case "Cocina":
                    //todo redirigir ventana cocina
                    break;
            }
        }else {
            welcomeText.setText("Usuario no registrado");
        }

    }
}