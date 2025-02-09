package com.example.bitego_javafx.Controller;

import DAO.PedidoDAO;
import com.example.bitego_javafx.Model.Pedido;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class CocinaController {
    @FXML
    private TableView<Pedido> tableView;

    @FXML
    private TableColumn<Pedido, String> nombreAlu;

    @FXML
    private TableColumn<Pedido, String> lista_curso;

    @FXML
    private TableColumn <Pedido, String> tipo;

    @FXML
    private Button cerrarSesion;

    @FXML
    private TextField nombreFilter;

    @FXML
    private TextField apellidoFilter;

    @FXML
    private SplitMenuButton cursoFilter;


    @FXML
    protected void cargarDatos(){

        // Recorrer el spinner para recoger la selección del usuario
        for (MenuItem item : cursoFilter.getItems()) {
            item.setOnAction(event -> {
                cursoFilter.setText(item.getText()); // Mostrar selección en el botón
            });
        }

        String nombre = nombreFilter.getText();
        String apellido = apellidoFilter.getText();
        String curso = cursoFilter.getText(); // Obtener el curso seleccionado

        //configurar las columnas con los atributos de la clase Pedido
        nombreAlu.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        lista_curso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        try {
            PedidoDAO pedido = new PedidoDAO();
            //Recoger resultado del metodo en List
            List<Pedido> pedidoList = pedido.listarPedidos(nombre,apellido,curso);

            ObservableList<Pedido> ol = FXCollections.observableArrayList(pedidoList);
            tableView.setItems(ol);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
