package com.example.bitego_javafx.Controller;

import DAO.LoginDAO;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private void onLoginButtonClick() throws SQLException {
        // Obtener los datos de los campos
        String email = correoField.getText();
        String contrasenya = contrasenaField.getText();

        if (email.isEmpty() || contrasenya.isEmpty()) {
            welcomeText.setText("Por favor complete los campos");
            return;
        }

        Usuario usuario = null;

        try {
            LoginDAO login = new LoginDAO();
            usuario = login.log(email, contrasenya);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No se pudo realizar la consulta");
        }

        if (usuario != null) {
            switch (usuario.getRol()) {
                case "Alumno":
                    cambiarDashboard("/com/example/bitego_javafx/dashboardAlumno.fxml", usuario);
                    break;
                case "Administrador":
                    cambiarDashboard("/com/example/bitego_javafx/dashboardAdmin.fxml", usuario);
                    break;
                case "Cocina":
                    cambiarDashboard("/com/example/bitego_javafx/dashboardCocina.fxml", usuario);
                    break;
                default:
                    welcomeText.setText("Rol desconocido");
                    break;
            }
        } else {
            welcomeText.setText("Usuario no registrado");
        }
    }

    private void cambiarDashboard(String ruta, Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            // Obtener el controlador de la vista cargada
            Object controller = loader.getController();

            // Verificar el tipo de usuario y pasarlo al controlador correspondiente
            if (controller instanceof AlumnoController && usuario.getRol().equals("Alumno")) {
                ((AlumnoController) controller).setUsuario(usuario);
            } else if (controller instanceof AdminController && usuario.getRol().equals("Administrador")) {
                ((AdminController) controller).setUsuario(usuario);
            } else if (controller instanceof CocinaController && usuario.getRol().equals("Cocina")) {
                ((CocinaController) controller).setUsuario(usuario);
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 800));
            stage.show();

            // Cerrar la ventana de login después de iniciar sesión
            Stage loginStage = (Stage) loginButton.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
