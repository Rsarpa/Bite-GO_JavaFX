package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyEvent;
import java.net.URL;
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

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Asocio las columnas con las propiedades del objeto
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id_alumno"));
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        this.colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        this.colLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Cargo los alumnos al iniciar
        this.mostrarAlumnos();
    }

    private void mostrarAlumnos() {
        List<Alumno> alumnos = alumnoDAO.obtenerAlumnos();
        listaAlumnos.setAll(alumnos);
        tblAlumnos.setItems(listaAlumnos);
    }

    @FXML
    private void filtrarPorNombre(KeyEvent event) {
        String filtro = txtFiltroNombre.getText();
        if (filtro == null || filtro.isEmpty()) {
            tblAlumnos.setItems(listaAlumnos);
        } else {
            List<Alumno> filtrados = listaAlumnos.stream()
                    .filter(alumno -> alumno.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                    .collect(Collectors.toList());
            tblAlumnos.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
    }

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
}
