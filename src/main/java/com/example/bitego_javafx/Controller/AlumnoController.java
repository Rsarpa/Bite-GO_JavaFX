package com.example.bitego_javafx.Controller;

import DAO.BocadilloDao;
import com.example.bitego_javafx.Model.Alergeno;
import com.example.bitego_javafx.Model.Bocadillo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import java.util.List;

public class AlumnoController {

    @FXML
    private Label lblNombreFrio, lblTipoFrio, lblDescripcionFrio, lblPrecioFrio, lblAlergenosFrio;
    @FXML
    private Label lblNombreCaliente, lblTipoCaliente, lblDescripcionCaliente, lblPrecioCaliente, lblAlergenosCaliente;
    @FXML
    private ToggleButton btnFrio, btnCaliente;

    @FXML
    public void initialize() {
        cargarBocadillos();
    }

    private void cargarBocadillos() {
        BocadilloDao bocadilloDAO = new BocadilloDao();
        List<Bocadillo> bocadillosHoy = bocadilloDAO.obtenerBocadillosDelDia();

        for (Bocadillo b : bocadillosHoy) {
            if ("Frio".equalsIgnoreCase(b.getTipo())) {
                actualizarBocadilloFrio(b);
            } else if ("Caliente".equalsIgnoreCase(b.getTipo())) {
                actualizarBocadilloCaliente(b);
            }
        }
    }

    private void actualizarBocadilloFrio(Bocadillo bocadillo) {
        lblNombreFrio.setText(bocadillo.getNombre());
        lblTipoFrio.setText("Tipo: " + bocadillo.getTipo());
        lblDescripcionFrio.setText(bocadillo.getDescripcion());
        lblPrecioFrio.setText(String.format("Precio: %.2f€", bocadillo.getPrecio_base()));
        lblAlergenosFrio.setText("Alérgenos: " + obtenerListaAlergenos(bocadillo));
        btnFrio.setDisable(false);
    }

    private void actualizarBocadilloCaliente(Bocadillo bocadillo) {
        lblNombreCaliente.setText(bocadillo.getNombre());
        lblTipoCaliente.setText("Tipo: " + bocadillo.getTipo());
        lblDescripcionCaliente.setText(bocadillo.getDescripcion());
        lblPrecioCaliente.setText(String.format("Precio: %.2f€", bocadillo.getPrecio_base()));
        lblAlergenosCaliente.setText("Alérgenos: " + obtenerListaAlergenos(bocadillo));
        btnCaliente.setDisable(false);
    }

    private String obtenerListaAlergenos(Bocadillo bocadillo) {
        if (bocadillo.getAlergenos() != null && !bocadillo.getAlergenos().isEmpty()) {
            StringBuilder alergenos = new StringBuilder();
            for (Alergeno a : bocadillo.getAlergenos()) {
                alergenos.append(a.getNombre()).append(", ");
            }
            return alergenos.substring(0, alergenos.length() - 2); // Eliminar la última coma y espacio
        }
        return "Ninguno";
    }

    /*
    //Comprobar información
    for (Bocadillo b : bocadillosHoy) {
            System.out.println("Bocadillo: " + b.getNombre() + " - Tipo: " + b.getTipo());

            if (b.getAlergenos() != null && !b.getAlergenos().isEmpty()) {
                for (Alergeno a : b.getAlergenos()) {
                    System.out.println("  Alergeno: " + a.getNombre());
                }
            } else {
                System.out.println("  No tiene alérgenos.");
            }
        }
     */
}
