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


        // Recorrer el spinner para recoger la selección del usuario
        /*for (MenuItem item : cursoFilter.getItems()) {
            item.setOnAction(event -> {
                cursoFilter.setText(item.getText()); // Mostrar selección en el botón
            });
        }*/

        String nombre = nombreFilter.getText();
        String apellido = apellidoFilter.getText();
        String curso = cursoFilter.getValue(); // Obtener el curso seleccionado

        //System.out.println("curso: " + curso);


        try {
            PedidoDAO pedido = new PedidoDAO();

            //Recoger resultado del metodo en List
            List<PedidoBocadillo> pedidoList = pedido.listarPedidos(nombre, apellido, curso);
            ObservableList<PedidoBocadillo> ol = FXCollections.observableArrayList(pedidoList);
            tableView.setItems(ol);


            colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id_pedido"));
            colNombreAlu.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getNombre()));
            colIdBocadillo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBocadillo().getNombre()));
            colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBocadillo().getTipo()));
            colCurso.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlumno().getCurso().getNombre_curso()));
            colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fecha_hora"));

            //boton de retirar pedido
            botonPreparar.setCellValueFactory(param -> {
                Button btnCheck = new Button("Check");
                //todo evento retirar bocadillo
                //btnCheck.setOnAction(event -> checkearPedido(param.getVaue));
                HBox hbox = new HBox(btnCheck);
                hbox.setStyle("-fx-alignment: Center");
                return new SimpleObjectProperty<>(hbox);
            });

        }catch (SQLException e) {
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
            //Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/login.fxml"));
           // Parent root = loader.load();
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 320, 240);

            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
