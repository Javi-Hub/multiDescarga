package com.sanvalero.multiDescarga.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Creado por @author: Javier
 * el 08/12/2020
 */
public class AlertUtils {

    public static void mostrarError(String mensaje){
        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
        alertWarning.setTitle("ALERTA");
        alertWarning.setHeaderText("ATENCIÓN");
        alertWarning.setContentText(mensaje);
        alertWarning.show();
    }

    public static Optional<ButtonType> mostrarConfirmacion(String mensaje){
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Confirmar");
        alertConfirm.setHeaderText(mensaje);
        alertConfirm.setContentText("Confirmar");
        return alertConfirm.showAndWait();
    }
}
