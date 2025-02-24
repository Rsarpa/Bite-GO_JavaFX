package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlergenoDAO;
import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.Model.Alergeno;
import com.example.bitego_javafx.Model.Bocadillo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class    AdminBocadilloController implements Initializable {

    @FXML
    private TextField txtNombre, txtDescrip, txtPrecio;
    @FXML
    private ComboBox<String> tipoBox;
    @FXML
    private ComboBox<Integer> diaBox;
    @FXML
    private ComboBox<Alergeno> cmbAlergenos;
    @FXML
    private ListView<Alergeno> listAlergenos;
    @FXML
    private Button btnGuardar, btnAgregarAlergeno, btnEliminarAlergeno;

    private final BocadilloDAO bocadilloDAO = new BocadilloDAO();
    private final AlergenoDAO alergenoDAO = new AlergenoDAO();
    private Bocadillo bocadilloEditado;
    private CrudBocadilloController crudBocadilloController;
    private ObservableList<Alergeno> listaAlergenos = FXCollections.observableArrayList();
    private ObservableList<Alergeno> alergenosSeleccionados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoBox.setItems(FXCollections.observableArrayList("Frio", "Caliente"));
        diaBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        cargarAlergenos();
        listAlergenos.setItems(alergenosSeleccionados);
    }

    //Cargamos todos los alergenos de la DB en el ComboBox
    private void cargarAlergenos() {
        listaAlergenos.setAll(alergenoDAO.obtenerTodosAlergenos());
        cmbAlergenos.setItems(listaAlergenos);
    }


    //Agregar alergeno a el ListView de alergenos
    @FXML
    private void agregarAlergeno() {
        Alergeno seleccionado = cmbAlergenos.getValue();
        if (seleccionado != null && !alergenosSeleccionados.contains(seleccionado)) {
            alergenosSeleccionados.add(seleccionado);
        } else {
            mostrarAlerta("Seleccione un alérgeno válido o que no haya sido añadido.");
        }
    }

    //Eliminar un alergeno seleccionado en la lista de alérgenos
    @FXML
    private void eliminarAlergeno() {
        Alergeno seleccionado = listAlergenos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            alergenosSeleccionados.remove(seleccionado);
        } else {
            mostrarAlerta("Seleccione un alérgeno para eliminar.");
        }
    }

    //Cargar los datos del bocadillo acción editar
    public void cargarDatosBocadillo(Bocadillo bocadillo) {
        this.bocadilloEditado = bocadillo;
        txtNombre.setText(bocadillo.getNombre());
        tipoBox.setValue(bocadillo.getTipo());
        txtDescrip.setText(bocadillo.getDescripcion());
        txtPrecio.setText(String.valueOf(bocadillo.getPrecio_base()));
        diaBox.setValue(bocadillo.getDia_asociado());

        // Cargar los alérgenos que ya están asociados
        alergenosSeleccionados.setAll(bocadillo.getAlergenos());
    }


    //Guardar los cambios del bocadillo
    @FXML
    private void guardarBocadillo() {
        if (validarCampos()) {
            if (bocadilloEditado == null) {
                bocadilloEditado = new Bocadillo();
            }

            bocadilloEditado.setNombre(txtNombre.getText());
            bocadilloEditado.setTipo(tipoBox.getValue());
            bocadilloEditado.setDescripcion(txtDescrip.getText());

            try {
                bocadilloEditado.setPrecio_base(Float.parseFloat(txtPrecio.getText()));
            } catch (NumberFormatException e) {
                mostrarAlerta("Error: El precio debe ser un número válido.");
                return;
            }

            if (diaBox.getValue() == null) {
                mostrarAlerta("Error: Debes seleccionar un día.");
                return;
            }
            bocadilloEditado.setDia_asociado(diaBox.getValue());

            // Asignar los alérgenos seleccionados
            bocadilloEditado.setAlergenos(new HashSet<>(alergenosSeleccionados));

            // Guardar en la BD
            if (bocadilloDAO.existeBocadillo(txtNombre.getText()) == null) {
                bocadilloDAO.save(bocadilloEditado);
            } else {
                bocadilloDAO.update(bocadilloEditado);
            }

            crudBocadilloController.mostrarBocadillos();
            cerrarVentana();
        } else {
            mostrarAlerta("Todos los campos son obligatorios.");
        }
    }

    //TODO Service validar que todos los campos obligatorios estén incluidos
    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() &&
                tipoBox.getValue() != null &&
                !txtDescrip.getText().isEmpty() &&
                !txtPrecio.getText().isEmpty() &&
                diaBox.getValue() != null;
    }

    //Cierra la ventana actual
    @FXML
    private void cerrarVentana() {
        btnGuardar.getScene().getWindow().hide();
    }

    //Mostramos alerta
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    //Asignamos el admincontroler desde CrudBocadilloController
    public void setAdminController(CrudBocadilloController controller) {
        this.crudBocadilloController = controller;
    }
}
