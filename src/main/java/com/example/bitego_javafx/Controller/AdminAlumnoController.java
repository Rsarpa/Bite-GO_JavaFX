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
    private ComboBox<Curso> cbCurso; // ✅ Se reemplaza txtCurso por cbCurso (ComboBox)

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private CursoDAO cursoDAO = new CursoDAO();
    private Usuario usuario = new Usuario();
    private Alumno alumnoEditando;
    private AdminController adminController;

    public void initialize() {
        validarEmailTiempoReal();
        validarDniTiempoReal();
        cargarCursos(); // ✅ Se llama a la función para llenar el ComboBox con cursos
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    // ✅ Nuevo método para cargar cursos en el ComboBox
    private void cargarCursos() {
        List<Curso> cursos = cursoDAO.obtenerTodos();
        ObservableList<Curso> listaCursos = FXCollections.observableArrayList(cursos);
        cbCurso.setItems(listaCursos);
    }

    public void cargarDatosAlumno(Alumno alumno) {
        this.alumnoEditando = alumno;
        usuario = usuarioDAO.cargarUsuario(alumno.getEmail());

        txtNombre.setText(alumno.getNombre());
        txtApellidos.setText(alumno.getApellidos());
        txtDni.setText(alumno.getDni());
        txtLocalidad.setText(alumno.getLocalidad());
        txtEmail.setText(alumno.getEmail());
        txtContrasena.setText(usuario.getContrasenya());
        txtMotivoBaja.setText(alumno.getMotivo_baja());
        chkAbonado.setSelected(alumno.isAbonado());

        // ✅ Seleccionar el curso del alumno en el ComboBox
        if (alumno.getCurso() != null) {
            cbCurso.getSelectionModel().select(alumno.getCurso());
        }
    }

    @FXML
    private void guardarAlumno() {
        if (validarCampos()) {
            if (alumnoEditando == null) {
                alumnoEditando = new Alumno();
                usuario = new Usuario();
            }

            alumnoEditando.setNombre(txtNombre.getText());
            alumnoEditando.setApellidos(txtApellidos.getText());
            alumnoEditando.setDni(txtDni.getText());
            alumnoEditando.setLocalidad(txtLocalidad.getText());
            alumnoEditando.setEmail(txtEmail.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setRol("Alumno");
            usuario.setContrasenya(txtContrasena.getText());
            alumnoEditando.setMotivo_baja(txtMotivoBaja.getText());
            alumnoEditando.setAbonado(chkAbonado.isSelected());

            // ✅ Obtener el curso seleccionado en el ComboBox
            Curso cursoSeleccionado = cbCurso.getSelectionModel().getSelectedItem();

            if (cursoSeleccionado != null) {
                alumnoEditando.setCurso(cursoSeleccionado);

                if (alumnoEditando.getId_alumno() == 0) {
                    alumnoDAO.save(alumnoEditando);
                    usuarioDAO.save(usuario);
                } else {
                    alumnoDAO.update(alumnoEditando);
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
