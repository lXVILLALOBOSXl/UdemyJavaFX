package com.systemnecs.controller;

import com.systemnecs.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.kordamp.ikonli.javafx.FontIcon;

public class DashboardController implements Initializable {
    @FXML
    private BorderPane root;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuArchivo;

    @FXML
    private MenuItem menuconfig;

    @FXML
    private MenuItem menuSalir;

    @FXML
    private Menu menuProductos;

    @FXML
    private MenuItem menuVerProductos;

    @FXML
    private Menu menuVentas;

    @FXML
    private MenuItem menuRealizarVenta;

    @FXML
    private Menu menuClientes;

    @FXML
    private MenuItem nuevoCliente;

    @FXML
    private JFXTabPane tabPane;

    private Tab tabProductos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void abrirConfiguracion(ActionEvent event) {

    }

    @FXML
    void mostraRealizarVenta(ActionEvent event) {

    }


    @FXML
    void mostrarTablaProductos(ActionEvent event) throws IOException {
        if (tabProductos == null){
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/fxml/Producto.fxml"));
            tabProductos = new Tab("PRODUCTOS", anchorPane);
            tabProductos.setGraphic(FontIcon.of(new FontIcon("fa-tags").getIconCode(), 20, Color.valueOf("FFF")));
            tabProductos.setClosable(true);
            tabProductos.setOnClosed(event1 -> {
                tabProductos = null;
            });
            tabPane.getTabs().add(tabProductos);
        }
        tabPane.getSelectionModel().select(tabProductos);
    }

    @FXML
    void nuevoCliente(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RegistrarCliente.fxml"));
        VBox vBox = fxmlLoader.load();
        Scene scene = new Scene(vBox);
        Stage stage = new Stage();
        stage.setTitle("Nuevo Cliente");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/add_user.png")));
        stage.setScene(scene);
        stage.initOwner(root.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setIconified(false);
        stage.showAndWait();
    }

    @FXML
    void salir(ActionEvent event) {

    }
}
