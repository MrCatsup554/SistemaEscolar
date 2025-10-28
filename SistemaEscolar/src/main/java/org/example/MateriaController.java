package org.example;

import com.jcraft.jsch.JSchException; // Importar
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

public class MateriaController implements Initializable {

    private ConexionBD baseMaterias;

    @FXML private Button btnInicio;
    @FXML private Button btnPersonas;
    @FXML private Button btnAsistencia;
    @FXML private Button btnMateria;
    @FXML private Button btnInscripciones;

    @FXML private TextField textDescripcion;
    @FXML private TextField textSemestre;
    @FXML private TextField textCreditos;

    @FXML private Button btnVerMaterias;
    @FXML private Button btnRegMateria;

    @FXML
    private TableColumn<Materias, Integer> colCred;

    @FXML
    private TableColumn<Materias, String> colDesc;

    @FXML
    private TableColumn<Materias, Integer> colId;

    @FXML
    private TableColumn<Materias, Integer> colSem;

    @FXML
    private TableView<Materias> tblMaterias;

    public MateriaController() {
        this.baseMaterias = new ConexionBD();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMateria"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colSem.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colCred.setCellValueFactory(new PropertyValueFactory<>("creditos"));
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

    private void cargarTablaMaterias() {
        // La lista que devuelve el método ya es de tipo ObservableList<Materias>
        tblMaterias.setItems(baseMaterias.selectMaterias());
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
    void verMaterias(ActionEvent event) {
        cargarTablaMaterias();
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
            cargarTablaMaterias();

        } catch (Exception e) {
            mostrarError("Error de Registro", "No se pudo registrar la materia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void borrarDatos() {
        textDescripcion.clear();
        textSemestre.clear();
        textCreditos.clear();
    }

    private void mostrarError(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.ERROR_MESSAGE);
    }
}