package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Farmaco;
import model.Glicemia;
import model.Paziente;
import model.Alert;

public class PazienteController 
{
	//////////////////////////// INIZIO HOME ////////////////////////////////
	
    @FXML private Label benvenutoLabel;
    public Paziente paziente;

    public void setPaziente(Paziente p) 
    {
        this.paziente = p;
        aggiornaHome();
    }

    private void aggiornaHome() 
    {
        if (paziente != null && benvenutoLabel != null) 
        {
            benvenutoLabel.setText("Benvenuto, " + paziente.getNome() + "!");
        }
    }

    @FXML
    private void handleTorna(ActionEvent event) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente.fxml"));
            Parent root = loader.load();

            PazienteController controller = loader.getController();
            controller.setPaziente(paziente);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home Paziente");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void fattiAssociareAlert()
    {
    	javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
	    alert.setTitle("Attenzione");
	    alert.setHeaderText(null);
	    alert.setContentText("ti consigliamo di farti associare al piu' presto un diabetologo");
	    alert.showAndWait();
    }

    @FXML
    private void handleEsci() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Telemedicina Diabete T2");
            loginStage.setScene(new Scene(root));
            loginStage.show();

            List<Window> finestreDaChiudere = new ArrayList<>(Window.getWindows());
            for (Window w : finestreDaChiudere) 
            {
                if (w instanceof Stage && w != loginStage)
                {
                    ((Stage) w).close();
                }
            }

