package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class CocinaController implements Initializable {
    private Usuario usuario;
    private boolean mostrandoRetirados = false; // Para alternar entre pedidos normales y retirados

    @FXML
    private TableView<PedidoBocadillo> tableView;

    @FXML
    private TableColumn<PedidoBocadillo, String> colAlumno;

    @FXML
    private TableColumn<PedidoBocadillo, String> colIdBocadillo;

    @FXML
    private TableColumn<PedidoBocadillo, String> colCurso;

    @FXML
    private TableColumn<PedidoBocadillo, HBox> botonPreparar;

    @FXML
    private Button btnMostrarRetirados;

    @FXML
    private TextField nombreFilter;

    @FXML
    private TextField apellidoFilter;

    @FXML
    private ComboBox<String> cursoFilter;

    @FXML
    private Button btnAnterior,btnSiguiente;

    private HashMap<String, String> filtros = new HashMap<>();

    private int paginaActual = 1;
    private final int registrosPorPagina = 10;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
        cargarDatos();
    }

    @FXML
    protected void cargarDatos() {
        filtros.clear();
        filtros.clear();

        if (!nombreFilter.getText().isEmpty()) filtros.put("nombre", nombreFilter.getText());
        if (!apellidoFilter.getText().isEmpty()) filtros.put("apellido", apellidoFilter.getText());
        if (cursoFilter.getValue()!=null) filtros.put("curso", (String) cursoFilter.getValue());

        PedidoDAO pedidoDAO = new PedidoDAO();

        try {
            List<PedidoBocadillo> pedidoList;
            if (mostrandoRetirados) {
                pedidoList = pedidoDAO.listarPedidosRetirados(filtros);
                btnMostrarRetirados.setText("Mostrar Pedidos Pendientes");
            } else {
                pedidoList = pedidoDAO.listarPedidos(filtros);
                btnMostrarRetirados.setText("Mostrar Pedidos Retirados");
            }

            ObservableList<PedidoBocadillo> ol = FXCollections.observableArrayList(pedidoList);
            tableView.setItems(ol);

            colIdBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBocadillo().getNombre()));
            colAlumno.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getNombre() + " " + cellData.getValue().getAlumno().getApellidos()));
            colCurso.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getCurso().getNombre_curso()));

            // Agregar botones de acción según el estado de los pedidos
            botonPreparar.setCellValueFactory(param -> {
                PedidoBocadillo pedidoBocadillo = param.getValue();
                Button actionButton = new Button(mostrandoRetirados ? "Desmarcar" : "Check");

                botonPreparar.setMinWidth(100);
                botonPreparar.setMaxWidth(120);
                actionButton.setOnAction(event -> {
                    try {
                        if (mostrandoRetirados) {
                            pedidoDAO.marcarNoRetirado(pedidoBocadillo.getId_pedido());
                            System.out.println("Pedido " + pedidoBocadillo.getId_pedido() + " marcado como NO retirado.");
                        } else {
                            pedidoDAO.marcarRetirado(pedidoBocadillo.getId_pedido());
                            System.out.println("Pedido " + pedidoBocadillo.getId_pedido() + " marcado como retirado.");
                        }
                        cargarDatos();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                long totalRegistros = pedidoDAO.count();
                int totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);

                btnAnterior.setDisable(paginaActual == 1);
                btnSiguiente.setDisable(paginaActual >= totalPaginas);

                HBox hbox = new HBox(actionButton);
                hbox.setStyle("-fx-alignment: center");
                return new SimpleObjectProperty<>(hbox);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void alternarPedidosRetirados() {
        mostrandoRetirados = !mostrandoRetirados;
        cargarDatos();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> list = new ArrayList<>();
        list.add("1ºESO");
        list.add("2ºESO");
        list.add("3ºESO");
        list.add("4ºESO");
        ObservableList ol = FXCollections.observableList(list);
        cursoFilter.getItems().clear();
        cursoFilter.setItems(ol);
    }

    @FXML
    public void paginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            cargarDatos();
        }
    }

    public void paginaSiguiente() {
        paginaActual++;
        cargarDatos();
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        try {
            tableView.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/login.fxml"));
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 800, 450);
            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
