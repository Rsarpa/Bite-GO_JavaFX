package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminController implements Initializable {
    private Usuario usuario;
    @FXML
    private TableView<Alumno> tblAlumnos;
    @FXML
    private TableColumn<Alumno, Integer> colId;
    @FXML
    private TableColumn<Alumno, String> colNombre;
    @FXML
    private TableColumn<Alumno, String> colApellidos;
    @FXML
    private TableColumn<Alumno, String> colDni;
    @FXML
    private TableColumn<Alumno, String> colLocalidad;
    @FXML
    private TableColumn<Alumno, String> colEmail;
    @FXML
    private TextField txtFiltroNombre;
    @FXML
    private TextField txtPaginaActual;
    @FXML
    private Button btnAnterior,btnSiguiente;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();
    private int paginaActual = 1;
    private final int registrosPorPagina = 10;

    //Asigna los valores de la tabla dinámicamente
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_alumno"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        cargarAlumnos();
    }

    //Utilizamos un HashMap para aplicar varios filtros si es necesario
    public void cargarAlumnos() {
        HashMap<String, String> filtros = new HashMap<>();
        if (!txtFiltroNombre.getText().isEmpty()) {
            filtros.put("nombre", txtFiltroNombre.getText());
        }

        List<Alumno> alumnos = alumnoDAO.getPaginated(paginaActual, registrosPorPagina, filtros);
        listaAlumnos.setAll(alumnos);
        tblAlumnos.setItems(listaAlumnos);
        txtPaginaActual.setText(String.valueOf(paginaActual));

        // Deshabilitar el botón "Anterior" si estamos en la primera página
        btnAnterior.setDisable(paginaActual == 1);

        // Deshabilitar el botón "Siguiente" si la cantidad de resultados es menor al máximo por página
        btnSiguiente.setDisable(alumnos.size() < registrosPorPagina);
    }

    @FXML
    private void filtrarPorNombre(ActionEvent event) {
        paginaActual = 1;
        cargarAlumnos();
    }

    @FXML
    public void paginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            cargarAlumnos();
        }
    }

    public void paginaSiguiente() {
        paginaActual++;
        cargarAlumnos();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
    }

    public void menuBocadillo(ActionEvent event) {
        cerrarVentana(event, "Bocadillo");
    }

    public void menuAlumno(ActionEvent event) {
        cerrarVentana(event, "Alumno");
    }

    private void cerrarVentana(ActionEvent event, String tipo) {
        try {
            // Cerrar la ventana actual
            tblAlumnos.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    tipo.equals("Bocadillo") ? "/com/example/bitego_javafx/crudBocadillo.fxml" : "/com/example/bitego_javafx/dashboardAdmin.fxml"));
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 800, 600);
            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event){
        try {

            // Cerrar la ventana actual
            tblAlumnos.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/login.fxml"));
            // Parent root = loader.load();
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 320, 240);

            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void anyadirAlumno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/AdminAlumno.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load(), 500, 500);
            stage.setScene(scene);
            stage.setTitle("Añadir Alumno");

            AdminAlumnoController controller = loader.getController();
            controller.setAdminController(this); // Enlazar con AdminController
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editarAlumno() {
        Alumno alumnoSeleccionado = tblAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/AdminAlumno.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load(), 500, 500);
                stage.setScene(scene);
                stage.setTitle("Editar Alumno");

                AdminAlumnoController controller = loader.getController();
                controller.setAdminController(this);
                controller.cargarDatosAlumno(alumnoSeleccionado); // Pasar datos al formulario
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Debe seleccionar un alumno para editar.");
        }
    }

    @FXML
    public void borrarAlumno() {
        Alumno alumnoSeleccionado = tblAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quieres eliminar este alumno?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmar eliminación");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                alumnoDAO.delete(alumnoSeleccionado);
                cargarAlumnos(); // Actualizar la lista después de eliminar
            }
        } else {
            mostrarAlerta("Debe seleccionar un alumno para eliminar.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
