package org.example;

import com.jcraft.jsch.JSchException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Importar
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*; // Importar
import java.io.IOException;
import java.net.URL; // Importar
import java.util.ResourceBundle; // Importar

public class InscripcionController implements Initializable {

    // --- Conexión a la Base de Datos ---
    private ConexionBD baseInscripciones;

    // --- Variables FXML ---
    @FXML private TextField textIdMateria;
    @FXML private TextField textIdEstudiante;
    @FXML private TextField textCalificacion;
    @FXML private Button btnVerInscripciones;
    @FXML private Button btnRegInscripcion;
    @FXML private TextArea textArInscripciones;

    @FXML
    private TableColumn<Inscripciones, Integer> colCal;

    @FXML
    private TableColumn<Inscripciones, Integer> colEst;

    @FXML
    private TableColumn<Inscripciones, Integer> colId;

    @FXML
    private TableColumn<Inscripciones, Integer> colMat;

    @FXML
    private TableView<Inscripciones> tblInsc;

    public InscripcionController() {
        try {
            this.baseInscripciones = new ConexionBD();
        } catch (JSchException e) {
            mostrarError("Error de Conexión", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarTablaInscripciones() {
        tblInsc.setItems(baseInscripciones.selectInscripciones());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idInscripcion"));
        colMat.setCellValueFactory(new PropertyValueFactory<>("idMateria"));
        colEst.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        colCal.setCellValueFactory(new PropertyValueFactory<>("calificacion"));
    }

    @FXML
    void verInscripciones(ActionEvent event) {
        cargarTablaInscripciones();
    }

    @FXML
    void registrarInscripcion(ActionEvent event) {
        if (baseInscripciones == null) return;

        // --- Obtener Entradas ---
        String idMateriaStr = textIdMateria.getText();
        String idEstudianteStr = textIdEstudiante.getText();
        String calificacionStr = textCalificacion.getText();

        int idMateria, idEstudiante, calificacion;

        // --- Validación de Entradas ---
        if (idMateriaStr.isBlank() || idEstudianteStr.isBlank()) {
            mostrarError("Campos Vacíos", "Debe ingresar un ID de Materia y un ID de Estudiante.");
            return;
        }

        try {
            idMateria = Integer.parseInt(idMateriaStr);
            idEstudiante = Integer.parseInt(idEstudianteStr);
        } catch (NumberFormatException e) {
            mostrarError("Dato Inválido", "Los IDs de Materia y Estudiante deben ser números enteros.");
            return;
        }

        // Validación de Calificación (opcional, default 0)
        if (calificacionStr.isBlank()) {
            calificacion = 0;
        } else {
            try {
                // La BD espera un INT, no un double/float
                calificacion = Integer.parseInt(calificacionStr);
            } catch (NumberFormatException e) {
                mostrarError("Dato Inválido", "La calificación debe ser un número entero (ej: 8, 9, 10).");
                return;
            }
        }
        try {
            baseInscripciones.insertInscripcion(idMateria, idEstudiante, calificacion);

            // Limpiar campos y refrescar la tabla
            borrarDatos();
            cargarTablaInscripciones();

        } catch (Exception e) {
            // Captura errores comunes como Clave Foránea (FOREIGN KEY)
            if (e.getMessage().contains("a foreign key constraint fails")) {
                 mostrarError("Error de Registro", "No se pudo registrar: El ID de Materia o el ID de Estudiante no existen.");
            } else {
                 mostrarError("Error de Registro", "No se pudo registrar la inscripción: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
    
    // --- Métodos de Ayuda (Helpers) ---

    /**
     * Limpia los campos de entrada del formulario.
     */
    private void borrarDatos() {
        textIdMateria.clear();
        textIdEstudiante.clear();
        textCalificacion.clear();
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
    

    // ===================================================================
    // --- INICIO: Bloque de Navegación (Ya lo tenías) ---
    // ===================================================================

    @FXML private Button btnInicio;
    @FXML private Button btnPersonas;
    @FXML private Button btnAsistencia;
    @FXML private Button btnMateria;
    @FXML private Button btnInscripciones;

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
        cambiarVista(event, "AsistenciasVista.fxml");
    }

    @FXML
    void irAMateria(ActionEvent event) throws IOException {
        cambiarVista(event, "MateriasVista.fxml");
    }

    @FXML
    void irAInscripciones(ActionEvent event) throws IOException {
        System.out.println("Ya estás en Inscripciones.");
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

    // ===================================================================
    // --- FIN: Bloque de Navegación ---
    // ===================================================================
}