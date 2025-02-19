package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.CursoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Curso;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class AdminAlumnoController {

    @FXML
    private TextField txtNombre, txtApellidos, txtDni, txtLocalidad, txtEmail,txtCurso, txtMotivoBaja;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private CheckBox chkAbonado;
    @FXML
    private Button btnGuardar, btnCancelar;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private Usuario usuario = new Usuario();
    private Alumno alumnoEditando;
    private AdminController adminController;

    public void initialize() {
        validarEmailTiempoReal();
        validarDniTiempoReal();
    }

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
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
        txtCurso.setText(alumno.getCurso().getNombre_curso());
        txtMotivoBaja.setText(alumno.getMotivo_baja());
        chkAbonado.setSelected(alumno.isAbonado());
    }

    @FXML
    private void guardarAlumno() {
        if (validarCampos()) {
            if (alumnoEditando == null) { // Si es un nuevo alumno
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

            // Buscar el curso por nombre_curso
            String nombreCurso = txtCurso.getText().trim();
            Curso curso = CursoDAO.getByNombre(nombreCurso);

            if (curso != null) {
                alumnoEditando.setCurso(curso);
                if (alumnoEditando.getId_alumno() == 0) {
                    alumnoDAO.save(alumnoEditando); // Guardar nuevo alumno
                    usuarioDAO.save(usuario);
                } else {
                    alumnoDAO.update(alumnoEditando); // Actualizar alumno
                    usuarioDAO.update(usuario);
                }
                adminController.cargarAlumnos();
                cerrarVentana();
            } else {
                mostrarAlerta("El curso ingresado no existe.");
            }
        } else {
            mostrarAlerta("Todos los campos son obligatorios.");
        }
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() && !txtApellidos.getText().isEmpty() &&
                !txtDni.getText().isEmpty() && !txtLocalidad.getText().isEmpty() &&
                !txtEmail.getText().isEmpty() && !txtContrasena.getText().isEmpty() && !txtCurso.getText().isEmpty();
    }

    private void validarEmailTiempoReal() {
        txtEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$")) { // Patrón básico de email
                    txtEmail.setStyle("-fx-border-color: green;");
                    validarHabilitarGuardar();
                } else {
                    txtEmail.setStyle("-fx-border-color: red;");
                    btnGuardar.setDisable(true);
                }
            }
        });
    }

    private void validarDniTiempoReal() {
        txtDni.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("^[0-9]{8}[A-Z]$")) { // 8 números + 1 letra mayúscula
                    txtDni.setStyle("-fx-border-color: green;");
                    validarHabilitarGuardar();
                } else {
                    txtDni.setStyle("-fx-border-color: red;");
                    btnGuardar.setDisable(true);
                }
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
