package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MovimientosController implements Initializable {
    @FXML
    private TableView<PedidoBocadillo> tablaPedidos;
    @FXML
    private TableColumn<PedidoBocadillo, Integer> colIdPedido;
    @FXML
    private TableColumn<PedidoBocadillo, String> colNombreBocadillo;
    @FXML
    private TableColumn<PedidoBocadillo, String> colFecha;
    @FXML
    private TableColumn<PedidoBocadillo, Double> colCosto;
    @FXML
    private TableColumn<PedidoBocadillo, Double> colDescuento;
    @FXML
    private ComboBox<String> cbFiltroTiempo;
    @FXML
    private DatePicker dpFechaInicio, dpFechaFin;
    @FXML
    private Button btnFiltrarFecha;
    @FXML
    private Label lblNumPedidos, lblTotalGastado, lblEmail;
    @FXML
    private Button btnAnterior, btnSiguiente;
    @FXML
    private TextField txtPagina;
    @FXML
    private VBox sidePane;
    @FXML
    private ImageView iconVolver;

    private Usuario usuario;
    private Alumno alumno;

    private static final int OFFSET = 14;
    private int currentPage = 1;
    private int totalPages = 1;
    private long totalPedidos;

    private LocalDate fechaFiltro = null;
    private LocalDate fechaInicio = null;
    private LocalDate fechaFin = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabla();
        configurarFiltroTiempo();
        configurarFiltroFechas();
        btnAnterior.setDisable(true);
    }

    private void configurarTabla() {
        colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id_pedido"));
        colNombreBocadillo.setCellValueFactory(cellData -> {
            PedidoBocadillo pedido = cellData.getValue();
            return new SimpleStringProperty(
                    (pedido != null && pedido.getBocadillo() != null) ? pedido.getBocadillo().getNombre() : ""
            );
        });
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_hora"));
        colCosto.setCellValueFactory(new PropertyValueFactory<>("costo_final"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("id_descuento"));
    }

    private void configurarFiltroTiempo() {
        cbFiltroTiempo.getItems().addAll("Mostrar 1 Mes", "Mostrar 3 Meses", "Mostrar 6 Meses", "Mostrar 1 Año");
        cbFiltroTiempo.getSelectionModel().select(0);
        cbFiltroTiempo.setOnAction(event -> {
            fechaInicio = null;
            fechaFin = null;
            fechaFiltro = calcularFechaFiltro();
            resetPaginacion();
        });
    }

    private void configurarFiltroFechas() {
        btnFiltrarFecha.setOnAction(event -> {
            if (dpFechaInicio.getValue() != null && dpFechaFin.getValue() != null) {
                fechaFiltro = null;
                fechaInicio = dpFechaInicio.getValue();
                fechaFin = dpFechaFin.getValue();
                resetPaginacion();
            } else {
                mostrarAlerta("Debes seleccionar ambas fechas para filtrar.");
            }
        });
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        if (usuario != null) {
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
            resetPaginacion();
        }
    }

    private void resetPaginacion() {
        currentPage = 1;
        cargarPedidos();
    }

    private void cargarPedidos() {
        if (alumno != null) {
            List<PedidoBocadillo> pedidos = PedidoDAO.obtenerPedidosDelAlumno(alumno.getId_alumno(), currentPage, OFFSET, fechaFiltro, fechaInicio, fechaFin);
            tablaPedidos.getItems().clear();
            tablaPedidos.getItems().addAll(pedidos);

            totalPedidos = PedidoDAO.obtenerTotalPedidos(alumno.getId_alumno(), fechaFiltro, fechaInicio, fechaFin);
            totalPages = (int) Math.ceil((double) totalPedidos / OFFSET);

            lblNumPedidos.setText(String.format("Pedidos mostrados: %d de %d", pedidos.size(), totalPedidos));
            lblTotalGastado.setText(String.format("Total Gastado: %.2f€", calcularTotalGasto()));

            actualizarBotonesPaginacion();
        }
    }

    private LocalDate calcularFechaFiltro() {
        String filtroSeleccionado = cbFiltroTiempo.getValue();
        LocalDate fechaActual = LocalDate.now();
        switch (filtroSeleccionado) {
            case "Mostrar 3 Meses": return fechaActual.minusMonths(3);
            case "Mostrar 6 Meses": return fechaActual.minusMonths(6);
            case "Mostrar 1 Año": return fechaActual.minusYears(1);
            default: return fechaActual.minusMonths(1);
        }
    }

    private double calcularTotalGasto() {
        return PedidoDAO.obtenerTotalGasto(alumno.getId_alumno(), fechaFiltro, fechaInicio, fechaFin);
    }

    private void actualizarBotonesPaginacion() {
        txtPagina.setText(String.valueOf(currentPage));
        btnAnterior.setDisable(currentPage == 1);
        btnSiguiente.setDisable(currentPage >= totalPages);
    }

    @FXML
    public void siguientePagina() {
        if (currentPage < totalPages) {
            currentPage++;
            cargarPedidos();
        }
    }

    @FXML
    public void anteriorPagina() {
        if (currentPage > 1) {
            currentPage--;
            cargarPedidos();
        }
    }

    @FXML
    private void volverAtras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/dashboardAlumno.fxml"));
            Parent root = loader.load();
            AlumnoController controller = loader.getController();
            controller.setUsuario(usuario);

            Stage stage = (Stage) lblNumPedidos.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 800);
            stage.setScene(scene);
            stage.setIconified(true); // Minimizar
            stage.setMaximized(true); // Maximizar después
            stage.setIconified(false); // Restaurar
            stage.setTitle("Dashboard Alumno");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
