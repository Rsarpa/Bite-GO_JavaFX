package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.Model.Bocadillo;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminBocadilloController {

    @FXML
    private TextField txtNombre, txtTipo, txtDescrip, txtPrecio, txtDiaAsoc;
    @FXML
    private Button btnGuardar;

    private BocadilloDAO bocadilloDAO = new BocadilloDAO();
    private Bocadillo bocadilloEditado;
    private CrudBocadilloController crudBocadilloController;


    public void setAdminController(CrudBocadilloController cru){
        this.crudBocadilloController = cru;
    }

    public void cargarDatosBocadillo(Bocadillo bocadillo){
        this.bocadilloEditado = bocadillo;
        txtNombre.setText(bocadillo.getNombre());
        txtTipo.setText(bocadillo.getTipo());
        txtDescrip.setText(bocadillo.getDescripcion());
        txtPrecio.setText(bocadillo.getPrecio_base().toString());

        int dia_asoc = bocadillo.getDia_asociado();
        txtDiaAsoc.setText(Integer.toString(dia_asoc));
    }

    @FXML
    private void guardarBocadillo(){
        if (validarCampos()){
            if (bocadilloEditado == null){
                bocadilloEditado = new Bocadillo();
            }

            bocadilloEditado.setNombre(txtNombre.getText());
            bocadilloEditado.setTipo(txtTipo.getText());
            bocadilloEditado.setDescripcion(txtDescrip.getText());

            //controlar el tipo de valor del imput
            try{
                bocadilloEditado.setPrecio_base(Float.parseFloat(txtPrecio.getText()));
            }catch (NumberFormatException e){
                System.out.println("Error: El texto no es un número válido. Tipo Float");
            }

            try {
                bocadilloEditado.setDia_asociado(Integer.parseInt(txtDiaAsoc.getText()));
            }catch (NumberFormatException e){
                System.out.println("Error: El texto no es un número válido. Tipo integer");
            }

            //GUARDAR NUEVO BOCADILLO
            bocadilloDAO.save(bocadilloEditado);
        }else {
            mostrarAlerta("Todos los campos son obligatorios");
        }
    }

    private boolean validarCampos() {
        return !txtNombre.getText().isEmpty() && !txtTipo.getText().isEmpty() &&
                !txtDescrip.getText().isEmpty() && !txtPrecio.getText().isEmpty() &&
                !txtDiaAsoc.getText().isEmpty();
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
