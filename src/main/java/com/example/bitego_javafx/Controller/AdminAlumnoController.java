package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.CursoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Curso;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class AdminAlumnoController {

    @FXML
    private TextField txtNombre, txtApellidos, txtDni, txtLocalidad, txtEmail, txtMotivoBaja;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private CheckBox chkAbonado;
    @FXML
    private Button btnGuardar, btnCancelar;
    @FXML
    private ComboBox<Curso> cbCurso;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private CursoDAO cursoDAO = new CursoDAO();
    private Usuario usuario = new Usuario();
    private Alumno alumno_editado;
    private AdminController adminController;

    public void initialize() {
        validarEmailTiempoReal();
        validarDniTiempoReal();
        cargarCursos(); //Se rellena el ComboBox
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    //Carga los cursos en el comboBoc
    private void cargarCursos() {
        List<Curso> cursos = cursoDAO.obtenerTodos();
        ObservableList<Curso> listaCursos = FXCollections.observableArrayList(cursos);
        cbCurso.setItems(listaCursos);
    }

    public void cargarDatosAlumno(Alumno alumno) {
        this.alumno_editado = alumno;
        usuario = usuarioDAO.cargarUsuario(alumno.getEmail());

        txtNombre.setText(alumno.getNombre());
        txtApellidos.setText(alumno.getApellidos());
        txtDni.setText(alumno.getDni());
        txtLocalidad.setText(alumno.getLocalidad());
        txtEmail.setText(alumno.getEmail());
        txtContrasena.setText(usuario.getContrasenya());
        txtMotivoBaja.setText(alumno.getMotivo_baja());
        chkAbonado.setSelected(alumno.isAbonado());

        /*
        Seleccionamos el curso por defecto si estamos entrando este editar
        Por defecto aparecerá seleccione un Curso
         */
        if (alumno.getCurso() != null) {
            cbCurso.getSelectionModel().select(alumno.getCurso());
        }
    }

    @FXML
    private void guardarAlumno() {
        if (validarCampos()) {
            if (alumno_editado == null) {
                alumno_editado = new Alumno();
                usuario = new Usuario();
            }

            alumno_editado.setNombre(txtNombre.getText());
            alumno_editado.setApellidos(txtApellidos.getText());
            alumno_editado.setDni(txtDni.getText());
            alumno_editado.setLocalidad(txtLocalidad.getText());
            alumno_editado.setEmail(txtEmail.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setRol("Alumno");
            usuario.setContrasenya(txtContrasena.getText());
            alumno_editado.setMotivo_baja(txtMotivoBaja.getText());
            alumno_editado.setAbonado(chkAbonado.isSelected());

            //Obtenemos el curso por el comboBox
            Curso cursoSeleccionado = cbCurso.getSelectionModel().getSelectedItem();

            if (cursoSeleccionado != null) {
                alumno_editado.setCurso(cursoSeleccionado);

                if (alumno_editado.getId_alumno() == 0) {
                    alumnoDAO.save(alumno_editado);
                    usuarioDAO.save(usuario);
                } else {
                    alumnoDAO.update(alumno_editado);
                    usuarioDAO.update(usuario);
                }

                adminController.cargarAlumnos();
                cerrarVentana();
            } else {
                mostrarAlerta("Debes seleccionar un curso.");
            }
        } else {
            mostrarAlerta("Todos los campos son obligatorios.");
        }
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() && !txtApellidos.getText().isEmpty() &&
                !txtDni.getText().isEmpty() && !txtLocalidad.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() && !txtContrasena.getText().isEmpty() &&
                cbCurso.getSelectionModel().getSelectedItem() != null;
    }

    //TODO Validaciones añadir a SERVICE
    private void validarEmailTiempoReal() {
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")) {
                txtEmail.setStyle("-fx-border-color: green;");
                validarHabilitarGuardar();
            } else {
                txtEmail.setStyle("-fx-border-color: red;");
                btnGuardar.setDisable(true);
            }
        });
    }

    //TODO Validaciones añadir a SERVICE
    private void validarDniTiempoReal() {
        txtDni.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^[0-9]{8}[A-Z]$")) {
                txtDni.setStyle("-fx-border-color: green;");
                validarHabilitarGuardar();
            } else {
                txtDni.setStyle("-fx-border-color: red;");
                btnGuardar.setDisable(true);
            }
        });
    }

    //TODO Validaciones añadir a SERVICE
    private void validarHabilitarGuardar() {
        if (txtEmail.getStyle().contains("green") && txtDni.getStyle().contains("green")) {
            btnGuardar.setDisable(false);
        }
    }

    @FXML
    private void cerrarVentana() {
        btnGuardar.getScene().getWindow().hide();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
