package com.systemnecs.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Producto {
    private final IntegerProperty idproducto = new SimpleIntegerProperty(0);
    private final StringProperty codigodebarras = new SimpleStringProperty();
    private final StringProperty referencia = new SimpleStringProperty();
    private final StringProperty nombreproducto = new SimpleStringProperty();
    private final DoubleProperty stock = new SimpleDoubleProperty();
    private final DoubleProperty stockminimo = new SimpleDoubleProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final ObjectProperty<byte[]> imagen = new SimpleObjectProperty<>();
    private final StringProperty estado = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> fechaderegistro = new SimpleObjectProperty<>();
    private final DoubleProperty precio = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> fechadevencimiento = new SimpleObjectProperty<>();

    public int getIdproducto() {
        return idproducto.get();
    }

    public IntegerProperty idproductoProperty() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto.set(idproducto);
    }

    public String getCodigodebarras() {
        return codigodebarras.get();
    }

    public StringProperty codigodebarrasProperty() {
        return codigodebarras;
    }

    public void setCodigodebarras(String codigodebarras) {
        this.codigodebarras.set(codigodebarras);
    }

    public String getReferencia() {
        return referencia.get();
    }

    public StringProperty referenciaProperty() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia.set(referencia);
    }

    public String getNombreproducto() {
        return nombreproducto.get();
    }

    public StringProperty nombreproductoProperty() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto.set(nombreproducto);
    }

    public double getStock() {
        return stock.get();
    }

    public DoubleProperty stockProperty() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock.set(stock);
    }

    public double getStockminimo() {
        return stockminimo.get();
    }

    public DoubleProperty stockminimoProperty() {
        return stockminimo;
    }

    public void setStockminimo(double stockminimo) {
        this.stockminimo.set(stockminimo);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public byte[] getImagen() {
        return imagen.get();
    }

    public ObjectProperty<byte[]> imagenProperty() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen.set(imagen);
    }

    public String getEstado() {
        return estado.get();
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public LocalDateTime getFechaderegistro() {
        return fechaderegistro.get();
    }

    public ObjectProperty<LocalDateTime> fechaderegistroProperty() {
        return fechaderegistro;
    }

    public void setFechaderegistro(LocalDateTime fechaderegistro) {
        this.fechaderegistro.set(fechaderegistro);
    }

    public double getPrecio() {
        return precio.get();
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public LocalDate getFechadevencimiento() {
        return fechadevencimiento.get();
    }

    public ObjectProperty<LocalDate> fechadevencimientoProperty() {
        return fechadevencimiento;
    }

    public void setFechadevencimiento(LocalDate fechadevencimiento) {
        this.fechadevencimiento.set(fechadevencimiento);
    }
}
