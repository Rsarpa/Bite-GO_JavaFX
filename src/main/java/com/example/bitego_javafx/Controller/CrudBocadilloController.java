package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CrudBocadilloController implements Initializable {
    private Usuario usuario;
    @FXML
    private TableView<Bocadillo> tblBocadillos;
    @FXML
    private TableColumn<Bocadillo, String> colNombre;
    @FXML
    private TableColumn<Bocadillo, String> colTipo;
    @FXML
    private TableColumn<Bocadillo, Integer> colDescrip;
    @FXML
    private TableColumn<Bocadillo, String> colPrecio;
    @FXML
    private TextField txtFiltroNombre;
    @FXML
    private TextField txtPaginaActual;
    @FXML
    private Button btnAnterior,btnSiguiente;

    private BocadilloDAO bocadilloDAO = new BocadilloDAO();
    private ObservableList<Bocadillo> listarBocadillos = FXCollections.observableArrayList();
    private int paginaActual = 1;
    private final int registrosPorPagina = 10;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Asocio las columnas con las propiedades del objeto
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        this.colDescrip.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio_base"));

        // Cargo los bocadillos al iniciar
        this.mostrarBocadillos();
    }

    private void mostrarBocadillos(){

        HashMap<String, String> filtros = new HashMap<>();
        if (!txtFiltroNombre.getText().isEmpty()){
            filtros.put("nombre", txtFiltroNombre.getText());
        }

        List<Bocadillo> bocadillos = bocadilloDAO.getPaginated(paginaActual, registrosPorPagina, filtros); //antes obtenerBocadillos() ahora con HashMap y paginado
        listarBocadillos.setAll(bocadillos);
        tblBocadillos.setItems(listarBocadillos);
        txtPaginaActual.setText(String.valueOf(paginaActual));

        // Deshabilitar el botón "Anterior" si estamos en la primera página
        btnAnterior.setDisable(paginaActual == 1);

        // Deshabilitar el botón "Siguiente" si la cantidad de resultados es menor al máximo por página
        btnSiguiente.setDisable(bocadillos.size() < registrosPorPagina);
    }

    @FXML
    private void filtrarPorNombre(KeyEvent event) {
        paginaActual = 1;
        mostrarBocadillos();
    }

    @FXML
    public void paginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            mostrarBocadillos();
        }
    }

    public void paginaSiguiente() {
        paginaActual++;
        mostrarBocadillos();
    }

    @FXML
    private void anyadirBocadillo(){
        //todo
    }

    @FXML
    private void editarBocadillo(){
        //todo
    }

    @FXML
    private void borrarBocadillo(){

    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());

    }

    public void menuBocadillo(ActionEvent event){
        String tipo="Bocadillo";
        cerrarVentana(event, "Bocadillo");
    }

    public void menuAlumno(ActionEvent event){
        String tipo="Alumno";
        cerrarVentana(event, "Alumno");
    }

    private void cerrarVentana(ActionEvent event, String tipo){
        try {
            // Cerrar la ventana actual
            tblBocadillos.getScene().getWindow().hide();

            if (tipo.equals("Bocadillo")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/crudBocadillo.fxml"));
                // Parent root = loader.load();
                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load(), 800, 600);

                mainStage.setTitle("Hello!");
                mainStage.setScene(scene);
                mainStage.show();
            }

            if (tipo.equals("Alumno")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/dashboardAdmin.fxml"));
                // Parent root = loader.load();
                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load(), 800, 600);

                mainStage.setTitle("Hello!");
                mainStage.setScene(scene);
                mainStage.show();
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event){
        try {

            // Cerrar la ventana actual
            tblBocadillos.getScene().getWindow().hide();

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

