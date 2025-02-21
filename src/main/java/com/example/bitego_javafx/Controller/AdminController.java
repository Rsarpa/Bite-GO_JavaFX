package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
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
import javafx.scene.input.KeyCode;
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
    private Button btnAnterior, btnSiguiente;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
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
        // Capturar eventos de teclado de la tabla
        tblAlumnos.setOnKeyPressed(this::manejarAtajosTabla);

        // Esperar hasta que la escena esté disponible para registrar los atajos globales
        tblAlumnos.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::manejarAtajosGlobales);
            }
        });
    }

    // Evento global (Ctrl + N) en toda la ventana
    private void manejarAtajosGlobales(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.N) {
            anyadirAlumno(); // Ctrl + N funciona en cualquier parte
        }
    }

    // Evento en la tabla (Ctrl + E, Ctrl + D) SOLO si hay un elemento seleccionado
    private void manejarAtajosTabla(KeyEvent event) {
        if (event.isControlDown()) {
            Alumno alumnoSeleccionado = tblAlumnos.getSelectionModel().getSelectedItem();

            if (alumnoSeleccionado != null) {
                if (event.getCode() == KeyCode.E) {
                    editarAlumno(); // Ctrl + E para editar
                } else if (event.getCode() == KeyCode.D) {
                    borrarAlumno(); // Ctrl + D para borrar
                }
            } else {
                mostrarAlerta("Debe seleccionar un alumno para esta acción.");
            }
        }
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
        //establecerlo en modo vista
        txtPaginaActual.setEditable(false);

        long totalRegistros = alumnoDAO.count(filtros); // Método que cuenta registros en la BD
        int totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);

        // Deshabilitar el botón "Anterior" si estamos en la primera página
        btnAnterior.setDisable(paginaActual == 1);

        // Deshabilitar el botón "Siguiente" si la cantidad de resultados es menor al máximo por página
        btnSiguiente.setDisable(paginaActual >= totalPaginas);

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
            mainStage.setMaximized(true);
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
            Scene scene = new Scene(loader.load(), 800, 450);

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
            // Enlazar con AdminController
            controller.setAdminController(this);
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
                // Pasar datos al formulario
                controller.cargarDatosAlumno(alumnoSeleccionado);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Debe seleccionar un alumno para editar.");
        }
    }

    @FXML
    public void borrarAlumno(){
        Alumno alumnoSeleccionado = tblAlumnos.getSelectionModel().getSelectedItem();

        try {
            String email = alumnoSeleccionado.getEmail();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que quieres eliminar este alumno?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmar eliminación");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                alumnoDAO.delete(alumnoSeleccionado);
                try {
                    usuarioDAO.delete(email);
                }catch (Exception e){
                    mostrarAlerta("Este usuario no se puede eliminar, tiene un montón de dependencias");
                }
                cargarAlumnos(); // Actualizar la lista después de eliminar
            }
        }catch (Exception e){
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
