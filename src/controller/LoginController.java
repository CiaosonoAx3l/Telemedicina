package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Diabetologo;
import model.Paziente;

public class LoginController 
{
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private boolean initialized = false;

    private void inizializza() 
    {
        if (!initialized) 
        {
            DataUtentiLoader.inizializzaUtenti();
            initialized = true;
        }
    }

    @FXML 
    private void handleLogin() 
    {
        inizializza();
        String email = emailField.getText();
        String pwd = passwordField.getText();

        for (Paziente p : DataUtentiLoader.getPazienti()) 
        {
            if (p.getMail().equalsIgnoreCase(email) && p.Checkpass(pwd)) 
            {
                errorLabel.setVisible(false);
                System.out.println("Login paziente riuscito: " + p.getNome());
                caricaVistaPaziente(p);
                return;
            }
        }

        for (Diabetologo d : DataUtentiLoader.getDiabetologi()) 
        {
            if (d.getMail().equalsIgnoreCase(email) && d.Checkpass(pwd)) 
            {
                errorLabel.setVisible(false);
                System.out.println("Login diabetologo riuscito: Dr. " + d.getCognome());
                caricaVistaDiabetologo(d);
                return;
            }
        }

        errorLabel.setText("Email o password non validi");
        errorLabel.setVisible(true);
    }
    
    private void caricaVistaDiabetologo(Diabetologo d) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_diabetologo.fxml"));
            Parent root = loader.load();
            DiabetologoController controller = loader.getController();
            controller.setDiabetologo(d);

            Stage stage = (Stage) emailField.getScene().getWindow();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
            stage.setScene(scene);
            
            stage.show();
            stage.setTitle("Home Diabetologo");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    private void caricaVistaPaziente(Paziente p) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente.fxml"));
            Parent root = loader.load();
            PazienteController controller = loader.getController();
            controller.setPaziente(p);

            Stage stage = (Stage) emailField.getScene().getWindow();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
            stage.setScene(scene);
            
            stage.show();
            stage.setTitle("Home Paziente");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleEsci() 
    {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    } 
}