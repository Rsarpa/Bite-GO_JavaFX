package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CocinaController implements Initializable {
    private Usuario usuario;

    @FXML
    private TableView<PedidoBocadillo> tableView;

    @FXML
    private TableColumn<PedidoBocadillo, Integer> colIdPedido;

    @FXML
    private TableColumn<PedidoBocadillo, String> colIdBocadillo;

    @FXML
    private TableColumn<PedidoBocadillo, LocalDateTime> colFechaHora;

    @FXML
    private TableColumn<PedidoBocadillo, String> colNombreAlu;

    @FXML
    private TableColumn<PedidoBocadillo, String> colCurso;

    @FXML
    private TableColumn <PedidoBocadillo, String> colTipo;
    @FXML
    private TableColumn <PedidoBocadillo, HBox> botonPreparar;
    private HashMap<String, String> filtros = new HashMap<>();


    @FXML
    private Button cerrarSesion;

    @FXML
    private TextField nombreFilter;

    @FXML
    private TextField apellidoFilter;

    @FXML
    private ComboBox<String> cursoFilter;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
        cargarDatos();
    }

    @FXML
    protected void cargarDatos(){

        filtros.clear();

        if (!nombreFilter.getText().isEmpty()) filtros.put("nombre", nombreFilter.getText());
        if (!apellidoFilter.getText().isEmpty()) filtros.put("apellido", apellidoFilter.getText());
        if (cursoFilter.getValue()!=null) filtros.put("curso", (String) cursoFilter.getValue());


        try {

            PedidoDAO pedido = new PedidoDAO();

            //Recoger resultado del metodo en List
            List<PedidoBocadillo> pedidoList = pedido.listarPedidos(filtros);
            ObservableList<PedidoBocadillo> ol = FXCollections.observableArrayList(pedidoList);
            tableView.setItems(ol);


            colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id_pedido"));
            //colNombreAlu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getNombre()));
            colIdBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBocadillo().getNombre()));
            colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBocadillo().getTipo()));
            //colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fecha_hora"));
            //colCurso.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getCurso().getNombre_curso()));

            //boton de retirar pedido
            botonPreparar.setCellValueFactory(param -> {
                //instancia para recoger el objeto de esa fila
                PedidoBocadillo pedidoBocadillo = param.getValue();
                Button btnCheck = new Button("Check");

                btnCheck.setOnAction(event -> {
                    try{
                        pedido.marcarRetirado(pedidoBocadillo.getId_pedido());
                        System.out.println("Pedido " + pedidoBocadillo.getId_pedido() + " marcado como retirado.");
                        cargarDatos();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
                HBox hbox = new HBox(btnCheck);
                hbox.setStyle("-fx-alignment: Center");
                return new SimpleObjectProperty<>(hbox);
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, java.util.ResourceBundle resourceBundle) {
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
    public void cerrarSesion(ActionEvent event){
        try {

            // Cerrar la ventana actual
            tableView.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/login.fxml"));
           // Parent root = loader.load();
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 350, 300);

            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
