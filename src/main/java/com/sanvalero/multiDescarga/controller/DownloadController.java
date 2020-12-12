package com.sanvalero.multiDescarga.controller;

import com.sanvalero.multiDescarga.domain.DownloadTask;
import com.sanvalero.multiDescarga.util.AlertUtils;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Creado por @author: Javier
 * el 23/11/2020
 */
public class DownloadController implements Initializable {

    public TextField tfNameURL, tfNameFile;
    public Button btStart, btStop, btCancel;
    public ProgressBar pbProgress;
    public Label lbStatus, lbDownload, lbStatusFile, lbStatusRoute;
    public VBox vbPanel;
    public CheckBox chSelectDir;
    public AppController app;

    private String urlText;
    private String directorio;
    private DownloadTask downloadTask;
    private String name;
    private boolean route;
    private static final Logger LOGGER = LogManager.getLogger(DownloadController.class);

    public DownloadController(String urlText, String directorio, boolean route, String name){
        LOGGER.trace("Descarga (" + urlText + ") creada");
        this.urlText = urlText;
        this.directorio = directorio;
        this.name = name;
        this.route = route;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Mostrar en el TextField de downloadTask la url pasada por parametro
        tfNameURL.setText(this.urlText);
        lbDownload.setText(this.name);
        lbStatusFile.setText("Introduce el nombre del archivo");
        if (route) lbStatusRoute.setText("Introduce la ruta de descarga");
    }

    @FXML
    public void start(ActionEvent event) {
        File file = null;

        try{
            if (!chSelectDir.isSelected() && directorio.equals("")){
                LOGGER.warn("Directorio (" + directorio + ") no introducido");
                AlertUtils.mostrarError("Debe seleccionar la ruta");
            } else if (tfNameFile.getText().equals("")){
                LOGGER.warn("Nombre archivo no introducido");
                AlertUtils.mostrarError("Debe asignar un nombre al archivo");
            } else if (!chSelectDir.isSelected()){
                LOGGER.trace("Directorio (" + directorio + ") introducido");
                file = new File(directorio + File.separator + tfNameFile.getText());
            } else if (chSelectDir.isSelected()){
                LOGGER.trace("Directorio (" + directorio + ") todas las descargas");
                FileChooser chooserFile = new FileChooser();
                file = chooserFile.showSaveDialog(tfNameURL.getScene().getWindow());
            }

            // Para lanzar la tarea --> DownloadTask
            downloadTask = new DownloadTask(urlText, file);

            // Vinculo la barra del progreso al progreso de la tarea
            pbProgress.progressProperty().unbind();
            pbProgress.progressProperty().bind(downloadTask.progressProperty());

            /*downloadTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("La descarga ha terminado");
                        alert.show();
                    }
                }
            });*/

            // Que tiene que hacer el metodo cuando cambie algo del DownloadTask
            downloadTask.stateProperty().addListener((observableValue, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED){
                    LOGGER.trace("Alerta 'FIN DESCARGA' desplegada");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La descarga ha terminado");
                    alert.show();
                }
            });

            /*downloadTask.messageProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    lbStatus.setText(newValue);
                }
            });*/

            // El mensaje de texto que vaya generando la tarea lo muestro en el lbStatus
            downloadTask.messageProperty().addListener((observableValue, oldValue, newValue) -> lbStatus.setText(newValue));

            new Thread(downloadTask).start();

        } catch (MalformedURLException murle){
            murle.printStackTrace();
            LOGGER.error("URL (" + urlText + ") mal formada -->", murle.fillInStackTrace());
        }

    }

    @FXML
    public void stop(){
        LOGGER.trace("Descarga (" + urlText + ") detenida");
            downloadTask.cancel();
    }


    @FXML
    public void cancel(ActionEvent event){
        vbPanel.getChildren().clear();
    }

}
