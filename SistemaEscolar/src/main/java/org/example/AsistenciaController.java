package org.example;

import com.jcraft.jsch.JSchException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AsistenciaController implements Initializable {

    // --- Conexión a la Base de Datos ---
    private ConexionBD baseAsistencias;

    // --- Variables FXML ---

    // Botones del menú de navegación
    @FXML private Button btnInicio;
    @FXML private Button btnPersonas;
    @FXML private Button btnAsistencia;
    @FXML private Button btnMateria;
    @FXML private Button btnInscripciones;

    // Campos de entrada
    @FXML private TextField textIdInscripcion;
    @FXML private DatePicker dateAsistencia;

    // Botones de acción
    @FXML private Button btnVerAsistencias;
    @FXML private Button btnRegAsistencia;

    // Área de texto para resultados
    @FXML private TextArea textArAsistencias;

    /**
     * Constructor: Inicializa la conexión a la BD.
     */
    public AsistenciaController() {
        try {
            this.baseAsistencias = new ConexionBD();
        } catch (JSchException e) {
            mostrarError("Error de Conexión", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Se llama después de que se cargan los elementos FXML.
     * Carga las asistencias existentes al iniciar.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    // --- Métodos de Navegación ---
    
    @FXML
    void irAInicio(ActionEvent event) throws IOException {
        cambiarVista(event, "InicioVista.fxml");
    }

    @FXML
    void irAPersonas(ActionEvent event) throws IOException {
        cambiarVista(event, "PersonasVista.fxml");
    }

    @FXML
    void irAAsistencia(ActionEvent event) throws IOException {
        System.out.println("Ya estás en Asistencia.");
    }

    @FXML
    void irAMateria(ActionEvent event) throws IOException {
        cambiarVista(event, "MateriasVista.fxml");
    }

    @FXML
    void irAInscripciones(ActionEvent event) throws IOException {
        cambiarVista(event, "InscripcionesVista.fxml");
    }

    private void cambiarVista(ActionEvent event, String fxmlFileName) throws IOException {
        String fxmlPath = "/" + fxmlFileName;
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        if (root == null) {
            throw new IOException("No se pudo encontrar el archivo FXML en: " + fxmlPath);
        }
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    // --- Métodos de Acción (onAction) ---

    @FXML
    void verAsistencias(ActionEvent event) {
        if (baseAsistencias == null) return; // No hacer nada si la conexión falló

        try {
            String tablaAsistencias = baseAsistencias.selectAsistencias();
            textArAsistencias.setText(tablaAsistencias);
        } catch (Exception e) {
            mostrarError("Error de Consulta", "No se pudieron cargar las asistencias: " + e.getMessage());
        }
    }

    @FXML
    void registrarAsistencia(ActionEvent event) {
        if (baseAsistencias == null) return; // No hacer nada si la conexión falló

        String idInscripcionStr = textIdInscripcion.getText();
        String fecha = getFechaAsistencia();
        int idInscripcion;

        // --- Validación de Entradas ---
        if (idInscripcionStr.isBlank()) {
            mostrarError("Campo Vacío", "Debe ingresar un ID de Inscripción.");
            return;
        }

        try {
            idInscripcion = Integer.parseInt(idInscripcionStr);
        } catch (NumberFormatException e) {
            mostrarError("Dato Inválido", "El ID de Inscripción debe ser un número entero.");
            return;
        }

        if (fecha == null) {
            mostrarError("Campo Vacío", "Debe seleccionar una fecha.");
            return;
        }

        // --- Inserción en la BD ---
        try {
            baseAsistencias.insertAsistencia(idInscripcion, fecha);
            
            // Limpiar campos y refrescar la tabla
            borrarDatos();
            verAsistencias(null); // Refresca el TextArea

        } catch (Exception e) {
            mostrarError("Error de Registro", "No se pudo registrar la asistencia: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- Métodos de Ayuda (Helpers) ---

    /**
     * Obtiene la fecha del DatePicker en formato String YYYY-MM-DD.
     * @return String de la fecha, o null si no hay fecha seleccionada.
     */
    private String getFechaAsistencia() {
        LocalDate fechaAsistencia = dateAsistencia.getValue();
        if (fechaAsistencia != null) {
            return fechaAsistencia.toString();
        } else {
            return null;
        }
    }

    /**
     * Limpia los campos de entrada del formulario.
     */
    private void borrarDatos() {
        textIdInscripcion.clear();
        dateAsistencia.setValue(null);
    }
    
    /**
     * Muestra un diálogo de error genérico.
     * @param titulo Título de la ventana de error.
     * @param mensaje Mensaje de error a mostrar.
     */
    private void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.ERROR_MESSAGE);
    }
}