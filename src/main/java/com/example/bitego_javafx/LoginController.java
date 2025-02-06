package com.example.bitego_javafx;

import Controler.LoginDAO;
import com.example.bitego_javafx.Clases.Usuario;
import com.example.bitego_javafx.Util.Conexion;
import jakarta.persistence.Query;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;

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
        String contrasena = contrasenaField.getText();

        if (email.isEmpty() || contrasena.isEmpty()){
            welcomeText.setText("Por favor complete los campos");
        }

        SessionFactory sf = Conexion.getSessionFactory();
        LoginDAO login = new LoginDAO(sf);
        Usuario usuario = login.log(email, contrasena);
        //Usuario usuario = loginDAO.log(email, contrasena);


        if (usuario != null){
            System.out.println("Usuario registrado");

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