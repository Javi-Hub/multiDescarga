package com.sanvalero.multiDescarga.domain;

import com.sanvalero.multiDescarga.controller.DownloadController;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Creado por @author: Javier
 * el 23/11/2020
 */
public class DownloadTask extends Task<Integer> {

    private URL url;
    private File file;
    private static final Logger LOGGER = LogManager.getLogger(DownloadController.class);

    public DownloadTask(String urlText, File file) throws MalformedURLException {
        this.url = new URL(urlText);
        this.file = file;
    }

    @Override
    protected Integer call() throws Exception {
        LOGGER.trace("Descarga (" + url.toString() + ") iniciada");
        updateMessage("Conectando con el servidor . . .");

        URLConnection urlConnection = url.openConnection();

        // Para capturar el tamaño del fichero
        double fileSize = urlConnection.getContentLength();
        double fileSizeMB = fileSize / Math.pow(1024, 2);

        // Descargar el fichero de la URL
        BufferedInputStream input = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        int totalRead = 0;
        double downloadProgress = 0;
        double downloadProgressMB = 0;

        while ((bytesRead = input.read(dataBuffer, 0, 1024)) != -1) {
            downloadProgress = ((double) totalRead / fileSize);
            downloadProgressMB = ((double) totalRead / fileSizeMB);

            fileOutputStream.write(dataBuffer, 0, bytesRead);
            totalRead += bytesRead;

            // (Como lleva al progreso, hasta el 1 que es el final de la barra de progreso)
            updateProgress(downloadProgress, 1);
            updateMessage((int) (downloadProgress * 100) + " % - " + (int) downloadProgressMB + "MB de " + fileSizeMB + "MB");

            if (isCancelled()){
                LOGGER.trace("Descarga (" + url.toString() + ") cancelada");
                return null;
            }
        }

        // Para parar la barra de estado
        updateProgress(1, 1);
        updateMessage("100 % - " + (int) (downloadProgressMB) + " MB ");
        LOGGER.trace("Descarga (" + url.toString() + ") finalizada");
        return null;
    }
}

// TODO Logger rotar por dia
// TODO Diseño gráfico Aplicación
