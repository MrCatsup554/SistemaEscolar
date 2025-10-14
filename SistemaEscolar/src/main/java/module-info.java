module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example to javafx.fxml, javafx.graphics;
}