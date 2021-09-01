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
        String sql = "";
        if (producto.getIdproducto() == 0) {
            sql = "INSERT INTO producto ( ";
            sql += " codigodebarras, referencia, nombreproducto, stock, stockminimo, descripcion, estado, precio, fechadevencimiento, imagen ";
            sql += ") VALUES (";
            sql += " '"+producto.getCodigodebarras()+"' , '"+producto.getReferencia()+"' , '"+producto.getNombreproducto()+"' , '"+producto.getStock()+"' , ";
            sql += " '"+producto.getStockminimo()+"' , '"+producto.getDescripcion()+"' , '"+producto.getEstado()+"' , '"+producto.getPrecio()+"' , ";
            sql += " '"+producto.getFechadevencimiento()+"' , ? ";
            sql += ")";
        } else {
            sql = "UPDATE producto SET \n"
                    + "	codigodebarras='"+producto.getCodigodebarras()+"', referencia='"+producto.getReferencia()+"', nombreproducto='"+producto.getNombreproducto()+"', \n"
                    + " stock=" + producto.getStock() + ", "+"stockminimo="+producto.getStockminimo()+", descripcion='"+producto.getDescripcion()+"', estado='"+producto.getEstado()+"', precio="+producto.getPrecio()+", \n"
                    + " fechadevencimiento='"+producto.getFechadevencimiento()+"' , imagen=? WHERE idproducto="+producto.getIdproducto()+";";
        }


        PreparedStatement pst = conexionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setBytes(1, producto.getImagen());

        int insert = pst.executeUpdate();
        if (producto.getIdproducto() == 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            insert = rs.getInt(1);
            rs.close();
        }
        return insert;
    }

    public Producto getById(int idproducto) throws SQLException {
        Producto p = null;
        ResultSet rs = this.conexionDB.CONSULTAR("SELECT * FROM producto WHERE idproducto="+idproducto);
        if(rs.next()){
            p = new Producto();
            p.setIdproducto(idproducto);
            p.setCodigodebarras(rs.getString("codigodebarras").trim());
            p.setReferencia(rs.getString("referencia").trim());
            p.setNombreproducto(rs.getString("nombreproducto").trim());
            p.setStock(rs.getDouble("stock"));
            p.setStockminimo(rs.getDouble("stockminimo"));
            p.setDescripcion(rs.getString("descripcion").trim());
            p.setImagen(rs.getBytes("imagen"));
            p.setEstado(rs.getString("estado").trim());
            p.setPrecio(rs.getDouble("precio"));
            p.setFechadevencimiento(rs.getDate("fechadevencimiento").toLocalDate());
        }
        return p;
    }

    public boolean delete(int idproducto) throws SQLException {
        String sql = "DELETE FROM producto WHERE idproducto="+idproducto;
        return conexionDB.GUARDAR(sql);
    }
}
