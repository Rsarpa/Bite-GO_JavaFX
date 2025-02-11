package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.PedidoDAO;
import com.example.bitego_javafx.Model.PedidoBocadillo;
import com.example.bitego_javafx.Model.Usuario;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CocinaController implements Initializable {
    private Usuario usuario;

    @FXML
    private TableView<PedidoBocadillo> tableView;

    @FXML
    private TableColumn<PedidoBocadillo, Integer> colIdPedido;

    @FXML
    private TableColumn<PedidoBocadillo, Integer> colIdBocadillo;

    @FXML
    private TableColumn<PedidoBocadillo, LocalDateTime> colFechaHora;

    @FXML
    private TableColumn<PedidoBocadillo, Boolean> colRetirado;

    @FXML
    private TableColumn<PedidoBocadillo, Double> colCostoFinal;

    @FXML
    private TableColumn<PedidoBocadillo, Integer> colIdDescuento;
    /*@FXML
    private TableColumn<PedidoBocadillo, String> nombreAlu;

    @FXML
    private TableColumn<PedidoBocadillo, String> lista_curso;

    @FXML
    private TableColumn <PedidoBocadillo, String> tipo;*/

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

            //configurar las columnas con los atributos de la clase Pedido
            colIdPedido.setCellValueFactory(new PropertyValueFactory<>("id_pedido"));
            colIdBocadillo.setCellValueFactory(new PropertyValueFactory<>("id_bocadillo"));
            colFechaHora.setCellValueFactory(new PropertyValueFactory<>("fecha_hora"));
            colRetirado.setCellValueFactory(new PropertyValueFactory<>("retirado"));
            colCostoFinal.setCellValueFactory(new PropertyValueFactory<>("costo_final"));
            colIdDescuento.setCellValueFactory(new PropertyValueFactory<>("id_descuento"));

            //nombreAlu.setCellValueFactory(new PropertyValueFactory<>("id_alumno"));
            //lista_curso.setCellValueFactory(new PropertyValueFactory<>("id_bocadillo"));
            //lista_curso.setCellValueFactory(new PropertyValueFactory<>("id_curso"));
            //tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));


        }catch (SQLException e){
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

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
