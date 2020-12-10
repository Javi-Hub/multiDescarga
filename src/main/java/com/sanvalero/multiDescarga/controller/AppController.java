package com.sanvalero.multiDescarga.controller;

import com.sanvalero.multiDescarga.util.AlertUtils;
import com.sanvalero.multiDescarga.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por @author: Javier
 * el 23/11/2020
 */
public class AppController {

    public TextField tfURL, tfNumDownloads;
    public VBox vbPanel, vbPanelDownloader;
    public Button btSetRoute;
    public Label lbRoute;

    private List<DownloadController> downloads;
    private static final Logger LOGGER = LogManager.getLogger(AppController.class);
    private int contador = 0;

    public AppController(){
        downloads = new ArrayList<>();
    }

    @FXML
    public void launchDownload(ActionEvent event){

            if (tfURL.getText().equals("")){
                AlertUtils.mostrarError("Debe ingresar una url de descarga");
                return;
            }

            String urlText = tfURL.getText();
            tfURL.clear();
            tfURL.requestFocus();
            launchDownload(urlText);
    }

    public void launchDownload(String url){
        try {
            LOGGER.trace("Cargar descarga (" + contador + ")");
            contador++;
            if (!tfNumDownloads.getText().equals("") && contador > Integer.parseInt(tfNumDownloads.getText())) {
                LOGGER.trace("Limite descargas superado --> (" + tfNumDownloads.getText() + ")");
                AlertUtils.mostrarError("Límite de descargas superado. Descarga - " + contador + " cancelada.");
                return;
            }
            // Insertar en la ventana padre --> "multiDownload", las descargas que vaya
            //  añadiendo --> "download"
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI( "downloader.fxml"));
            DownloadController downloadController = new DownloadController(url, lbRoute.getText(), "Download - " + contador);
            loader.setController(downloadController);
            VBox downloader = loader.load();
            vbPanelDownloader.getChildren().add(downloader);
            downloads.add(downloadController);

        } catch (IOException ioe) {
            ioe.printStackTrace();
            LOGGER.error("Error Entrada/Salida --> " + ioe.fillInStackTrace());
        }
    }

    @FXML
    public void stopAllDownloads(ActionEvent event){
        LOGGER.trace("Parar todas las descargas --> (" + contador + ")");
        for (DownloadController downloadController : downloads){
                downloadController.stop();
        }
    }

    @FXML
    public void clearDownloads(ActionEvent event){
        LOGGER.trace("Borrar todas las descargas --> (" + contador + ")");
        vbPanelDownloader.getChildren().clear();
        contador = 0;
    }

    @FXML
    public void setRoute(ActionEvent event){
        LOGGER.trace("Selección ruta todas las descargas (" + lbRoute.getText() + ")");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File fileDir = directoryChooser.showDialog(null);
        String dir = fileDir.getAbsolutePath();
        lbRoute.setText(dir);
    }

    @FXML
    public void readDLC(){
        LOGGER.trace("Subir archivo urls");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        try {
            List<String> urls = Files.readAllLines(file.toPath());
            urls.forEach(this::launchDownload);
        } catch (IOException ioe) {
            AlertUtils.mostrarError("No se ha podido cargar el archivo correctamente");
        }
    }


}
