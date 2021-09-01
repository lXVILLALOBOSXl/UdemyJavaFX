package com.systemnecs.dao;

import com.systemnecs.model.Usuario;
import com.systemnecs.util.ConexionDB;
import com.systemnecs.util.Sesion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private ConexionDB conexionDB;

    public UsuarioDAO(ConexionDB conexionDB){ this.conexionDB = conexionDB; }

    public Usuario getUsuario(String username, String password) throws SQLException {
        Usuario usuario = null;

        String sql = "SELECT * FROM usuario WHERE username = '" + username + "' AND password = '" + password + "'";
        ResultSet resultSet = conexionDB.CONSULTAR(sql);
        if(resultSet.next()){
            usuario = new Usuario();
            usuario.setIdUsuario(resultSet.getInt("idusuario"));
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setNombre(resultSet.getString("nombre"));

            Sesion.getUsuario(usuario);
        }
        return  usuario;
    }
}
