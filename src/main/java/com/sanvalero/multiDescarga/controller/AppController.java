package com.sanvalero.multiDescarga.controller;

import com.sanvalero.multiDescarga.util.AlertUtils;
import com.sanvalero.multiDescarga.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Creado por @author: Javier
 * el 23/11/2020
 */
public class AppController implements Initializable {

    public TextField tfURL, tfNumDownloads;
    public VBox vbPanel, vbPanelDownloader;
    public Button btStopAll, btClear;
    public Label lbRoute;
    public TextArea taLog;
    public DownloadController dController;

    private List<DownloadController> downloads;
    private static final Logger LOGGER = LogManager.getLogger(AppController.class);
    public int contador = 0;

    public AppController(){
        downloads = new ArrayList<>();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btClear.setDisable(true);
        btStopAll.setDisable(true);
    }

    @FXML
    public void launchDownload(ActionEvent event){
            LOGGER.trace("Ventana hijo download desplegada");
            if (tfURL.getText().equals("")){
                AlertUtils.mostrarError("Debe ingresar una url de descarga");
                return;
            }

            String urlText = tfURL.getText();
            tfURL.clear();
            tfURL.requestFocus();
            btClear.setDisable(false);
            btStopAll.setDisable(false);
            launchDownload(urlText);
    }

    @FXML
    public void launchDownload(String url){
        LOGGER.trace("Layout Download creado");
        try {
            boolean route = false;
            contador++;
            if (lbRoute.getText().equals("")){
                route = true;
            }
            LOGGER.trace("Cargar descarga (" + contador + ")");
            if (!tfNumDownloads.getText().equals("") && contador > Integer.parseInt(tfNumDownloads.getText())) {
                LOGGER.trace("Limite descargas superado --> (" + tfNumDownloads.getText() + ")");
                AlertUtils.mostrarError("Límite de descargas superado. Descarga - " + contador + " cancelada.");
                return;
            }
            // Insertar en la ventana padre --> "multiDownload", las descargas que vaya
            //  añadiendo --> "download"
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUI( "downloader.fxml"));
            DownloadController downloadController = new DownloadController(url, lbRoute.getText(), route, "Download - " + contador);
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
        btClear.setDisable(true);
        btStopAll.setDisable(true);
    }

    @FXML
    public void setRoute(ActionEvent event){
        LOGGER.trace("Selección ruta todas las descargas (" + lbRoute.getText() + ")");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File fileDir = directoryChooser.showDialog(null);
        if (fileDir != null){
            String dir = fileDir.getAbsolutePath();
            lbRoute.setText(dir);
        }

    }

    @FXML
    public void readDLC(){
        LOGGER.trace("Subir archivo con urls de descarga");
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        try {
            List<String> urls = Files.readAllLines(file.toPath());
            urls.forEach(this::launchDownload);
        } catch (IOException ioe) {
            LOGGER.error("Error Entrada/Salida leer archivo TXT --> " + ioe.fillInStackTrace());
            AlertUtils.mostrarError("No se ha podido cargar el archivo correctamente");
        }
        btStopAll.setDisable(false);
        btClear.setDisable(false);
    }

    @FXML
    public void readLogger(){
        LOGGER.trace("Mostrar archivo .log en TextArea");
        taLog.setText("");
        readLog();
    }

    @FXML
    public void clearLogger(){
        LOGGER.trace("Borrar TextArea ");
        taLog.setText("");
    }


    public void readLog() {
        try {
            File file = new File("multiDescarga.log");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while (line != null) {
                taLog.appendText(line + "\n");
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
