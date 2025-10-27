package org.example;

import com.jcraft.jsch.JSchException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

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
        String tablaPersonas = basePersonas.selectPersona();
        textArPersonas.setText("");
        textArPersonas.setText(tablaPersonas);
    }

    String nombre = textNombre.getText();
    String apellido = textApellido.getText();
    char sexoSel = getSexoSel().charAt(0);
    String fh_nac = getFechaNac();
    int rol = getRolSel();

    @FXML
    void registrarPersona(ActionEvent event) {

        //Ingresar personas en la base de datos
            System.out.println(nombre + " " + apellido + " " + sexoSel + " " + fh_nac + " " + rol);
            //basePersonas.insertPersonas(nombre, apellido, sexoSel, fh_nac, rol);

        //Mostrar personas actualizada en la tabla
        textArPersonas.setText("");
        String tablaPersonas = basePersonas.selectPersona();
        textArPersonas.setText("");
        textArPersonas.setText(tablaPersonas);

        //Borro los datos preparando para ingresar nuevos
        borrarDatos();
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
            return null;
        }
    }
    private String getFechaNac() {

        LocalDate fechaNacimiento = dateNac.getValue();
        if (fechaNacimiento != null) {
            String fechaGuardar = fechaNacimiento.toString();
            return fechaGuardar;
        } else {
            System.out.println("⚠️ Advertencia: No se ha seleccionado una fecha.");
            return null;
        }
    }

    private void borrarDatos(){
        textNombre.setText("");
        textApellido.setText("");
        dateNac.setValue(null);
        selecRol.getSelectionModel().clearSelection();
        Toggle toggleSeleccionado = sexo.getSelectedToggle();
        if (toggleSeleccionado != null) {
            toggleSeleccionado.setSelected(false);
        }

        nombre = null;
        apellido = null;
        sexoSel = ' ';
        fh_nac = null;
        rol = 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Crea una lista observable con las opciones
        selecRol.getSelectionModel().clearSelection();
        ObservableList<String> roles = FXCollections.observableArrayList(
                "-- Selecciona un rol --",
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
