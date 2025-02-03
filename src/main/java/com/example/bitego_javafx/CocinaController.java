package com.example.bitego_javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class CocinaController {
    @FXML
    private TableView tableView;

    @FXML
    private TableColumn nombreAlu;

    @FXML
    private TableColumn curso;

    @FXML
    private TableColumn tipo;

    @FXML
    private Button cerrarSesion;

    public void initialize(){
        nombreAlu.setCellValueFactory(new PropertyValueFactory<>("nombreAlumno"));
        curso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        try {
            cargarDatos();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void cargarDatos() throws SQLException{

    }
}
