package org.example;

import com.jcraft.jsch.JSchException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static javafx.application.Application.*;

public class Main extends Application {

    public void start(Stage escenario){
        try{
            Parent raiz = FXMLLoader.load(getClass().getResource("/PersonasVista.fxml"));
            Scene escena = new Scene(raiz);
            escenario.setScene(escena);
            escenario.show();
        } catch (IOException e){
            System.err.println("Error al cargar la vista FXML: " + e.getMessage());
        }

    }

    public static void main(String[] args) throws JSchException {
        launch(args);
    }

}
