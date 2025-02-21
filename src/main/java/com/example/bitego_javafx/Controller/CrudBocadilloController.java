package com.example.bitego_javafx.Controller;

import com.example.bitego_javafx.DAO.AlumnoDAO;
import com.example.bitego_javafx.DAO.BocadilloDAO;
import com.example.bitego_javafx.Model.Alergeno;
import com.example.bitego_javafx.Model.Alumno;
import com.example.bitego_javafx.Model.Bocadillo;
import com.example.bitego_javafx.Model.Usuario;
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
    private TableColumn<Bocadillo, String> colDescrip;
    @FXML
    private TableColumn<Bocadillo, String> colPrecio;
    @FXML
    private TableColumn<Bocadillo,String> colAlergenos;
    @FXML
    private TableColumn<Bocadillo,Integer> colDIa;

    @FXML
    private TextField txtFiltroNombre;
    @FXML
    private TextField txtPaginaActual;
    @FXML
    private Button btnAnterior,btnSiguiente;

    private BocadilloDAO bocadilloDAO = new BocadilloDAO();
    //Lo recogemos en un ArrayList por que son 14 bocadillos.
    private ObservableList<Bocadillo> listarBocadillos = FXCollections.observableArrayList();
    private int paginaActual = 1;
    private final int registrosPorPagina = 10;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Asocio las columnas con las propiedades del objeto
        this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        this.colDescrip.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        //Con el getValue obtenemos el bocadillo y posteriormente recuperamos la lista de alergenos de bocadillo
        colAlergenos.setCellValueFactory(cellData -> {
            Bocadillo bocadillo = cellData.getValue();
            return new SimpleStringProperty(bocadillo.getAlergenos().toString());
        });
        this.colDIa.setCellValueFactory(new PropertyValueFactory<>("dia_asociado"));

        this.colPrecio.setCellValueFactory(cellData -> {
            Bocadillo bocadillo = cellData.getValue();
            Float precio = bocadillo.getPrecio_base();

            // Verificamos si el precio es null antes de formatearlo
            String precioStr = (precio != null) ? String.format("%.2f €", precio) : "0.00 €";
            return new SimpleStringProperty(precioStr);
        });



        // Cargo los bocadillos al iniciar
        this.mostrarBocadillos();
    }
    /*

    private String obtenerListaAlergenos(Bocadillo bocadillo) {
        if (bocadillo != null && bocadillo.getAlergenos() != null && !bocadillo.getAlergenos().isEmpty()) {
            StringBuilder alergenos = new StringBuilder();
            for (Alergeno a : bocadillo.getAlergenos()) {
                alergenos.append(a.getNombre()).append(", ");
            }
            return alergenos.substring(0, alergenos.length() - 2); // Eliminar la última coma y espacio
        }
        return "Ninguno";
    }*/



    public void mostrarBocadillos() {
        String filtroNombre = txtFiltroNombre.getText();
        String nombreFiltrado = (filtroNombre != null && !filtroNombre.trim().isEmpty()) ? filtroNombre.trim() : null;

        List<Bocadillo> bocadillos = bocadilloDAO.getPaginated(paginaActual, registrosPorPagina, nombreFiltrado);

        // Asegurar que ningún bocadillo tenga precio null
        bocadillos.forEach(b -> {
            if (b.getPrecio_base() == null) {
                b.setPrecio_base(0.0f);
            }
        });

        listarBocadillos.setAll(bocadillos);
        tblBocadillos.setItems(listarBocadillos);
        txtPaginaActual.setText(String.valueOf(paginaActual));
        txtPaginaActual.setEditable(false);

        long totalRegistros = bocadilloDAO.count(nombreFiltrado);
        int totalPaginas = (int) Math.ceil((double) totalRegistros / registrosPorPagina);

        btnAnterior.setDisable(paginaActual == 1);
        btnSiguiente.setDisable(paginaActual >= totalPaginas);
    }



    @FXML
    private void filtrarPorNombre(ActionEvent event) {
        paginaActual = 1;
        mostrarBocadillos();
    }
//
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/AdminBocadillo.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load(), 500, 700);
            stage.setScene(scene);
            stage.setTitle("Añadir Bocadillo");

            // Enlazar con AdminController
            AdminBocadilloController controller = loader.getController();
            controller.setAdminController(this);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editarBocadillo(){
        Bocadillo bocadilloSeleccionado = tblBocadillos.getSelectionModel().getSelectedItem();
        if (bocadilloSeleccionado != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/AdminBocadillo.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(loader.load(), 500, 700);
                stage.setScene(scene);
                stage.setTitle("Editar Bocadillo");

                // Enlazar con AdminController
                AdminBocadilloController controller = loader.getController();
                controller.setAdminController(this);
                // Pasar datos al formulario
                controller.cargarDatosBocadillo(bocadilloSeleccionado);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Debe seleccionar un bocadillo para editar.");
        }
    }

    @FXML
    private void borrarBocadillo(){
        //Seleccionar esa linea (objeto bocadillo)
        Bocadillo bocadilloSeleccionado = tblBocadillos.getSelectionModel().getSelectedItem();
        if(bocadilloSeleccionado != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro quieres eliminar este bocadillo?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmar eliminación");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES){
                bocadilloDAO.delete(bocadilloSeleccionado);
                mostrarBocadillos();
            }
        }else {
            mostrarAlerta("Debe seleccionar un bocadillo para eliminar.");
        }
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
                mainStage.setMaximized(true);
                mainStage.show();
            }

            if (tipo.equals("Alumno")){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/bitego_javafx/dashboardAdmin.fxml"));
                // Parent root = loader.load();
                Stage mainStage = new Stage();

                Scene scene = new Scene(loader.load(), 800, 600);

                mainStage.setTitle("Hello!");
                mainStage.setScene(scene);
                mainStage.setMaximized(true);
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
            Scene scene = new Scene(loader.load(), 800, 450);
            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

