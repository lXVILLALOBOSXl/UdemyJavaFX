package com.systemnecs.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Comercio {

    private final IntegerProperty idcomercio = new SimpleIntegerProperty(0);
    private final StringProperty nombre = new SimpleStringProperty();
    private final StringProperty nit = new SimpleStringProperty();
    private final StringProperty direccion = new SimpleStringProperty();
    private final StringProperty telefono = new SimpleStringProperty();
    private final IntegerProperty iva = new SimpleIntegerProperty(0);

    private static Comercio comercio;

    public static Comercio getInstance(Comercio c){
        if(comercio == null){
            comercio = c;
        }
        return comercio;
    }

    public int getIva() {
        return iva.get();
    }

    public void setIva(int value) {
        iva.set(value);
    }

    public IntegerProperty ivaProperty() {
        return iva;
    }


    public String getTelefono() {
        return telefono.get();
    }

    public void setTelefono(String value) {
        telefono.set(value);
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }


    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String value) {
        direccion.set(value);
    }

    public StringProperty direccionProperty() {
        return direccion;
    }


    public String getNit() {
        return nit.get();
    }

    public void setNit(String value) {
        nit.set(value);
    }

    public StringProperty nitProperty() {
        return nit;
    }


    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String value) {
        nombre.set(value);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }


    public int getIdcomercio() {
        return idcomercio.get();
    }

    public void setIdcomercio(int value) {
        idcomercio.set(value);
    }

    public IntegerProperty idcomercioProperty() {
        return idcomercio;
    }



}
