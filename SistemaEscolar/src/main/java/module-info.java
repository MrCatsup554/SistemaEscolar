module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jcraft.jsch;
    requires java.sql;

    opens org.example to javafx.fxml, javafx.graphics;
}