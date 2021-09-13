package com.systemnecs;

import com.systemnecs.dao.ComercioDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainApp extends Application {

    public static void main(String[] args) {
        try {
            com.systemnecs.model.Comercio.getInstance(new ComercioDAO().getComercio());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        HBox hBox = fxmlLoader.load(getClass().getResource("/fxml/Login.fxml"));
        primaryStage.setScene(new Scene(hBox));
        primaryStage.show();
    }
}
