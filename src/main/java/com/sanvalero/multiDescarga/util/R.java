package com.sanvalero.multiDescarga.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * Creado por @author: Javier
 * el 22/11/2020
 */
public class R {

    // Obtener los archivos de imagnes de la carpet "images"
    public static InputStream getImage(String name){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream("images" + File.separator + name);
    }

    // Obtener los archivos fxml de la carpeta "ui"
    public static URL getUI (String name){
        return Thread.currentThread().getContextClassLoader().getResource("ui" + File.separator + name);
    }

}
