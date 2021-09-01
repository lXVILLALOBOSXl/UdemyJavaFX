package com.systemnecs.util;

import java.sql.*;

public class ConexionDB {

    private Connection connection;
    private Statement statement;
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/POS?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "";

    public ConexionDB() { }

    public Connection getConnection() {
        return connection;
    }

    public void conectar(){
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER,JDBC_PASS);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace(System.out);
        }
    }

    public ResultSet CONSULTAR(String sql) throws SQLException {
        ResultSet resultSet = null;
        resultSet = statement.executeQuery(sql);
        System.out.println(sql);
        return resultSet;
    }

    public boolean GUARDAR(String sql) throws SQLException {
        return (statement.executeUpdate(sql) > 0);
    }

    public void CERRAR(){
        try {
            if(connection != null){
                connection.close();
            }
            if (statement != null){
                statement.close();
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace(System.out);
        }
    }
}
