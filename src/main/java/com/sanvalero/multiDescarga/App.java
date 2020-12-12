package com.sanvalero.multiDescarga;

import com.sanvalero.multiDescarga.controller.AppController;
import com.sanvalero.multiDescarga.controller.SplashScreenController;
import com.sanvalero.multiDescarga.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creado por @author: Javier
 * el 22/11/2020
 */
public class App extends Application {

    @Override
    public void init () throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar la ventada de multiDownload
        /*FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("multiDownload.fxml")); //
        loader.setController(new AppController());
        ScrollPane vBox = loader.load();
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();*/

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("splashScreen.fxml"));
        loader.setController(new SplashScreenController());
        VBox vBox = loader.load();

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}
