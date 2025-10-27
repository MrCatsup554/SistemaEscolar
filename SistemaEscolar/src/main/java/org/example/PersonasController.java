package org.example;

import com.jcraft.jsch.JSchException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PersonasController implements Initializable {

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
    private TextArea textArPersonas;

    @FXML
    private TextField textNombre;

    public PersonasController() throws JSchException {
    }

    @FXML
    void verPersonas(ActionEvent event) {
        String tablaPersonas = basePersonas.selectMateria();
        textArPersonas.setText("");
        textArPersonas.setText(tablaPersonas);
    }

    @FXML
    void registrarPersona(ActionEvent event) {
        String nombre = textNombre.getText();
        String apellido = textApellido.getText();
        char sexoSel = getSexoSel().charAt(0);
        String fh_nac = getFechaNac();
        int rol = getRolSel();

        //Ingresar personas en la base de datos
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

            //Borro los datos preparando para ingresar nuevos
            borrarDatos();

            nombre = null;
            apellido = null;
            sexoSel = ' ';
            fh_nac = null;
            rol = 0;

            //Mostrar personas actualizada en la tabla
            textArPersonas.setText("");
            String tablaPersonas = basePersonas.selectPersona();
            textArPersonas.setText("");
            textArPersonas.setText(tablaPersonas);
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
        // Crea una lista observable con las opciones
        selecRol.getSelectionModel().clearSelection();
        ObservableList<String> roles = FXCollections.observableArrayList(
                "-- Seleccionar rol --",
                "Estudiante",
                "Profesor",
                "Directivo",
                "Administrativo"
        );
        selecRol.setItems(roles);

        // Opcional: Seleccionar una opción por defecto
        selecRol.getSelectionModel().selectFirst();
    }
}
