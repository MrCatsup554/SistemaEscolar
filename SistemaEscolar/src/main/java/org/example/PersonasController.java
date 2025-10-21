package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class PersonasController {

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
    private ComboBox<?> selecRol;

    @FXML
    private ToggleGroup sexo;

    @FXML
    private TextField textApellido;

    @FXML
    private TextArea textArPersonas;

    @FXML
    private TextField textNombre;

    @FXML
    void registrarPersona(ActionEvent event) {

    }

    @FXML
    void verPersonas(ActionEvent event) {

    }

}
