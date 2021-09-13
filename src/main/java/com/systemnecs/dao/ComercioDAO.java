package com.systemnecs.dao;

import com.systemnecs.model.Comercio;
import com.systemnecs.util.ConexionDB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ComercioDAO {

    private ConexionDB conexionBD = new ConexionDB();

    public ComercioDAO() {
    }

    public Comercio getComercio() throws SQLException {
        Comercio c = new Comercio();
        this.conexionBD.conectar();
        ResultSet rs = this.conexionBD.CONSULTAR("SELECT * FROM comercio;");
        if (rs.next()) {
            c.setIdcomercio(rs.getInt("idcomercio"));
            c.setNombre(rs.getString("nombre"));
            c.setNit(rs.getString("nit"));
            c.setDireccion(rs.getString("direccion"));
            c.setTelefono(rs.getString("telefono"));
            c.setIva(rs.getInt("iva"));
        }
        this.conexionBD.CERRAR();
        return c;
    }
}
