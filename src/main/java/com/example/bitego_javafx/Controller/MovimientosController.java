package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class MovimientosController {
    @FXML
    private TableView<?> tablaPedidos;
    @FXML
    private TableColumn<?, ?> colIdPedido;
    @FXML
    private TableColumn<?, ?> colNombreBocadillo;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colCosto;
    @FXML
    private TableColumn<?, ?> colDescuento;

    @FXML
    private ComboBox<String> cbFiltroTiempo;

    @FXML
    private Label lblNumPedidos;
    @FXML
    private Label lblTotalGastado, lblEmail;

    @FXML
    private Button btnVolver;
    @FXML
    private ImageView iconVolver;


    @FXML
    private VBox sidePane;
    private Usuario usuario;
    private Alumno alumno;

    @FXML
    public void initialize() {
        // Inicialización de eventos y datos

    }

    private void filtrarPedidos() {
        String filtro = cbFiltroTiempo.getValue();
        System.out.println("Filtrando pedidos por: " + filtro);
        // Implementar lógica de filtrado
    }

    @FXML
    private void volverAtras() {
        System.out.println("Volviendo a la pantalla anterior...");
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/com/example/bitego_javafx/dashboardAlumno.fxml")));
        try {
            Parent root = loader.load();
            //Se obtiene el controlador de la vista que se va a cargar

            Object controller = loader.getController();
            Stage stage = (Stage) lblNumPedidos.getScene().getWindow();

            //Llama a set usuario de AlumnoController
            ((AlumnoController) controller).setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene); //Preguntar duda a victror // stage.setScene(new Scene(root, 1200, 800));

            stage.show();
            //Nueva escena

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());

        if (usuario != null) {
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
        }
    }
}

