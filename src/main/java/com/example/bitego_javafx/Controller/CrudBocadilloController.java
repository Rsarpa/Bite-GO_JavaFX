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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    private BocadilloDAO bocadilloDAO = new BocadilloDAO();
    private ObservableList<Bocadillo> listarBocadillos = FXCollections.observableArrayList();

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
        List<Bocadillo> bocadillos = bocadilloDAO.obtenerBocadillos();
        listarBocadillos.setAll(bocadillos);
        tblBocadillos.setItems(listarBocadillos);
    }

    @FXML
    private void filtrarPorNombre(KeyEvent event) {
        String filtro = txtFiltroNombre.getText();
        if (filtro == null || filtro.isEmpty()) {
            tblBocadillos.setItems(listarBocadillos);
        } else {
            List<Bocadillo> filtrados = listarBocadillos.stream()
                    .filter(alumno -> alumno.getNombre().toLowerCase().contains(filtro.toLowerCase()))
                    .collect(Collectors.toList());
            tblBocadillos.setItems(FXCollections.observableArrayList(filtrados));
        }
    }

    @FXML
    private void añadirBocadillo(){

    }

    @FXML
    private void editarBocadillo(){

    }

    @FXML
    private void borrarBocadillo(){

    }

    @FXML
    private void mostrarInformacion() {

    }

    @FXML
    private void mostrarInfo(){

    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        System.out.println("Usuario recibido en Dashboard: " + usuario.getEmail());
    }

    public void menuBocadillo(ActionEvent event){
        String tipo="Bocadillo";
        cerrarVentana(event, tipo);
    }

    public void menuAlumno(ActionEvent event){
        String tipo="Alumno";
        cerrarVentana(event, tipo);
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
}
