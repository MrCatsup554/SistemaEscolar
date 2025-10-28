package org.example;

import com.jcraft.jsch.JSchException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PersonasController implements Initializable {


    public PersonasController() throws JSchException {
    }

    ConexionBD basePersonas = new ConexionBD();

    @FXML
    private Button btnRegPersonas;

    @FXML
    private Button btnVerPersonas;

    @FXML
    private DatePicker dateNac;

    @FXML
    private RadioButton radH;

    @FXML
    private RadioButton radM;

    @FXML
    private RadioButton radO;

    @FXML
    private ComboBox<String> selecRol;

    @FXML
    private ToggleGroup sexo;

    @FXML
    private TextField textApellido;

    @FXML
    private TextField textNombre;

    @FXML
    private TableView<Personas> tblPersona;

    @FXML
    private TableColumn<Personas, String> colApellido;

    @FXML
    private TableColumn<Personas, String> colFh;

    @FXML
    private TableColumn<Personas, Integer> colId;

    @FXML
    private TableColumn<Personas, String> colNombre;

    @FXML
    private TableColumn<Personas, String> colRol;

    @FXML
    private TableColumn<Personas, String> colSexo;


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
        System.out.println("Ya estás en Personas.");
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

    private void cargarTablaPersonas() {
        tblPersona.setItems(basePersonas.selectPersonas());
    }

    @FXML
    void verPersonas(ActionEvent event) {
        cargarTablaPersonas();
    }

    @FXML
    void registrarPersona(ActionEvent event) {
        String nombre = textNombre.getText();
        String apellido = textApellido.getText();
        char sexoSel = getSexoSel().charAt(0);
        String fh_nac = getFechaNac();
        int rol = getRolSel();

        if(textNombre.getText().isBlank() || textApellido.getText().isBlank()){
            JOptionPane.showMessageDialog(
                    null,
                    "Ingresa nombres y apellidos válidos.",
                    "Error de Nombres y Apellidos",
                    JOptionPane.ERROR_MESSAGE);
        }
        if (fh_nac == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Debe seleccionar una Fecha de Nacimiento.",
                    "Error de fecha",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (rol == 0){
            JOptionPane.showMessageDialog(
                    null,
                    "Debe seleccionar un rol.",
                    "Error de rol",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            System.out.println(nombre + " " + apellido + " " + sexoSel + " " + fh_nac + " " + rol);
            basePersonas.insertPersonas(nombre, apellido, sexoSel, fh_nac, rol);

            borrarDatos();

            nombre = null;
            apellido = null;
            sexoSel = ' ';
            fh_nac = null;
            rol = 0;

            cargarTablaPersonas();
        }
    }

    private int getRolSel(){
        String rolSeleccionado = selecRol.getSelectionModel().getSelectedItem();

        switch (rolSeleccionado){
            case "Estudiante":
                return 1;
            case "Profesor":
                return 2;
            case "Directivo":
                return 3;
            case "Administrativo":
                return 4;
        }
        return 0;
    }
    private String getSexoSel() {
        Toggle toggleSeleccionado = sexo.getSelectedToggle();

        if (toggleSeleccionado != null) {
            RadioButton selectedRadioButton = (RadioButton) toggleSeleccionado;

            return selectedRadioButton.getId();
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione un sexo.",
                    "Error de Sexo",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    private String getFechaNac() {

        LocalDate fechaNacimiento = dateNac.getValue();
        if (fechaNacimiento != null) {
            String fechaGuardar = fechaNacimiento.toString();
            return fechaGuardar;
        } else {
            return null;
        }
    }

    private void borrarDatos(){
        textNombre.setText("");
        textApellido.setText("");
        dateNac.setValue(null);
        selecRol.getSelectionModel().selectFirst();
        Toggle toggleSeleccionado = sexo.getSelectedToggle();
        if (toggleSeleccionado != null) {
            toggleSeleccionado.setSelected(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colFh.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        selecRol.getSelectionModel().clearSelection();
        ObservableList<String> roles = FXCollections.observableArrayList(
                "-- Seleccionar rol --",
                "Estudiante",
                "Profesor",
                "Directivo",
                "Administrativo"
        );
        selecRol.setItems(roles);
        selecRol.getSelectionModel().selectFirst();
    }
}
