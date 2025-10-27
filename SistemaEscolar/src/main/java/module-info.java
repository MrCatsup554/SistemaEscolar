module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jcraft.jsch;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;

    opens org.example to javafx.fxml, javafx.graphics;
}