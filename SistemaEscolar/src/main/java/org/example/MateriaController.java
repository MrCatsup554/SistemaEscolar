package org.example;

import com.jcraft.jsch.JSchException; // Importar
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Importar
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*; // Importar
import java.io.IOException;
import java.net.URL; // Importar
import java.util.ResourceBundle; // Importar

public class MateriaController implements Initializable {

    // --- Conexión a la Base de Datos ---
    private ConexionBD baseMaterias;

    // --- Variables FXML ---

    // Botones del menú de navegación
    @FXML private Button btnInicio;
    @FXML private Button btnPersonas;
    @FXML private Button btnAsistencia;
    @FXML private Button btnMateria;
    @FXML private Button btnInscripciones;

    // Campos de entrada
    @FXML private TextField textDescripcion;
    @FXML private TextField textSemestre;
    @FXML private TextField textCreditos;

    // Botones de acción
    @FXML private Button btnVerMaterias;
    @FXML private Button btnRegMateria;

    // Área de texto para resultados
    @FXML private TextArea textArMaterias;

    /**
     * Constructor: Inicializa la conexión a la BD.
     */
    public MateriaController() {
        try {
            this.baseMaterias = new ConexionBD();
        } catch (JSchException e) {
            mostrarError("Error de Conexión", "No se pudo conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Se llama después de que se cargan los elementos FXML.
     * Carga las materias existentes al iniciar.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar datos al iniciar la vista
        verMaterias(null);
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
        cambiarVista(event, "AsistenciasVista.fxml");
    }

    @FXML
    void irAMateria(ActionEvent event) throws IOException {
        System.out.println("Ya estás en Materia.");
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
    void verMaterias(ActionEvent event) {
        if (baseMaterias == null) return; // No hacer nada si la conexión falló

        try {
            String tablaMaterias = baseMaterias.selectMateria();
            textArMaterias.setText(tablaMaterias);
        } catch (Exception e) {
            mostrarError("Error de Consulta", "No se pudieron cargar las materias: " + e.getMessage());
        }
    }

    @FXML
    void registrarMateria(ActionEvent event) {
        if (baseMaterias == null) return;

        // --- Obtener Entradas ---
        String descripcion = textDescripcion.getText();
        String semestreStr = textSemestre.getText();
        String creditosStr = textCreditos.getText();

        int semestre, creditos;

        // --- Validación de Entradas ---
        if (descripcion.isBlank() || semestreStr.isBlank() || creditosStr.isBlank()) {
            mostrarError("Campos Vacíos", "Todos los campos (Descripción, Semestre, Créditos) son obligatorios.");
            return;
        }

        try {
            semestre = Integer.parseInt(semestreStr);
            creditos = Integer.parseInt(creditosStr);
        } catch (NumberFormatException e) {
            mostrarError("Dato Inválido", "Semestre y Créditos deben ser números enteros.");
            return;
        }
        
        // --- Inserción en la BD ---
        try {
            baseMaterias.insertMateria(descripcion, semestre, creditos);

            // Limpiar campos y refrescar la tabla
            borrarDatos();
            verMaterias(null); // Refresca el TextArea

        } catch (Exception e) {
            mostrarError("Error de Registro", "No se pudo registrar la materia: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // --- Métodos de Ayuda (Helpers) ---

    /**
     * Limpia los campos de entrada del formulario.
     */
    private void borrarDatos() {
        textDescripcion.clear();
        textSemestre.clear();
        textCreditos.clear();
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