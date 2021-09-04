package com.systemnecs.dao;

import com.systemnecs.model.Cliente;
import com.systemnecs.util.ConexionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private ConexionDB conexionDB;

    public ClienteDAO(ConexionDB conexionDB) {
        this.conexionDB = conexionDB;
    }

    public int guardar(Cliente c) throws SQLException {

        String sql = null;
        if (c.getIdcliente() == 0) {
            sql = "INSERT INTO cliente(\n"
                    + "	nombrecliente, apellidocliente, direccioncliente, telefonocliente, numerodocumento)\n"
                    + "	VALUES ('"+c.getNombrecliente()+"', '"+c.getApellidocliente()+"', '"+c.getDireccioncliente()+"', '"+c.getTelefonocliente()+"', '"+c.getNumerodocumento()+"');";
        } else {
            sql = "UPDATE cliente\n" +
                    "	SET nombrecliente='"+c.getNombrecliente()+"', apellidocliente='"+c.getApellidocliente()+"', direccioncliente='"+c.getDireccioncliente()+"', "
                    + "telefonocliente="+c.getTelefonocliente()+"', numerodocumento='"+c.getNumerodocumento()+"'\n" +
                    "	WHERE idcliente="+c.getIdcliente()+";";
        }
        System.out.println(sql);

        PreparedStatement pst = this.conexionDB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        int n = pst.executeUpdate();
        if (c.getIdcliente() == 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            n = rs.getInt(1);
        }
        return n;
    }

    public List<Cliente> getAll() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        Cliente c = null;
        ResultSet rs = this.conexionDB.CONSULTAR("SELECT idcliente, nombrecliente, apellidocliente FROM cliente;");
        while(rs.next()){
            c = new Cliente();
            c.setIdcliente(rs.getInt("idcliente"));
            c.setNombrecliente(rs.getString("nombrecliente").trim());
            c.setApellidocliente(rs.getString("apellidocliente").trim());

            lista.add(c);
        }
        return lista;
    }
}
