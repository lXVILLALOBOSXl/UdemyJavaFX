package com.systemnecs.controller;

import animatefx.animation.Tada;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.systemnecs.dao.ClienteDAO;
import com.systemnecs.dao.ProductoDAO;
import com.systemnecs.model.*;
import com.systemnecs.util.ConexionDB;
import com.systemnecs.util.CurrencyCell;
import com.systemnecs.util.DoubleCell;
import com.systemnecs.util.SearchComboBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrarVentaController implements Initializable {
    @FXML
    private VBox root;

    @FXML
    private JFXTextField cjCodigoBarras;

    @FXML
    private TableView<DetalleVenta> tablaPedidos;

    @FXML
    private TableColumn<DetalleVenta, String> colProducto;

    @FXML
    private TableColumn colcantidad;

    @FXML
    private TableColumn colvalor;

    @FXML
    private TableColumn<DetalleVenta, Double> coltotal;

    @FXML
    private JFXButton btnQuitarProducto;

    @FXML
    private JFXButton btnAgregarProducto;

    @FXML
    private Text txtTituloEmpresa;

    @FXML
    private GridPane gridPane;

    @FXML
    private JFXDatePicker cjFecha;

    @FXML
    private JFXComboBox<String> comboFormaDePago;

    @FXML
    private Label lblIva;

    @FXML
    private Text txtSubtotal;

    @FXML
    private Text txtIva;

    @FXML
    private Text txtTotal;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnPagar;

    @FXML
    private JFXTextField cjBuscarProducto;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colProductos;

    SearchComboBox<Cliente> comboCliente = new SearchComboBox<>();

    private ConexionDB conexionDB = new ConexionDB();
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;

    FilteredList<Producto> filtro;
    ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    ObservableList<DetalleVenta> listaPedido = FXCollections.observableArrayList();

    private Integer iva = Comercio.getInstance(null).getIva();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnPagar.disableProperty().bind(Bindings.isEmpty(listaPedido));

        cjFecha.setValue(LocalDate.now());

        cjCodigoBarras.requestFocus();
        comboFormaDePago.getItems().addAll("EFECTIVO", "TARJETA CREDITO", "TARJETA DEBITO");
        comboFormaDePago.getSelectionModel().selectFirst();

        tablaPedidos.setEditable(true);
        tablaPedidos.getSelectionModel().setCellSelectionEnabled(true);
        tablaPedidos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaPedidos.setItems(listaPedido);
        tablaPedidos.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        colProducto.setCellValueFactory(tc -> tc.getValue().getProducto().nombreproductoProperty());

        colcantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colcantidad.setStyle("-fx-alignment: CENTER;");
        colcantidad.setCellFactory(tc -> new DoubleCell<>());
        colcantidad.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DetalleVenta, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DetalleVenta, Double> e) {
                if (!Objects.equals(e.getNewValue(), e.getOldValue())) {
                    ((DetalleVenta) e.getTableView().getItems().get(e.getTablePosition().getRow())).setCantidad(e.getNewValue());
                    calcular();
                    com.systemnecs.util.Metodos.changeSizeOnColumn(coltotal, tablaPedidos, e.getTablePosition().getRow());
                }
            }
        });


        colvalor.setCellValueFactory(new PropertyValueFactory<>("precioventa"));
        colvalor.setCellFactory(tc -> new CurrencyCell<>());
        colvalor.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DetalleVenta, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DetalleVenta, Double> e) {
                if (!Objects.equals(e.getNewValue(), e.getOldValue())) {
                    ((DetalleVenta) e.getTableView().getItems().get(e.getTablePosition().getRow())).setPrecioventa(e.getNewValue());
                    calcular();
                    com.systemnecs.util.Metodos.changeSizeOnColumn(colvalor, tablaPedidos, e.getTablePosition().getRow());
                }
            }
        });

        coltotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        coltotal.setCellFactory(tc -> new CurrencyCell<>());
        coltotal.setEditable(false);

        try {
            this.conexionDB.conectar();
            clienteDAO = new ClienteDAO(this.conexionDB);
            comboCliente.getItems().addAll(clienteDAO.getAll());
            comboCliente.setFilter((Cliente t, String u) -> {
                if (t.getNombrecliente().toUpperCase().contains(u.toUpperCase())) {
                    return true;
                } else if (t.getApellidocliente().toUpperCase().contains(u.toUpperCase())) {
                    return true;
                }
                return false;
            });
        } catch (SQLException ex) {
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.conexionDB.CERRAR();
        }

        gridPane.add(comboCliente,0,1);
        comboCliente.setMaxWidth(Double.MAX_VALUE);

        try {
            this.conexionDB.conectar();
            productoDAO = new ProductoDAO(this.conexionDB);
            listaProductos.addAll(productoDAO.getAll());
            tablaProductos.setItems(listaProductos);
            filtro = new FilteredList(listaProductos, p -> true);
            colProductos.setCellValueFactory(param -> param.getValue().nombreproductoProperty());
        } catch (SQLException ex) {
            org.controlsfx.control.Notifications.create().title("Aviso").text("No se cargaron los productos").position(Pos.CENTER).showWarning();
            Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        txtTituloEmpresa.setText(Comercio.getInstance(null).getNombre());
        lblIva.setText("IVA: ("+this.iva+"%)");
    }

    @FXML
    void buscarCodigo(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && cjCodigoBarras.getText() != null && !cjCodigoBarras.getText().isEmpty()) {

            listaPedido.stream().
                    filter(p -> p.getProducto().getCodigodebarras().equals(cjCodigoBarras.getText()))
                    .findFirst().map((t) -> {
                t.setCantidad((t.getCantidad() + 1));
                cjCodigoBarras.setText(null);
                return t;
            }).orElseGet(() -> {
                listaProductos.stream()
                        .filter((p) -> p.getCodigodebarras().contains(cjCodigoBarras.getText()))
                        .findFirst()
                        .ifPresent(p -> {
                            DetalleVenta dv = new DetalleVenta();
                            dv.setProducto(p);
                            dv.setCantidad(1);
                            dv.setPrecioventa(p.getPrecio());
                            listaPedido.add(dv);
                            com.systemnecs.util.Metodos.changeSizeOnColumn(colProducto, tablaPedidos, -1);
                            cjCodigoBarras.setText(null);
                        });
                return null;
            });

            calcular();
        } else if (event.getCode() == KeyCode.DOWN) {
            tablaPedidos.requestFocus();
            tablaPedidos.getFocusModel().focus(0, colcantidad);
            tablaPedidos.getSelectionModel().select(0, colcantidad);
        } else if (event.getCode() == KeyCode.RIGHT && cjCodigoBarras.getText() == null) {
            cjBuscarProducto.requestFocus();
        }
    }

    @FXML
    void buscarProducto(KeyEvent event) {
        switch (event.getCode()) {
            case DOWN:
                tablaProductos.requestFocus();
                tablaProductos.getSelectionModel().select(0, colProductos);
                break;
            case ESCAPE:
                cjCodigoBarras.requestFocus();
                break;
            default:
                cjBuscarProducto.textProperty().addListener((observableValue, oldValue, newValue) -> {
                    filtro.setPredicate((Predicate<? super Producto>) param -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        return (param.getNombreproducto().contains(newValue.toUpperCase()));
                    });
                });
                SortedList<Producto> sorterData = new SortedList<>(filtro);
                sorterData.comparatorProperty().bind(tablaProductos.comparatorProperty());
                tablaProductos.setItems(sorterData);
        }
    }

    @FXML
    void cancelarPedido(ActionEvent event) {

    }

    @FXML
    void pagar(ActionEvent event) throws IOException {

        if(comboCliente.getSelectionModel().getSelectedItem()==null){
            new Tada(comboCliente).play();
            org.controlsfx.control.Notifications.create().title("Aviso").text("Seleccione un cliente").position(Pos.CENTER).showWarning();
            return;
        }

        Venta v = new Venta();
        v.setCliente(comboCliente.getSelectionModel().getSelectedItem());
        v.setFormadepago(comboFormaDePago.getSelectionModel().getSelectedItem());
        v.setDetalleventa(listaPedido);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Pagar.fxml"));
        VBox vbox = loader.load();

        PagarController controller = loader.getController();
        controller.setVenta(v);

        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setTitle("Confirmar venta");
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/productos.png")));
        stage.setScene(scene);
        stage.initOwner(root.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setIconified(false);
        stage.showAndWait();

        if(controller.getIdventa() > 0){
            comboCliente.getSelectionModel().clearSelection();
            listaPedido.clear();
            cjCodigoBarras.requestFocus();
            txtSubtotal.setText("$0");
            txtIva.setText("$0");
            txtTotal.setText("$0");
        }
    }

    @FXML
    void restarCantidad(ActionEvent event) {
        listaPedido.removeAll(tablaPedidos.getSelectionModel().getSelectedItems().filtered(p -> p.getCantidad() == 1));
        tablaPedidos.getSelectionModel().getSelectedItems().stream().filter(p -> p.getCantidad() > 1).forEach(p -> {
            p.setCantidad((p.getCantidad() - 1));
        });
        calcular();
    }

    @FXML
    void sumarCantidad(ActionEvent event) {
        tablaPedidos.getSelectionModel().getSelectedItems().stream().forEach(p -> {
            p.setCantidad((p.getCantidad() + 1));
            calcular();
        });
    }

    @FXML
    void tablaPedidosKeyPressed(KeyEvent event) {
        if (event.getCode().isDigitKey()) {
            final TablePosition focusedCell = tablaPedidos.focusModelProperty().get().focusedCellProperty().get();
            tablaPedidos.edit(focusedCell.getRow(), focusedCell.getTableColumn());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            cjCodigoBarras.requestFocus();
            tablaPedidos.getSelectionModel().clearSelection();
        }
    }

    @FXML
    void tablaProductosKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            cjBuscarProducto.requestFocus();
            tablaProductos.getSelectionModel().clearSelection();
        } else if (event.getCode() == KeyCode.ENTER) {
            agregarPedido(tablaProductos.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void tablaProductosMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Producto pr = tablaProductos.getSelectionModel().getSelectedItem();
            agregarPedido(pr);
        }
    }

    void agregarPedido(Producto pr) {
        listaPedido.stream().
                filter(p -> p.getProducto().getIdproducto() == (pr.getIdproducto()))
                .findFirst().map((t) -> {
            t.setCantidad((t.getCantidad() + 1));
            com.systemnecs.util.Metodos.changeSizeOnColumn(coltotal, tablaPedidos, -1);
            cjCodigoBarras.setText(null);
            return t;
        }).orElseGet(() -> {
            DetalleVenta dv = new DetalleVenta();
            dv.setProducto(pr);
            dv.setCantidad(1);
            dv.setPrecioventa(pr.getPrecio());
            listaPedido.add(dv);
            com.systemnecs.util.Metodos.changeSizeOnColumn(colProductos, tablaPedidos, -1);
            cjCodigoBarras.setText(null);
            return dv;
        });
        calcular();
    }


    private void calcular() {
        double suma = listaPedido.stream().mapToDouble(ped -> ped.getCantidad() * ped.getPrecioventa()).sum();
        double iva = (suma*this.iva)/100.0;
        txtIva.setText(NumberFormat.getCurrencyInstance().format(iva));
        txtSubtotal.setText(NumberFormat.getCurrencyInstance().format((suma)));
        txtTotal.setText(NumberFormat.getCurrencyInstance().format((suma+iva)));
    }
}
