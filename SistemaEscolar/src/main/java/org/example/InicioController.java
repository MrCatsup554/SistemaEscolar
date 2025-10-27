package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class InicioController {

    // --- Variables FXML ---
    @FXML private Button btnInicio;
    @FXML private Button btnPersonas;
    @FXML private Button btnAsistencia;
    @FXML private Button btnMateria;
    @FXML private Button btnInscripciones;

    // --- Métodos de Navegación (con los nombres de tus archivos) ---

    @FXML
    void irAPersonas(ActionEvent event) throws IOException {
        // 1. Nombre de archivo corregido
        cambiarVista(event, "PersonasVista.fxml"); 
    }

    @FXML
    void irAAsistencia(ActionEvent event) throws IOException {
        // 1. Nombre de archivo corregido
        cambiarVista(event, "AsistenciasVista.fxml");
    }

    @FXML
    void irAMateria(ActionEvent event) throws IOException {
        // 1. Nombre de archivo corregido
        cambiarVista(event, "MateriasVista.fxml");
    }

    @FXML
    void irAInscripciones(ActionEvent event) throws IOException {
        // 1. Nombre de archivo corregido
        cambiarVista(event, "InscripcionesVista.fxml");
    }

    // --- Lógica de Ayuda (con el "/" agregado) ---

    private void cambiarVista(ActionEvent event, String fxmlFileName) throws IOException {
        
        // 2. Se agrega "/" para buscar en la RAÍZ de 'resources'
        String fxmlPath = "/" + fxmlFileName; 

        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));

        if (root == null) {
            // Esta línea te ayudará si el nombre del archivo sigue mal
            throw new IOException("No se pudo encontrar el archivo FXML en: " + fxmlPath);
        }
        
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}