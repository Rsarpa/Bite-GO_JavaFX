package com.example.bitego_javafx;

import Controler.PedidoDAO;
import com.example.bitego_javafx.Clases.Pedido;
import com.example.bitego_javafx.Util.Conexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.SessionFactory;

import java.sql.SQLException;

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
    public void initialize(){

        // Recorrer el spinner para recoger la selección del usuario
        for (MenuItem item : cursoFilter.getItems()) {
            item.setOnAction(event -> {
                cursoFilter.setText(item.getText()); // Mostrar selección en el botón
            });
        }

        String nombre = nombreFilter.getText();
        String apellido = apellidoFilter.getText();
        String curso = cursoFilter.getText(); // Obtener el curso seleccionado

        nombreAlu.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        lista_curso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        try {
            cargarDatos(nombre, apellido, curso);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void cargarDatos(String nombre, String apellido, String curso) throws SQLException{
        SessionFactory sf = Conexion.getSessionFactory();
        PedidoDAO pedido = new PedidoDAO(sf);
        pedido.listarPedidos(nombre,apellido,curso);
    }
}
