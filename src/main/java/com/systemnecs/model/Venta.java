package com.systemnecs.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private final IntegerProperty idventa = new SimpleIntegerProperty();
    private Cliente cliente;
    private Usuario usuario;
    private final ObjectProperty<LocalDateTime> fecha = new SimpleObjectProperty<>();
    private final IntegerProperty numerofactura = new SimpleIntegerProperty();
    private final StringProperty formadepago = new SimpleStringProperty();

    public String getFormadepago() {
        return formadepago.get();
    }

    public void setFormadepago(String value) {
        formadepago.set(value);
    }

    public StringProperty formadepagoProperty() {
        return formadepago;
    }

    private List<DetalleVenta> detalleventa = new ArrayList<>();

    public int getNumerofactura() {
        return numerofactura.get();
    }

    public void setNumerofactura(int value) {
        numerofactura.set(value);
    }

    public IntegerProperty numerofacturaProperty() {
        return numerofactura;
    }

    public LocalDateTime getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDateTime value) {
        fecha.set(value);
    }

    public ObjectProperty fechaProperty() {
        return fecha;
    }

    private int getIdventa() {
        return idventa.get();
    }

    private void setIdventa(int value) {
        idventa.set(value);
    }

    private IntegerProperty idventaProperty() {
        return idventa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleVenta> getDetalleventa() {
        return detalleventa;
    }

    public void setDetalleventa(List<DetalleVenta> detalleventa) {
        this.detalleventa = detalleventa;
    }
}