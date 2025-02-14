package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.DAO.UsuarioDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MovimientosController {
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
    private Label lblNumPedidos;
    @FXML
    private Label lblTotalGastado, lblEmail;

    @FXML
    private Button btnVolver;
    @FXML
    private Button btnAnterior, btnSiguiente;
    @FXML
    private TextField txtPagina;

    @FXML
    private ImageView iconVolver;
    @FXML
    private VBox sidePane;

    private Usuario usuario;
    private Alumno alumno;

    private static final int OFFSET = 14;
    private int currentPage = 1;
    private int totalPages = 1;
    private long totalPedidos;

    @FXML
    public void initialize() {
        configurarTabla();
        configurarFiltroTiempo();
        btnAnterior.setDisable(true);
    }

    private void configurarFiltroTiempo() {
        cbFiltroTiempo.setItems(FXCollections.observableArrayList("Mostrar 1 Mes", "Mostrar 3 Meses", "Mostrar 6 Meses", "Mostrar 1 Año"));
        cbFiltroTiempo.getSelectionModel().select(0);
        cbFiltroTiempo.setOnAction(event -> resetPaginacion());
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en MovimientosController: " + usuario.getEmail());

        if (usuario != null) {
            alumno = UsuarioDAO.obtenerAlumnoPorEmail(usuario.getEmail());
            lblEmail.setText(alumno.getEmail());
            resetPaginacion();
        }
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

    private void resetPaginacion() {
        currentPage = 1;
        cargarPedidos();
    }

    private void cargarPedidos() {
        if (alumno != null) {
            LocalDate fechaFiltro = calcularFechaFiltro();
            List<PedidoBocadillo> pedidos = PedidoDAO.obtenerPedidosDelAlumno(alumno.getId_alumno(), currentPage, OFFSET, fechaFiltro);
            ObservableList<PedidoBocadillo> pedidosObservable = FXCollections.observableArrayList(pedidos);
            tablaPedidos.setItems(pedidosObservable);

            totalPedidos = PedidoDAO.obtenerPedidosDelAlumno(alumno.getId_alumno(), 1, Integer.MAX_VALUE, fechaFiltro).size();
            totalPages = (int) Math.ceil((double) totalPedidos / OFFSET);
            lblNumPedidos.setText(String.format("Pedidos mostrados: %d de %d", pedidos.size(), totalPedidos));
            lblTotalGastado.setText(String.format("Total Gastado: %.2f€", calcularTotalGastoRango(fechaFiltro)));

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

    private double calcularTotalGastoRango(LocalDate fechaFiltro) {
        List<PedidoBocadillo> todosLosPedidos = PedidoDAO.obtenerPedidosDelAlumno(alumno.getId_alumno(), 1, Integer.MAX_VALUE, fechaFiltro);
        return Math.round(todosLosPedidos.stream().mapToDouble(PedidoBocadillo::getCosto_final).sum() * 100.0) / 100.0;
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

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1200, 800)); // Tamaño de la ventana
            stage.setTitle("Dashboard Alumno");
            stage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) lblNumPedidos.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
