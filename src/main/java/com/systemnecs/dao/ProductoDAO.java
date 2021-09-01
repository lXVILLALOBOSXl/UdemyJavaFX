package com.systemnecs.dao;

import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    private ConexionDB conexionDB;

    public ProductoDAO(ConexionDB conexionDB){
        this.conexionDB = conexionDB;
    }

    public List<Producto> getProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        Producto producto = null;
        ResultSet resultSet = conexionDB.CONSULTAR("SELECT * FROM producto");
        while (resultSet.next()){
            producto = new Producto();

            producto.setIdproducto(resultSet.getInt("idproducto"));
            producto.setNombreproducto(resultSet.getString("nombreproducto").trim());
            producto.setCodigodebarras(resultSet.getString("codigodebarras").trim());
            producto.setReferencia(resultSet.getString("referencia").trim());
            producto.setStock(resultSet.getDouble("stock"));
            producto.setStockminimo(resultSet.getDouble("stockminimo"));
            producto.setDescripcion(resultSet.getString("descripcion").trim());
            producto.setEstado(resultSet.getString("estado").trim());
            producto.setPrecio(resultSet.getDouble("precio"));
            producto.setFechadevencimiento(resultSet.getDate("fechadevencimiento").toLocalDate());

            productos.add(producto);
        }
        return productos;
    }

    public int guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto ( ";
        sql += " codigodebarras, referencia, nombreproducto, stock, stockminimo, descripcion, estado, precio, fechadevencimiento, imagen ";
        sql += ") VALUES (";
        sql += " '"+producto.getCodigodebarras()+"' , '"+producto.getReferencia()+"' , '"+producto.getNombreproducto()+"' , '"+producto.getStock()+"' , ";
        sql += " '"+producto.getStockminimo()+"' , '"+producto.getDescripcion()+"' , '"+producto.getEstado()+"' , '"+producto.getPrecio()+"' , ";
        sql += " '"+producto.getFechadevencimiento()+"' , ? ";
        sql += ")";

        PreparedStatement preparedStatement = conexionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setBytes(1, producto.getImagen());

        int insert = preparedStatement.executeUpdate();
        if (producto.getIdproducto() == 0) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            insert = resultSet.getInt(1);
            resultSet.close();
        }
        return insert;
    }
}
