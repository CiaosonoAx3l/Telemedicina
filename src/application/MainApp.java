package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApp extends Application 
{

    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        // Nota: il percorso parte da /view perché ora è un package a parte
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
        primaryStage.setTitle("Telemedicina Diabete T2");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// aggiungete un tasto per rimuovere il farmaco 
// quando invia un sintomo vuoto non deve arrivare al diabetologo