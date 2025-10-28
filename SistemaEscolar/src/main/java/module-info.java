module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jcraft.jsch;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base; // Asegúrate de que este 'requires' esté presente (lo estaba implícitamente, pero es buena práctica)

    // LA LÍNEA CLAVE: Añadir javafx.base al permiso 'opens'
    opens org.example to javafx.fxml, javafx.graphics, javafx.base;

    exports org.example;
}