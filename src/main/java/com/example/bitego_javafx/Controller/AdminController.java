package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.CursoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.*;
import javafx.beans.property.SimpleStringProperty;
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
    private TableColumn<Alumno, String> colCurso;
    @FXML
    private TextField txtFiltroNombre;
    @FXML
    private TextField txtPaginaActual;
    @FXML
    private Button btnAnterior, btnSiguiente;
    @FXML
    private ComboBox cmbFiltroCurso;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private CursoDAO cursoDAO=new CursoDAO();
    private ObservableList<Alumno> listaAlumnos = FXCollections.observableArrayList();
    private int paginaActual = 1;
    private final int registrosPorPagina = 15;

    private ObservableList<Curso> listaCursos = FXCollections.observableArrayList();

    //Cargamos todos los alergenos de la DB en el ComboBox
    private void cargarCursos() {
        listaCursos.setAll(cursoDAO.obtenerTodos());
        cmbFiltroCurso.setItems(listaCursos);
    }


    //Asigna los valores de la tabla dinámicamente
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Aqui no utilizamos configurar tabla sino que ya empezamos configurando como se van a añadir valores a las tablas
        colId.setCellValueFactory(new PropertyValueFactory<>("id_alumno"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colLocalidad.setCellValueFactory(new PropertyValueFactory<>("localidad"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        //Utilizamos getValue para obtener el obtejo ALumno y de el recuperar el getCurso() devolverá el toString de la clase Curso
        colCurso.setCellValueFactory(cellData -> {
            Alumno alumno = cellData.getValue();
            String curso = alumno.getCurso().toString();
            return new SimpleStringProperty(curso);
        });

        cargarAlumnos();
        cargarCursos();
        //ATAJOS DE TECLADO

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
        //Facilidad para añadir filtros a la aplicación

        HashMap<String, String> filtros = new HashMap<>();
        if (!txtFiltroNombre.getText().isEmpty()) {
            filtros.put("nombre", txtFiltroNombre.getText());
        }

        if (!cmbFiltroCurso.getSelectionModel().isEmpty()){
            Curso cursoSeleccionado = (Curso) cmbFiltroCurso.getSelectionModel().getSelectedItem();
            String idCurso = String.valueOf(cursoSeleccionado.getId_curso());
            filtros.put("idCurso", idCurso);

        }

        List<Alumno> alumnos = alumnoDAO.getPaginated(paginaActual, registrosPorPagina, filtros);
        listaAlumnos.setAll(alumnos);
        //Asignamos los elementos a cada columna de la tabla ya configurada
        tblAlumnos.setItems(listaAlumnos);
        txtPaginaActual.setText(String.valueOf(paginaActual));
        //establecerlo en modo vista
        txtPaginaActual.setEditable(false);

        //Calculo para obtener la paginación
        long totalRegistros = alumnoDAO.count(filtros); // MétHodo que cuenta registros en la BD
        int totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);


        // Deshabilitar el botón "Anterior" si estamos en la primera página
        btnAnterior.setDisable(paginaActual == 1);

        // Deshabilitar el botón "Siguiente" si la cantidad de resultados es menor al máximo por página
        btnSiguiente.setDisable(paginaActual >= totalPaginas);

    }

    //Cada vez que se llama a pagina anterior o página  siguiente se cargan los alumnos para refrescar y se comprueba el total de paginas
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

            //IMPORTANTISIMO PARA LOGRAR LA PERSISTENCIA EN TIEMPO REAL
            AdminAlumnoController controller = loader.getController();
            // Enlazar con AdminController para llamar a cargarAlumnos y que se refresque cuando se pulsa en guardar
            controller.setAdminController(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editarAlumno() {
        //Recuperar el alumno seleccionado con el puntero de la tabla
        Alumno alumnoSeleccionado = tblAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/AdminAlumno.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load(), 500, 500);
                stage.setScene(scene);
                stage.setTitle("Editar Alumno");
                //IMPORTANTISIMO PARA LOGRAR LA PERSISTENCIA EN TIEMPO REAL
                AdminAlumnoController controller = loader.getController();
                controller.setAdminController(this);
                // Pasar datos al formulario llamando al controller de la ventana que vamos a cargar
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
