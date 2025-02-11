package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.Alergeno;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import java.util.List;

public class AlumnoController {
    private Usuario usuario;
    private Alumno alumno;

    @FXML
    private Label lblNombreFrio, lblTipoFrio, lblDescripcionFrio, lblPrecioFrio, lblAlergenosFrio;
    @FXML
    private Label lblNombreCaliente, lblTipoCaliente, lblDescripcionCaliente, lblPrecioCaliente, lblAlergenosCaliente;
    @FXML
    private ToggleButton btnFrio, btnCaliente;

    @FXML
    private Label lblEmail;

    @FXML
    public void initialize() {
        //Carga los bocadillos asociados al dia
        cargarBocadillos();

        if(usuario!=null){
            alumno=UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
        }else{
            System.out.println("Alumno no inicializado");
        }

    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
    }

    private void cargarBocadillos() {
        BocadilloDAO bocadilloDAO = new BocadilloDAO();
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

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
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