            if (paziente != null) 
            {
                System.out.println("Pagina paziente chiusa, arrivederci " + paziente.getNome());
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    //////////////////////////// FINE HOME ////////////////////////////////
    
    //////////////////////////// INIZIO PROFILO ///////////////////////////
    
    @FXML private ScrollPane scrollPane;

    @FXML private Label cfLabel;
    @FXML private Label nomeLabel;
    @FXML private Label cognomeLabel;
    @FXML private Label emailLabel;
    @FXML private Label diabetologoLabel;
    @FXML private Label diabetologoEmailLabel;
    
    //TERAPIA INIZIO
    @FXML private TableView<Farmaco> tabellaFarmaci;
    @FXML private TableColumn<Farmaco, String> nomeCol;
    @FXML private TableColumn<Farmaco, Integer> quantitaCol;
    @FXML private TableColumn<Farmaco, Integer> assunzioniCol;
    @FXML private Label IndicazioniTerapiaLabel;
    //TERAPIA FINE
    
    @FXML
    private void handleProfilo(ActionEvent event) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente_profilo.fxml"));
            Parent root = loader.load();

            PazienteController controller = loader.getController();
            controller.setPaziente(paziente);
            controller.aggiornaProfilo();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Profilo Paziente");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    private void aggiornaProfilo() 
    {
    	Platform.runLater(() -> scrollPane.setVvalue(0.0));
        if (paziente != null) 
        {
            if (cfLabel != null) cfLabel.setText("Codice Fiscale: " + paziente.getCodiceFiscale());
            if (nomeLabel != null) nomeLabel.setText("Nome: " + paziente.getNome());
            if (cognomeLabel != null) cognomeLabel.setText("Cognome: " + paziente.getCognome());
            if (emailLabel != null) emailLabel.setText("Email: " + paziente.getMail());
            if (paziente.getDiabetologo() != null) diabetologoLabel.setText("Il tuo diabetologo: Dr." + paziente.getDiabetologo().getCognome());
            else diabetologoLabel.setText("Non hai ancora un diabetologo associato");
            if (paziente.getDiabetologo() != null) diabetologoEmailLabel.setText("Email: " + paziente.getDiabetologo().getMail());
            if(paziente.getTerapia() != null)popolaTabellaFarmaci();
            if(paziente.getTerapia() != null)IndicazioniTerapiaLabel.setText(paziente.getTerapia().getIndicazioni());
        }
    }
    
    public void popolaTabellaFarmaci() 
    {
        if (tabellaFarmaci != null) 
        {
        	nomeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        	quantitaCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantitaPerAssunzione()));
            assunzioniCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAssunzioniGiornaliere()));

            ObservableList<Farmaco> lista = FXCollections.observableArrayList(paziente.getTerapia().getFarmaci());
            tabellaFarmaci.setItems(lista);
        }
    }

    //////////////////////////// FINE PROFILO /////////////////////////////////
    
    //////////////////////////// INIZIO REPORT ////////////////////////////////
    
    @FXML private TableView<Farmaco> tabellaReport;
    @FXML private TableColumn<Farmaco, String> farmacoReportCol;
    @FXML private TableColumn<Farmaco, Integer> quantitaReportCol;
    
    private List<Farmaco> farmaciAssunti = new ArrayList<>();
    
    @FXML
    private void handleRegistrazioni(ActionEvent event) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente_report.fxml"));
            Parent root = loader.load();

            PazienteController controller = loader.getController();
            controller.setPaziente(paziente);
            controller.aggiornaReport();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Report Paziente");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    private void aggiornaReport() 
    {
        farmacoReportCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));
        quantitaReportCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantitaPerAssunzione()).asObject());

        ObservableList<Farmaco> lista = FXCollections.observableArrayList(farmaciAssunti);
        tabellaReport.setItems(lista);
    }
    
    @FXML
    private void handleAggiungiFarmaco()
    {
    	if(paziente.getTerapia() != null)
    	{
	        List<Farmaco> FarmaciDisponibili = new ArrayList<>();
	        
	        for(Farmaco f : paziente.getTerapia().getFarmaci()) 
	        {
	        	FarmaciDisponibili.add(f);
	        }
	
	        if (FarmaciDisponibili.isEmpty()) 
	        {
	            return;
	        }
	
	        // Mostra una finestra di scelta
	        ChoiceDialog<Farmaco> dialog = new ChoiceDialog<>(FarmaciDisponibili.get(0), FarmaciDisponibili);
	        
	        dialog.setTitle("Aggiungi i farmaci presi oggi");
	        dialog.setHeaderText("Seleziona un farmaco preso");
	        dialog.setContentText("Farmaco:");
	        dialog.showAndWait().ifPresent(farmacoScelto -> 
	        {
	        	farmaciAssunti.add(farmacoScelto);
	            aggiornaReport();
	        });
    	}
    	else
    	{
    	    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    	    alert.setTitle("Nessuna Terapia");
    	    alert.setHeaderText(null);
    	    alert.setContentText("Non puoi aggiungere farmaci se non hai una terapia assegnata.");
    	    alert.showAndWait();
    	}
    }
    
    @FXML
    private void handleInvia() 
    {
    	if(paziente.getTerapia() != null)
    	{
    		@SuppressWarnings("unused") boolean errore = false;
	        List<Farmaco> farmaciPrevisti = paziente.getTerapia().getFarmaci();
	
	        // Se non ci sono farmaci previsti
	        if (farmaciPrevisti == null || farmaciPrevisti.isEmpty()) 
	        {
	            paziente.resetSgarri();
	            System.out.println("Nessun farmaco da assumere quindi assunzione considerata corretta.");
	            farmaciAssunti.clear();
	            aggiornaReport();
	            return;
	        }
	        
	        // Conta le assunzioni attese per nome
	        Map<String, Integer> attese = new HashMap<>();
	        for (Farmaco f : farmaciPrevisti) 
	        {
	            attese.put(f.getNome(), attese.getOrDefault(f.getNome(), 0) + f.getAssunzioniGiornaliere());
	        }
	
	        // Conta le assunzioni effettive per nome
	        Map<String, Integer> ricevute = new HashMap<>();
	        for (Farmaco f : farmaciAssunti) 
	        {
	            ricevute.put(f.getNome(), ricevute.getOrDefault(f.getNome(), 0) + 1);
	        }
	
	        // Controlla se tutte le assunzioni sono corrette
	        for (String nome : attese.keySet()) 
	        {
	            if (!ricevute.containsKey(nome) || !attese.get(nome).equals(ricevute.get(nome)))
	            {
	                System.out.println("Errore: per il farmaco '" + nome + "' attese " + attese.get(nome)
	                    + " assunzioni, ma trovate " + ricevute.getOrDefault(nome, 0));
	                errore = true;
	            }
	        }
	
	        /* Controlla se ci sono farmaci non previsti
	        for (String nome : ricevute.keySet()) 
	        {
	            if (!attese.containsKey(nome)) 
	            {
	                System.out.println("Errore: farmaco '" + nome + "' non previsto nella terapia.");
	                errore = true;
	            }
	        }*/

	        if (errore == true) 
	        {
	            paziente.incrementaSgarro();
	            int sgarri = paziente.getSgarri();
	            System.out.println("Assunzione errata. Sgarri consecutivi: " + sgarri);
	
	            // Notifica al paziente
	            Alert notificaPaziente = new Alert(
	                UUID.randomUUID().toString(),
	                paziente,
	                " non hai seguito la terapia oggi.",
	                LocalDateTime.now()
	            );
	            paziente.aggiungiNotifica(notificaPaziente);
	            System.out.println("Notifica inviata al paziente.");
	
	            // Se sgarri consecutivi >= 3, notifica anche al diabetologo
	            if (sgarri >= 3 && paziente.getDiabetologo() != null) 
	            {
	                Alert notificaDottore = new Alert(
	                    UUID.randomUUID().toString(),
	                    paziente.getDiabetologo(),
	                    paziente,
	                    " ha sbagliato l’assunzione dei farmaci per piu' di 2 giorni consecutivi.",
	                    LocalDateTime.now()
	                );
	                paziente.getDiabetologo().aggiungiNotifica(notificaDottore);
	                System.out.println("Notifica inviata al diabetologo.");
	            }
	            else
	            {
	            	if(paziente.getDiabetologo() == null)fattiAssociareAlert();
	            }
	            aggiornaReport();
	        }
	        else
	        {
	        	paziente.resetSgarri();
		        System.out.println("Assunzione corretta. Sgarri consecutivi azzerati.");
		        farmaciAssunti.clear();
		        aggiornaReport();
	        }
    	}
    	else
    	{
    	    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    	    alert.setTitle("Nessuna Terapia");
    	    alert.setHeaderText(null);
    	    alert.setContentText("Non puoi inviare il report senza una Terapia dichiarata");
    	    alert.showAndWait();
    	}
    	
    	mostraMessaggio("inviato con successo");
    }
    
    /////////////////////////////// FINE REPORT ///////////////////////////////////
    
    /////////////////////////////// INIZIO GLICEMIA ///////////////////////////////
   
    @FXML private ListView<String> glicemieView;
    @FXML private TextField fieldValore;
    @FXML private CheckBox boxDopoPasto;
    @FXML private Button buttonInserisci;
    @FXML private Button buttonSalva;
   
    @FXML
    private void handleInserisciGlicemia(ActionEvent event) 
    {
    	 try 
         {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente_glicemia.fxml"));
             Parent root = loader.load();

             PazienteController controller = loader.getController();
             controller.setPaziente(paziente);
             controller.aggiornaGlicemia();

             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(new Scene(root));
             stage.setTitle("Glicemia Paziente");
         } 
         catch (IOException e) 
         {
             e.printStackTrace();
         }	
    }
    
    
    private void aggiornaGlicemia()
    {
    	glicemieView.getItems().clear();
    	if (paziente.getGlicemia() != null)
    	{
    		for (Glicemia g : paziente.getGlicemia())
    		{
    			glicemieView.getItems().add(g.toString());
    		}
    	} 	
    }
    
    @FXML
    private void handleInserisci() 
    {
    	 buttonInserisci.setVisible(false); //si nasconde
    	 buttonInserisci.setManaged(false);

         buttonSalva.setVisible(true); //si attiva
         buttonSalva.setManaged(true);

         fieldValore.setVisible(true);
         fieldValore.setManaged(true);

         boxDopoPasto.setVisible(true);
         boxDopoPasto.setManaged(true);
    }
    
    @FXML
    private void handleSalva()
    {
    	 String nuovoValoreStringa = fieldValore.getText().trim();
    	 boolean dopoPasto = boxDopoPasto.isSelected();
    	 
    	 Integer nuovoValore = Integer.parseInt(nuovoValoreStringa);
    	 
    	 Glicemia nuovaGlicemia = new Glicemia(nuovoValore, dopoPasto, LocalDateTime.now());
    	 paziente.aggiungiGlicemia(nuovaGlicemia);
    	 
    	 controlloGlicemia(nuovoValore, dopoPasto);
    	 
    	 buttonInserisci.setVisible(true); 
    	 buttonInserisci.setManaged(true);

         buttonSalva.setVisible(false); 
         buttonSalva.setManaged(false);

         fieldValore.setVisible(false);
         fieldValore.setManaged(false);

         boxDopoPasto.setVisible(false);
         boxDopoPasto.setManaged(false);
         
         aggiornaGlicemia();
    }
    
    private void controlloGlicemia(int nuovoValore, boolean dopoPasto) 
    {
        Alert.Gravita gravita = null;
        String messaggio = "";

        if (dopoPasto == false) 
        {
            if (nuovoValore < 70) 
            {
                gravita = Alert.Gravita.BASSA;
                messaggio += " ha un'ipoglicemia grave a digiuno. ";
            } 
            else if (nuovoValore > 130 && nuovoValore <= 180) 
            {
                gravita = Alert.Gravita.MEDIA;
                messaggio += " ha una lieve iperglicemia a digiuno. ";
            } 
            else if (nuovoValore > 180) 
            {
                gravita = Alert.Gravita.CRITICA;
                messaggio += " ha una grave iperglicemia a digiuno. ";
            }
        } 
        else 
        {
            if (nuovoValore < 70) 
            {
                gravita = Alert.Gravita.BASSA;
                messaggio += " ha un'ipoglicemia grave dopo pasto. ";
            } 
            else if (nuovoValore > 180 && nuovoValore <= 250) 
            {
                gravita = Alert.Gravita.MEDIA;
                messaggio += " ha una lieve iperglicemia dopo pasto. ";
            } 
            else if (nuovoValore > 250) 
            {
                gravita = Alert.Gravita.CRITICA;
                messaggio += " ha una grave iperglicemia dopo pasto. ";
            }
        }

        if (gravita != null && paziente.getDiabetologo() != null) 
        {
            Alert notifica = new Alert(
                UUID.randomUUID().toString(),
                paziente.getDiabetologo(),
                paziente,
                messaggio + "Valore registrato: " + nuovoValore + " mg/dL.",
                LocalDateTime.now(),
                gravita
            );
            paziente.getDiabetologo().aggiungiNotifica(notifica);
            System.out.println("Notifica inviata: " + notifica);
        } 
        else if (gravita != null) 
        {
            System.out.println("Valore critico (" + nuovoValore + " mg/dL) ma nessun diabetologo assegnato.");
            if(paziente.getDiabetologo() == null)fattiAssociareAlert();
            Alert notifica = new Alert(
                    UUID.randomUUID().toString(),
                    paziente,
                    "hai registrato valori sballati di glicemia. " + "Valore registrato: " + nuovoValore + " mg/dL.",
                    LocalDateTime.now()
                );
            paziente.aggiungiNotifica(notifica);
        } 
        else 
        {
            System.out.println("Valore normale: " + nuovoValore + " mg/dL.");
        }
    }
    
    /////////////////////////////// FINE GLICEMIA ///////////////////////////////
    
    ////////////////////////////// INIZIO SINTOMI ///////////////////////////////
   
    @FXML private TextField fieldSintomo;
    @FXML private Button buttonSalvaSintomi;
    @FXML private Label inviatoLabel;
    
    @FXML
    private void handleSegnalaSintomi(ActionEvent event) 
    {
    	 try 
         {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente_sintomi.fxml"));
             Parent root = loader.load();

             PazienteController controller = loader.getController();
             controller.setPaziente(paziente);
             controller.aggiornaSintomi();

             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(new Scene(root));
             stage.setTitle("Sintomi Paziente");
         } 
         catch (IOException e) 
         {
             e.printStackTrace();
         }	
    }
    
    private void aggiornaSintomi()
    {
    	//in caso facciamo una view di sintomi
    }
    
    @FXML
    private void handleSalvaSintomi()
    {
     String nuovoSintomo = fieldSintomo.getText().trim();
     
     if(nuovoSintomo.isEmpty())
     {
    	 mostraMessaggio("campo vuoto");
    	 return;
     }
   	 
   	 paziente.aggiungiSintomi(nuovoSintomo);
   	 fieldSintomo.clear();
   	 mostraMessaggio("inviato con successo");
   	 aggiornaSintomi();
    }

    private void mostraMessaggio(String messaggio)
    {
    	inviatoLabel.setText(messaggio);
    	Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(4),evt->inviatoLabel.setText("")));
    	timeline.setCycleCount(1);
    	timeline.play();
    }
    
    ////////////////////////////// FINE SINTOMI //////////////////////////////
 
    //////////////////////////////INIZIO NOTIFICHE//////////////////////////////
    @FXML private ListView<String> notificheListView;
    
    @FXML
    private void handleVisualizzaAlert(ActionEvent event) 
    {
    	 try 
         {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_paziente_notifiche.fxml"));
             Parent root = loader.load();

             PazienteController controller = loader.getController();
             controller.setPaziente(paziente);
             controller.aggiornaNotifiche();

             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(new Scene(root));
             stage.setTitle("Notifiche Paziente");
         } 
         catch (IOException e) 
         {
             e.printStackTrace();
         }	
    }
    
    private void aggiornaNotifiche()
    {
    	 notificheListView.getItems().clear();
    	 for (Alert a : paziente.getNotifiche()) 
    	 {
    		 if (a.timestamp().isAfter(LocalDateTime.now().minusDays(3))) //guarda le notifiche negli ultimi 3 giorni
    	     {
                    notificheListView.getItems().add(a.toString());
             }
    	 }
    }
    //////////////////////////////FINE NOTIFICHE//////////////////////////////
}