package com.systemnecs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.systemnecs.dao.UsuarioDAO;
import com.systemnecs.model.Usuario;
import com.systemnecs.util.ConexionDB;
import com.systemnecs.util.Sesion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private HBox root;

    @FXML
    private JFXButton btnWhatsapp;

    @FXML
    private JFXButton btnPagina;

    @FXML
    private JFXTextField cjUsername;

    @FXML
    private JFXPasswordField cjPassword;

    @FXML
    private JFXButton btnIniciar;

    private ConexionDB conexionDB;
    private UsuarioDAO usuarioDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void login(ActionEvent event) throws SQLException, IOException {

        String username = cjUsername.getText().trim();
        String password = cjPassword.getText().trim();
        conexionDB = new ConexionDB();
        conexionDB.conectar();
        usuarioDAO = new UsuarioDAO(conexionDB);
        Usuario usuario = usuarioDAO.getUsuario(username, password);
        if (usuario == null){
            //Anunciar con ventana emergente que el usuario que se ingreso es incorrecto
            new animatefx.animation.Tada(cjUsername).play();
            new animatefx.animation.Tada(cjPassword).play();

            org.controlsfx.control.Notifications
                    .create()
                    .title("Mensaje")
                    .text("El usuario o contraseña no coinciden")
                    .position(Pos.CENTER).showInformation();
            return;
        }


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
        BorderPane borderPane = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(borderPane));
        stage.setTitle(Sesion.getSesion(null).getNombre());
        stage.setMaximized(true);
        com.systemnecs.util.Metodos.closeEffect(root);
        stage.show();

    }

}
