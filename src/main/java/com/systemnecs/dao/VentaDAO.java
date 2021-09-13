package com.systemnecs.dao;

import com.systemnecs.model.DetalleVenta;
import com.systemnecs.model.Venta;
import com.systemnecs.util.ConexionDB;
import com.systemnecs.util.Sesion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentaDAO {

    private ConexionDB conexionDB;

    public VentaDAO(ConexionDB conexionDB) {
        this.conexionDB = conexionDB;
    }

    public int guardar(Venta v) throws SQLException {
        String sql = "INSERT INTO venta(\n"
                + "	idcliente, idusuario, formadepago)\n"
                + "	VALUES ("+v.getCliente().getIdcliente()+" , "+ Sesion.getSesion(null).getIdUsuario()+" , '"+v.getFormadepago()+"');";

        this.conexionDB.getConnection().setAutoCommit(false);

        PreparedStatement pst = this.conexionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        if (pst.executeUpdate() > 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int idventa = rs.getInt(1);
            sql = "INSERT INTO detalleventa( idventa, idproducto, preciodeventa, cantidad) VALUES\n";
            for (DetalleVenta dv : v.getDetalleventa()) {
                sql += "(";
                sql += " "+idventa+", "+dv.getProducto().getIdproducto()+", "+dv.getPrecioventa()+", "+dv.getCantidad()+" ";
                sql += "),";
            }
            sql = sql.substring(0, sql.length()-1);
            pst = this.conexionDB.getConnection().prepareStatement(sql);
            pst.executeUpdate();

            this.conexionDB.getConnection().commit();
            return idventa;
        }

        return 0;
    }
}