package com.sanvalero.multiDescarga.controller;

import com.sanvalero.multiDescarga.util.R;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Creado por @author: Javier
 * el 11/12/2020
 */
public class SplashScreenController implements Initializable {

    public VBox vbSplash;
    private static final Logger LOGGER = LogManager.getLogger(SplashScreenController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LOGGER.trace("Mostranto SplashScreen");
        FadeTransition transition = new FadeTransition(Duration.millis(3000), vbSplash);
        transition.setFromValue(1.0);
        transition.setToValue(1.0);
        transition.play();

        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage splash = (Stage) vbSplash.getScene().getWindow();
                splash.hide();
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(R.getUI("multiDownload.fxml"));
                loader.setController(new AppController());
                ScrollPane vBox = null;
                try {
                    vBox = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(vBox);
                stage.setScene(scene);
                stage.show();
            }
        });
    }
}
