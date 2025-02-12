package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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

    //Reserva
    @FXML
    private VBox cardPedido;

    @FXML
    private Label lblNombrePedido, lblFechaLimite, lblRetirado, lblCosto;

    @FXML
    private Button btnCancelar;


    @FXML
    public void initialize() {

    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());

        if (usuario != null) {
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
            cargarBocadillos();

            // AHORA podemos inicializar correctamente
            if (comprobarPedidoDia()) {
                modoPedido();
            } else {
                modoNoPedido();
            }
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
        //System.out.println("Hermano quieres frio "+bocadilloFrio.getNombre());
        Date fechaActual = new Date(System.currentTimeMillis());
        //System.out.println(fechaActual);
        PedidoBocadillo pedidoFrio=new PedidoBocadillo(alumno,bocadilloFrio,fechaActual,false,bocadilloFrio.getPrecio_base(),null);
        //System.out.println("Donde estamos"+pedidoFrio);
        PedidoDAO pedidoDAO=new PedidoDAO();
        if(pedidoDAO.realizarPedido(pedidoFrio)){
            modoPedido();
        }
    }

    @FXML
    private void pedirCaliente(){
        if (mostrarAlerta()){
            Date fechaActual = new Date(System.currentTimeMillis());
            //System.out.println(fechaActual);
            //System.out.println("Hermano quieres Caliente "+bocadilloCaliente.getNombre());
            PedidoBocadillo pedidoCaliente=new PedidoBocadillo(alumno,bocadilloCaliente,fechaActual,false,bocadilloCaliente.getPrecio_base(),null);
            //System.out.println("Donde estamos "+pedidoCaliente);
            PedidoDAO pedidoDAO=new PedidoDAO();
            if(pedidoDAO.realizarPedido(pedidoCaliente)){
                modoPedido();
            }
        }
    }

    private Boolean mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Estás seguro?");
        alert.setContentText("Esta acción no se puede deshacer.");

        // Mostrar y esperar respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();

        // Evaluar la respuesta del usuario
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Usuario confirmó la acción.");
            return true;
        } else {
            System.out.println("Usuario canceló la acción.");
            return false;
        }
    }

    //Mostrar Pedido y Cambiar vista
    private void modoPedido(){
        PedidoDAO pedidoDAO=new PedidoDAO();
        Date fechaActual = new Date(System.currentTimeMillis());
        PedidoBocadillo pedidoHoy=pedidoDAO.obtenerPedidoDelDia(alumno.getId_alumno(),fechaActual);
        //TODO btn cancelar poner false cuando se inicializa
        btnCancelar.setVisible(true);
        btnFrio.setVisible(false);
        btnCaliente.setVisible(false);

        //private Label lblNombrePedido, lblFechaLimite, lblRetirado, lblCosto;

        lblNombrePedido.setText(pedidoHoy.getBocadillo().getNombre());

        //Asignarle siempre las 11 de cada día
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'a las' hh:mm a");
        String fechaFormateada = hoy.atTime(11, 0).format(formato);
        //Pasarle el formate y la fecha atTime

        lblFechaLimite.setText("Fecha Límite: " + fechaFormateada);

        if(pedidoHoy.getRetirado()){
            lblRetirado.setText("El Bocadillo ha sido retirado");
        }else{
            lblRetirado.setText("El Bocadillo aún no ha sido retirado");
        }

        //Convertimos el texto en String
        lblCosto.setText(pedidoHoy.getCosto_final().toString());
    }

    //Comprueba si ya hay un pedido existente
    private Boolean comprobarPedidoDia(){
        Date fechaActual = new Date(System.currentTimeMillis());
        PedidoDAO pedidoDAO=new PedidoDAO();
        if(pedidoDAO.obtenerPedidoDelDia(alumno.getId_alumno(),fechaActual)!=null){
            System.out.println("Hemos encontrado el pedido");
            return true;
        }else{
            return false;
        }
    }

    private void modoNoPedido(){
        btnCancelar.setVisible(false);
        btnFrio.setVisible(true);
        btnCaliente.setVisible(true);
        //private Label lblNombrePedido, lblFechaLimite, lblRetirado, lblCosto;
        lblNombrePedido.setText("");
        lblFechaLimite.setText("");
        lblRetirado.setText("");
        lblCosto.setText("");
    }
    @FXML
    private void cancelarPedido(){

    }

}
