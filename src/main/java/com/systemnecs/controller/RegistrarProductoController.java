package com.systemnecs.controller;

import animatefx.animation.Tada;
import com.github.sarxos.webcam.Webcam;
import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.Producto;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

public class RegistrarProductoController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private JFXTextField cjnombre;

    @FXML
    private JFXTextField cjcodigo;

    @FXML
    private JFXTextField cjreferencia;

    @FXML
    private JFXTextField cjstock;

    @FXML
    private JFXTextField cjstockminimo;

    @FXML
    private JFXTextField cjprecio;

    @FXML
    private JFXDatePicker cjfechavencimiento;

    @FXML
    private JFXButton btnGuardar;

    @FXML
    private ImageView imageView;

    @FXML
    private JFXButton btnTomarFoto;

    @FXML
    private JFXButton btnBuscarImagen;

    private boolean estadoCamara = false;
    private Webcam selWebCam = null;
    private BufferedImage bufferImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cjprecio.setTextFormatter(new TextFormatter<>(new StringConverter<Double>(){

            @Override
            public String toString(Double object) {
                if (object != null) {
                    return NumberFormat.getCurrencyInstance().format(object);
                }
                return NumberFormat.getCurrencyInstance().format(0.0);
            }

            @Override
            public Double fromString(String valor) {
                try {
                    return NumberFormat.getCurrencyInstance().parse(valor).doubleValue();
                } catch (ParseException e) {
                    try {
                        return NumberFormat.getCurrencyInstance().parse("$".concat(valor)).doubleValue();
                    } catch (ParseException ex) { ex.printStackTrace(); }
                }
                return 0.0;
            }
        }));
    }

    @FXML
    void abrirCamara(ActionEvent event) {
        if (Webcam.getWebcams().size() < 1) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "NO HAY CAMARAS DISPONIBLES", ButtonType.OK);
            a.showAndWait();
            return;
        }

        if (!estadoCamara) {
            imageView.imageProperty().unbind();
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/load.gif")));
            Task<Void> webCamIntilizer = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (selWebCam == null) {
                        selWebCam = Webcam.getWebcams().get(0);
                        selWebCam.open();
                    }

                    estadoCamara = true;
                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            while (estadoCamara) {
                                try {
                                    if ((bufferImage = selWebCam.getImage()) != null) {
                                        Platform.runLater(() -> {
                                            final Image mainiamge = SwingFXUtils.toFXImage(bufferImage, null);
                                            imageProperty.set(mainiamge);
                                        });
                                        bufferImage.flush();
                                    }
                                } catch (Exception e) {
                                } finally {
                                }
                            }
                            return null;
                        }
                    };
                    Thread th = new Thread(task);
                    th.setDaemon(true);
                    th.start();
                    imageView.imageProperty().bind(imageProperty);

                    return null;
                }
            };
            new Thread(webCamIntilizer).start();
        } else {
            estadoCamara = false;
        }
    }

    @FXML
    void buscarImagen(ActionEvent event) {

    }

    @FXML
    void guardar(ActionEvent event) {

        if (cjnombre.getText().isEmpty()) {
            new Tada(cjnombre).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese el nombre del producto").position(Pos.CENTER).showWarning();
            return;
        }

        double precio = 0;

        try {
            precio = (NumberFormat.getCurrencyInstance().parse((cjprecio.getText())).doubleValue());
        } catch (ParseException ex) {
            new Tada(cjprecio).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Precio no valido\n" + ex.getMessage()).position(Pos.CENTER).showError();
            return;
        }

        if (cjfechavencimiento.getValue() == null) {
            new Tada(cjfechavencimiento).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Seleccione una fecha de vencimiento").position(Pos.CENTER).showWarning();
            return;
        }

        Producto producto = new Producto();
        producto.setNombreproducto(cjnombre.getText().trim());
        producto.setCodigodebarras(cjcodigo.getText().trim());
        producto.setReferencia(cjreferencia.getText().trim());
        producto.setStock(Double.parseDouble(cjstock.getText()));
        producto.setStockminimo(Double.parseDouble(cjstockminimo.getText()));
        producto.setPrecio(precio);
        producto.setFechadevencimiento(cjfechavencimiento.getValue());
        producto.setImagen(com.systemnecs.util.Metodos.ImageToByte(imageView.getImage()));


    }
}
