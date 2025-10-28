package org.example;

import com.jcraft.jsch.JSchException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    private TableColumn<Asistencias, String> colFh;

    @FXML
    private TableColumn<Asistencias, Integer> colId;

    @FXML
    private TableColumn<Asistencias, Integer> colIns;

    @FXML
    private TableView<Asistencias> tblAsistencias;

    public AsistenciaController() {
        try {
            this.baseAsistencias = new ConexionBD();
        } catch (Exception e) {
            mostrarError("Error de Conexión", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTablaAsistencias() {
        tblAsistencias.setItems(baseAsistencias.selectAsistencias());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAsistencia"));
        colIns.setCellValueFactory(new PropertyValueFactory<>("idInscripcion"));
        colFh.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

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

    @FXML
    void verAsistencias(ActionEvent event) {
        cargarTablaAsistencias();
    }

    @FXML
    void registrarAsistencia(ActionEvent event) {
        if (baseAsistencias == null) return;

        String idInscripcionStr = textIdInscripcion.getText();
        String fecha = getFechaAsistencia();
        int idInscripcion;

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

        try {
            baseAsistencias.insertAsistencia(idInscripcion, fecha);

            borrarDatos();
            cargarTablaAsistencias();

        } catch (Exception e) {
            mostrarError("Error de Registro", "No se pudo registrar la asistencia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getFechaAsistencia() {
        LocalDate fechaAsistencia = dateAsistencia.getValue();
        if (fechaAsistencia != null) {
            return fechaAsistencia.toString();
        } else {
            return null;
        }
    }

    private void borrarDatos() {
        textIdInscripcion.clear();
        dateAsistencia.setValue(null);
    }

    private void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.ERROR_MESSAGE);
    }
}