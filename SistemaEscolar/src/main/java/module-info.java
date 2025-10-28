module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jcraft.jsch;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;

    opens org.example to javafx.fxml, javafx.graphics, javafx.base;

    exports org.example;
}