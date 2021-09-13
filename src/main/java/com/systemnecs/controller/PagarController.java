package com.systemnecs.controller;

import com.jfoenix.controls.JFXTextField;
import com.systemnecs.dao.VentaDAO;
import com.systemnecs.model.Venta;
import com.systemnecs.util.ConexionDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class PagarController implements Initializable {
    @FXML
    private VBox root;

    @FXML
    private Text txtTotalAPagar;

    @FXML
    private JFXTextField cjValorIngreso;

    @FXML
    private Text txtCambio;

    private Venta venta;

    private int idventa = 0;

    private double totalaPagar = 0;

    private NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);

    private ConexionDB conexionDB = new ConexionDB();
    private VentaDAO ventaDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void cjValorIngresoKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

            if (getValor() < totalaPagar) {
                org.controlsfx.control.Notifications.create().title("Aviso").text("Ingrese un valor mayor o igual al total a pagar").position(Pos.CENTER).showWarning();
                return;
            }

            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "¿CONTINUAR CON EL REGISTRO?", ButtonType.YES, ButtonType.NO);
            a.setHeaderText("Header");
            if (a.showAndWait().get() == ButtonType.YES) {
                conexionDB.conectar();
                this.ventaDAO = new VentaDAO(conexionDB);
                try {
                    if ( (idventa=ventaDAO.guardar(this.venta)) > 0 ) {
                        com.systemnecs.util.Metodos.closeEffect(root);
                        //JasperReport jr = (JasperReport) JRLoader.loadObject(new URL(getClass().getResource("/reports/factura.jasper").toString()));
                        //Map<String, Object> parametros = new HashMap<>();
                        //parametros.put("idventa", idventa);
                        //JasperPrint jasperprint = JasperFillManager.fillReport(jr, parametros, this.conexionDB.getConnection());
                        //JasperViewer viewer = new JasperViewer(jasperprint, false);
                        //viewer.setVisible(true);
                        //viewer.toFront();
                    }
                } catch (SQLException ex) {
                    //Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
                    org.controlsfx.control.Notifications.create().title("Aviso").text("NO SE PUDO REGISTRAR LA VENTA \n" + ex.getMessage()).position(Pos.CENTER).showError();
                } catch (Exception ex) {
                    //Logger.getLogger(RegistrarVentaController.class.getName()).log(Level.SEVERE, null, ex);
                    org.controlsfx.control.Notifications.create().title("Aviso").text("ERROR INESPERADO\n" + ex.getMessage()).position(Pos.CENTER).showError();
                }
            }
        }
    }

    @FXML
    private void cjValorIngresokeyReleased(KeyEvent event) {
        double pagar = getValor();
        txtCambio.setText(format.format((pagar - this.totalaPagar)));
    }

    public double getValor() {
        try {
            return format.parse(cjValorIngreso.getText()).doubleValue();
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
//            Logger.getLogger(PagarController.class.getName()).log(Level.SEVERE, null, ex);
            try {
                return format.parse("$" + cjValorIngreso.getText()).doubleValue();
            } catch (ParseException ex1) {
                System.out.println(ex1.getMessage());
//                Logger.getLogger(PagarController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return 0.0;

    }

    public void setVenta(Venta venta) {
        this.venta = venta;
        totalaPagar = this.venta.getDetalleventa().stream().mapToDouble(ped -> ped.getCantidad() * ped.getPrecioventa()).sum();
        txtTotalAPagar.setText(NumberFormat.getCurrencyInstance().format(totalaPagar));
    }

    public int getIdventa() {
        return idventa;
    }
}
