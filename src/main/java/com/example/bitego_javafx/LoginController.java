package com.example.bitego_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    protected void LoginButton() throws SQLException {
        welcomeText.setText("Welcome to JavaFX Application!");
        // Obtener los datos de los campos
        String email = correoField.getText();
        String contrasena = contrasenaField.getText();

        Connection connection=null;
        connection = Conexion.getInstance();


        if (connection != null) {
            System.out.println("Conexion establecida");
            String sql = "select * from usuario where email = '" + email + "' and contrasenya = '" + contrasena + "'";
            System.out.println(sql);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboardAlumno.fxml"));
                    AnchorPane root = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*// Crear una ventana nueva
                Stage newStage = new Stage();
                VBox vbox = new VBox(10);
                vbox.setStyle("-fx-padding: 20;");

                // Etiquetas con los datos
                vbox.getChildren().add(new Label("Usuario: " + email));
                vbox.getChildren().add(new Label("Bienvenido!!!"));

                Scene scene = new Scene(vbox, 300, 200);
                newStage.setTitle("Datos Introducidos");
                newStage.setScene(scene);
                newStage.show();*/
            }

            /*while (rs.next()){
                System.out.println(rs.getString("dni") + " " + rs.getString(2) + " " + rs.getString(3));
            }*/
        }
    }
}