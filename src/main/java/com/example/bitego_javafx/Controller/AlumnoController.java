package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class AlumnoController {
    private Usuario usuario;
    private Alumno alumno;
    private List<Bocadillo> bocadillosHoy;
    private Bocadillo bocadilloFrio,bocadilloCaliente;

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
        /*//Carga los bocadillos asociados al dia
        cargarBocadillos();
        //System.out.println("Usuario en initializate"+usuario.getEmail());
        if(usuario!=null){
            alumno=UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
        }else{
            System.out.println("Alumno no inicializado");
        }*/

    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());

        if (usuario != null) {
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
            cargarBocadillos();  // Mueve la carga de bocadillos aquí para asegurarte de que el usuario ya está disponible
        }
    }


    private void cargarBocadillos() {
        BocadilloDAO bocadilloDAO = new BocadilloDAO();
        bocadillosHoy = bocadilloDAO.obtenerBocadillosDelDia();

        for (Bocadillo b : bocadillosHoy) {
            if ("Frio".equalsIgnoreCase(b.getTipo())) {
                bocadilloFrio=b;
                actualizarBocadilloFrio(b);
            } else if ("Caliente".equalsIgnoreCase(b.getTipo())) {
                bocadilloCaliente=b;
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
    @FXML
    private void pedirFrio(){
        System.out.println("Hermano quieres frio "+bocadilloFrio.getNombre());
        Date fechaActual = new Date(System.currentTimeMillis());
        System.out.println(fechaActual);
        PedidoBocadillo pedidoFrio=new PedidoBocadillo(alumno,bocadilloFrio,fechaActual,false,bocadilloFrio.getPrecio_base(),null);
        System.out.println("Donde estamos"+pedidoFrio);
        PedidoDAO pedidoDAO=new PedidoDAO();
        pedidoDAO.realizarPedido(pedidoFrio);
    }

    @FXML
    private void pedirCaliente(){
        Date fechaActual = new Date(System.currentTimeMillis());
        System.out.println(fechaActual);
        System.out.println("Hermano quieres Caliente "+bocadilloCaliente.getNombre());
        PedidoBocadillo pedidoCaliente=new PedidoBocadillo(alumno,bocadilloCaliente,fechaActual,false,bocadilloCaliente.getPrecio_base(),null);
        System.out.println("Donde estamos "+pedidoCaliente);
        PedidoDAO pedidoDAO=new PedidoDAO();
        pedidoDAO.realizarPedido(pedidoCaliente);
    }
}
