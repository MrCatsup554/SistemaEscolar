package org.example;

import com.jcraft.jsch.JSchException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.*;

public class Main extends Application {

    public void start(Stage stage) throws IOException {
        
        
        Parent root = FXMLLoader.load(getClass().getResource("/InicioVista.fxml"));
        
       
        Scene scene = new Scene(root, 700, 600); 
        
        stage.setTitle("Sistema de Gestión"); 
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws JSchException {
        launch(args);
    }

}
