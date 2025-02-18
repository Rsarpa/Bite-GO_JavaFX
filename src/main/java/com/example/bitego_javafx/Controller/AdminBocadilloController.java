package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.Model.Bocadillo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminBocadilloController implements Initializable {

    @FXML
    private TextField txtNombre, txtDescrip, txtPrecio;
    @FXML
    private ComboBox<String> tipoBox;
    @FXML
    private ComboBox<Integer> diaBox;
    @FXML
    private Button btnGuardar;

    private BocadilloDAO bocadilloDAO = new BocadilloDAO();
    private Bocadillo bocadilloEditado;
    private CrudBocadilloController crudBocadilloController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> list = new ArrayList<>();
        list.add("Frio");
        list.add("Caliente");
        ObservableList ol = FXCollections.observableList(list);
        tipoBox.getItems().clear();
        tipoBox.setItems(ol);

        List <Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        ObservableList ol1 = FXCollections.observableList(list1);
        diaBox.getItems().clear();
        diaBox.setItems(ol1);
    }

    public void setAdminController(CrudBocadilloController cru){
        this.crudBocadilloController = cru;
    }

    public void cargarDatosBocadillo(Bocadillo bocadillo){
        this.bocadilloEditado = bocadillo;
        txtNombre.setText(bocadillo.getNombre());
        tipoBox.setValue(bocadillo.getTipo());
        txtDescrip.setText(bocadillo.getDescripcion());
        txtPrecio.setText(bocadillo.getPrecio_base().toString());
        diaBox.setValue(bocadillo.getDia_asociado());
    }

    @FXML
    private void guardarBocadillo(){
        if (validarCampos()){
            if (bocadilloEditado == null){
                bocadilloEditado = new Bocadillo();
            }

            bocadilloEditado.setNombre(txtNombre.getText());
            bocadilloEditado.setTipo(tipoBox.getValue());
            bocadilloEditado.setDescripcion(txtDescrip.getText());

            //controlar el tipo de valor del input
            try{
                bocadilloEditado.setPrecio_base(Float.parseFloat(txtPrecio.getText()));
            }catch (NumberFormatException e){
                System.out.println("Error: El texto no es un número válido. Tipo Float");
            }

            bocadilloEditado.setDia_asociado(diaBox.getValue());

            //si es un bocadillo nuevo darlo de alta, de lo contrario se editará
            String nombreBocadillo = txtNombre.getText().trim();
            Bocadillo bocadillo = bocadilloDAO.existeBocadillo(nombreBocadillo);

            if (bocadillo == null){
                bocadilloDAO.save(bocadilloEditado);
                //llamar a la clase controller devolver valores actualizados y cerrar ventana
                crudBocadilloController.mostrarBocadillos();
                cerrarVentana();
            }else {
                bocadilloDAO.update(bocadilloEditado);
                crudBocadilloController.mostrarBocadillos();
                cerrarVentana();
            }
        }else {
            mostrarAlerta("Todos los campos son obligatorios");
        }
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() && !tipoBox.getValue().isEmpty() &&
                !txtDescrip.getText().isEmpty() && !txtPrecio.getText().isEmpty() &&
                diaBox.getValue()>=1 || diaBox.getValue()<=5;
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
