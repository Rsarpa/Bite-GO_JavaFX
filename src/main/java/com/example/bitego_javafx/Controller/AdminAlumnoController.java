package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.CursoDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Curso;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminAlumnoController {

    @FXML
    private TextField txtNombre, txtApellidos, txtDni, txtLocalidad, txtEmail, txtCurso, txtMotivoBaja;
    @FXML
    private CheckBox chkAbonado;
    @FXML
    private Button btnGuardar, btnCancelar;

    private AlumnoDAO alumnoDAO = new AlumnoDAO();
    private Alumno alumnoEditando;
    private AdminController adminController;

    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    public void cargarDatosAlumno(Alumno alumno) {
        this.alumnoEditando = alumno;
        txtNombre.setText(alumno.getNombre());
        txtApellidos.setText(alumno.getApellidos());
        txtDni.setText(alumno.getDni());
        txtLocalidad.setText(alumno.getLocalidad());
        txtEmail.setText(alumno.getEmail());
        txtCurso.setText(alumno.getCurso().getNombre_curso());
        txtMotivoBaja.setText(alumno.getMotivo_baja());
        chkAbonado.setSelected(alumno.isAbonado());
    }

    @FXML
    private void guardarAlumno() {
        if (validarCampos()) {
            if (alumnoEditando == null) { // Si es un nuevo alumno
                alumnoEditando = new Alumno();
            }

            alumnoEditando.setNombre(txtNombre.getText());
            alumnoEditando.setApellidos(txtApellidos.getText());
            alumnoEditando.setDni(txtDni.getText());
            alumnoEditando.setLocalidad(txtLocalidad.getText());
            alumnoEditando.setEmail(txtEmail.getText());
            alumnoEditando.setMotivo_baja(txtMotivoBaja.getText());
            alumnoEditando.setAbonado(chkAbonado.isSelected());

            // Buscar el curso por nombre_curso
            String nombreCurso = txtCurso.getText().trim();
            Curso curso = CursoDAO.getByNombre(nombreCurso);

            if (curso != null) {
                alumnoEditando.setCurso(curso);
                if (alumnoEditando.getId_alumno() == 0) {
                    alumnoDAO.save(alumnoEditando); // Guardar nuevo alumno
                } else {
                    alumnoDAO.update(alumnoEditando); // Actualizar alumno
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
                !txtEmail.getText().isEmpty() && !txtCurso.getText().isEmpty();
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
