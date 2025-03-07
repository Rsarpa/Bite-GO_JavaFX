package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
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

    //Sesion
    @FXML
    private Button btnSalir;

    @FXML
    private void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("¿Seguro que quieres cerrar sesión?");
        alert.setContentText("Se perderán los datos de sesión.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {

                // Cerrar la ventana actual
                lblNombreFrio.getScene().getWindow().hide();

                // Cargar la pantalla de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/login.fxml"));
                Parent root = loader.load();

                // Obtener la ventana actual
                Stage stage = (Stage) btnSalir.getScene().getWindow();

                // Configurar nueva escena
                Scene scene = new Scene(root, 800, 450);
                stage.setScene(scene);
                stage.setMaximized(false);
                stage.show();

                System.out.println("Sesión cerrada correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al intentar cerrar sesión.");
            }
        }
    }
    //Méthod para acceder a la pantalla movimientos
    public void goHistorial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/movimientos.fxml"));
            Parent root = loader.load(); // Carga una nueva instancia del FXML

            // Obtiene el controlador a partir del loader y establece el usuario
            MovimientosController controller = loader.getController();
            controller.setUsuario(usuario);

            // Crea una nueva ventana , aunque deberiamos de reemplazar la actual
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600)); // Puedes ajustar el tamaño de la ventana
            stage.setTitle("Historial de Pedidos");
            stage.setMaximized(true);
            stage.show();

            // Cerramos la ventana actual
            Stage currentStage = (Stage) btnCaliente.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        if (usuario != null) {
            //Instanciado usuario con el setUsuario
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
            //Cargamos los bocadillos cuando se inicializa la ventana
            cargarBocadillos();

            //Inicializamos antes de que se cree el scene, asi ya all estará inicializado
            if (comprobarPedidoDia()) {
                modoPedido();
            } else {
                modoNoPedido();
            }
        }

    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());

        if (usuario != null) {
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
            //Cargamos los bocadillos antes de inicializar la ventana, habría que ver con cual quedarnos
            cargarBocadillos();

            //Inicializamos antes de que se cree el scene, asi ya all estará inicializado
            if (comprobarPedidoDia()) {
                modoPedido();
            } else {
                modoNoPedido();
            }
        }
    }

    private void cargarBocadillos() {
        //Cargamos los bocadillos del dia
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
        if (mostrarAlerta()){
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
        alert.setHeaderText("¿Estás seguro ?");
        alert.setContentText("Realizar Pedido");

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
        Date fechaActual = new Date(System.currentTimeMillis()); //Obtiene la fecha actual en milisegundos xd
        PedidoBocadillo pedidoHoy=pedidoDAO.obtenerPedidoDelDia(alumno.getId_alumno(),fechaActual);
        //TODO btn cancelar poner false cuando se inicializa
        btnCancelar.setVisible(true);
        //Poner invisibles los botones para cancelar un pedido
        btnFrio.setVisible(false);
        btnCaliente.setVisible(false);

        lblNombrePedido.setText(pedidoHoy.getBocadillo().getNombre());

        //Asignarle siempre las 11 de cada día es fecha límite para cancelar
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'a las' hh:mm a");
        String fechaFormateada = hoy.atTime(11, 0).format(formato);
        //Pasarle el formate y la fecha atTime

        lblFechaLimite.setText("Fecha Límite: " + fechaFormateada);

        if(pedidoHoy.getRetirado()){
            lblRetirado.setText("El Bocadillo ha sido retirado");
            //Si el pedido ha sido retirado ponemos en disable el btnCancelar el pedido
            btnCancelar.setDisable(true);
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
        //Vuelve a la versión inicial de la vista y resetea los lbl del Resumen

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
        Date fechaActual = new Date(System.currentTimeMillis());
        PedidoDAO pedidoDAO=new PedidoDAO();
        if(pedidoDAO.cancelarPedido(alumno.getId_alumno(),fechaActual)){
            modoNoPedido();
        }else{
            System.out.println("Pedido no cancelado");
        }
    }

}
