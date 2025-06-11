package controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.util.Duration;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.Alert;
import model.Diabetologo;
import model.Farmaco;
import model.Paziente;
import model.Sintomo;
import model.Terapia;
import model.Glicemia;

public class DiabetologoController 
{
	//////////////////////////// INIZIO HOME ////////////////////////////////
	
    @FXML private Label benvenutoLabel;
    private Diabetologo diabetologo;

    public void setDiabetologo(Diabetologo d) 
    {
        this.diabetologo = d;
        aggiornaHome();
    }

    private void aggiornaHome() 
    {
        if (diabetologo != null && benvenutoLabel != null) 
        {
            benvenutoLabel.setText("Benvenuto, Dr. " + diabetologo.getCognome() + "!");
        }
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
            
            // Dopo aver mostrato il login, chiudi tutte le altre finestre (compreso lo stage corrente e eventuali finestre secondarie)
            List<Window> finestreDaChiudere = new ArrayList<>(Window.getWindows());
            for (Window w : finestreDaChiudere) 
            {
                if (w instanceof Stage && w != loginStage) 
                {
                    ((Stage) w).close();
                }
            }
            System.out.println("Pagina diabetologo chiusa, arrivederci Dr. " + diabetologo.getCognome());

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    //////////////////////////// FINE HOME //////////////////////////////////////////////
    
    //////////////////////////// INIZIO GESTIONE VISTA TABELLA PAZIENTI /////////////////
    
    @FXML private TableView<Paziente> tabellaPazienti;
    @FXML private TableColumn<Paziente, String> colNome;
    @FXML private TableColumn<Paziente, String> colCognome;
    @FXML private TableColumn<Paziente, String> colEmail;

    public void popolaTabellaPazienti() 
    {
        if (tabellaPazienti != null && diabetologo != null) 
        {
            colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
            colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCognome()));
            colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMail()));

            ObservableList<Paziente> lista = FXCollections.observableArrayList(diabetologo.getPazienti());
            tabellaPazienti.setItems(lista);
        }
    }

    @FXML
    private void handleVisualizzaPazienti() 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_diabetologo_ListaPazienti.fxml"));
            Parent root = loader.load();

            // Recupera il controller della nuova vista
            DiabetologoController controller = loader.getController();
            controller.setDiabetologo(diabetologo); // Passa il diabetologo per accedere ai pazienti
            controller.popolaTabellaPazienti(); // Metodo da creare per caricare la tabella

            Stage stage = new Stage();
            stage.setTitle("Lista Pazienti");
            stage.setScene(new Scene(root));
            stage.show();

        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleAggiungiPaziente() 
    {
        // Prendi tutti i pazienti esistenti
        List<Paziente> tuttiPazienti = DataUtentiLoader.getPazienti();

        // Filtra solo quelli non già associati al diabetologo
        List<Paziente> nonAssociati = new ArrayList<>();
        for (Paziente p : tuttiPazienti) 
        {
            if (!diabetologo.getPazienti().contains(p)) 
            {
                nonAssociati.add(p);
            }
        }

        if (nonAssociati.isEmpty()) 
        {
            return;
        }

        ChoiceDialog<Paziente> dialog = new ChoiceDialog<>(nonAssociati.get(0), nonAssociati);
        dialog.setTitle("Aggiungi Paziente");
        dialog.setHeaderText("Seleziona un paziente da aggiungere");
        dialog.setContentText("Paziente:");

        dialog.showAndWait().ifPresent(pazienteScelto -> 
        {
            diabetologo.aggiungiPaziente(pazienteScelto);
            popolaTabellaPazienti();
        });
    }
    
    @FXML
    private void handleTornaAlMenu(ActionEvent event) 
    {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    //////////////////////////// FINE GESTIONE VISTA TABELLA PAZIENTI ///////////////////

    ///////////////////////////// INIZIO GESTIONE PAZIENTE SINGOLO //////////////////////

    @FXML private ScrollPane scrollPane;
    double scrollPosition;
    
    private Paziente pazienteSelezionato;
    
    @FXML
    private void handlePazienteSelezionato(javafx.scene.input.MouseEvent event) 
    {
        if (event.getClickCount() == 2) { // solo doppio click
            Paziente selezionato = tabellaPazienti.getSelectionModel().getSelectedItem();
            if (selezionato != null) 
            {
            	try 
            	{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_diabetologo_Paziente.fxml"));
                    Parent root = loader.load();

                    DiabetologoController controller = loader.getController();
                    controller.setPaziente(selezionato);
                    controller.aggiorna();

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Dettagli del Paziente");

                } 
            	catch (IOException e) 
            	{
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void setPaziente(Paziente paziente) 
    {
    	this.pazienteSelezionato = paziente;
    	comboPeriodo.getItems().addAll("Ultimi 7 giorni", "Ultimi 30 giorni");
    	comboPeriodo.setValue("Ultimi 7 giorni"); // default
    }
    
    public void aggiorna()
    {
    	cfLabel.setText("Codice Fiscale: " + pazienteSelezionato.getCodiceFiscale());
    	nomeLabel.setText("Nome: " + pazienteSelezionato.getNome());
    	cognomeLabel.setText("Cognome: " + pazienteSelezionato.getCognome());
    	emailLabel.setText("Email: " + pazienteSelezionato.getMail());
    	
    	glicemieView.getItems().clear();
    	if (pazienteSelezionato.getGlicemia() != null)
    	{
    		for (Glicemia g : pazienteSelezionato.getGlicemia())
    		{
    			glicemieView.getItems().add(g.toString());
    		}
    	}
    	
    	if(pazienteSelezionato.getMalattia() != null)malattiaLabel.setText(pazienteSelezionato.getMalattia());

    	if(pazienteSelezionato.getTerapia() != null)
    	{
    		comboPeriodo.setOnAction(e -> caricaDatiGlicemia(pazienteSelezionato.getGlicemia()));
    		caricaDatiGlicemia(pazienteSelezionato.getGlicemia());
        	popolaTabellaFarmaci();
        	
        	IndicazioniTerapiaLabel.setText(pazienteSelezionato.getTerapia().getIndicazioni());
    	}
    	
    	aggiornaSintomiView();
    	
    	Node content = scrollPane.getContent();
        if (content != null) 
        {
            content.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> 
            {
                Platform.runLater(() -> scrollPane.setVvalue(scrollPosition));
            });
        }
    }
    
    ///////////////////////////// FINE GESTIONE PAZIENTE SINGOLO /////////////////////////
    
    //////////////////////////// INIZIO DETTAGLI PAZIENTE ////////////////////////////////
    
    @FXML private Label cfLabel;
    @FXML private Label nomeLabel;
    @FXML private Label cognomeLabel;
    @FXML private Label emailLabel;
    
    //////////////////////////// FINE DETTAGLI PAZIENTE //////////////////////////////////
    
    //////////////////////////// INIZIO GRAFICO GLICEMIE//////////////////////////////////
    
    @FXML private ListView<String> glicemieView;
    
    @FXML private LineChart<String, Number> lineChartGlicemia;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ComboBox<String> comboPeriodo;
    
    public void caricaDatiGlicemia(List<Glicemia> listaGlicemie) 
    {
    	if (listaGlicemie == null || comboPeriodo.getValue() == null) return;
    	
        // Determina il numero di giorni in base alla selezione del ComboBox
        int giorni = comboPeriodo.getValue().contains("7") ? 7 : 30;

        LocalDateTime limite = LocalDateTime.now().minusDays(giorni);

        // Filtra solo le glicemie recenti (ultimi 7 o 30 giorni)
        List<Glicemia> filtrate = listaGlicemie.stream()
                .filter(g -> g.getDataOra().isAfter(limite))
                .sorted(Comparator.comparing(Glicemia::getDataOra))
                .collect(Collectors.toList());

        // Crea la serie per il grafico
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Valori Glicemici");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        for (Glicemia g : filtrate) 
        {
            String data = g.getDataOra().format(formatter);
            XYChart.Data<String, Number> punto = new XYChart.Data<>(data, g.getValore());

            // Tooltip con toString della Glicemia
            Tooltip tooltip = new Tooltip(g.toString());
            tooltip.setShowDelay(Duration.millis(100));
            punto.nodeProperty().addListener((obs, oldNode, newNode) -> 
            {
                if (newNode != null) 
                {
                    Tooltip.install(newNode, tooltip);
                }
            });
            serie.getData().add(punto);
        }
        
        // Aggiorna il grafico
        lineChartGlicemia.getData().clear();
        lineChartGlicemia.getData().add(serie);

        // Nasconde le etichette e tick dell'asse X perche' non sono atualmente funzionanti per sdk
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setOpacity(0); // completamente invisibile
    }
    
    //////////////////////////// FINE GRAFICO GLICEMIE //////////////////////////////////
    
    //////////////////////////// INIZIO MALATTIA ////////////////////////////////////////

    @FXML private Label malattiaLabel;
    @FXML private TextArea malattiaModificabile;
    @FXML private Button tastoModificaMalattia;
    @FXML private Button tastoSalvaMalattia;

    @FXML
    private void handleModificaDescrizione() 
    {
        scrollPosition = scrollPane.getVvalue();  // Salva posizione

        malattiaModificabile.setText(pazienteSelezionato.getMalattia());
        
        malattiaLabel.setVisible(false);
        malattiaLabel.setManaged(false);
        
        malattiaModificabile.setVisible(true);
        malattiaModificabile.setManaged(true);
        
        tastoModificaMalattia.setVisible(false);
        tastoModificaMalattia.setManaged(false);
        
        tastoSalvaMalattia.setVisible(true);
        tastoSalvaMalattia.setManaged(true);
    }
    
    @FXML
    private void handleSalvaDescrizione() 
    {
        String nuovaDescrizione = "";
        
        if(malattiaModificabile.getText() != null)
        {
        	nuovaDescrizione = malattiaModificabile.getText().trim();
        }

        pazienteSelezionato.setMalattia(nuovaDescrizione);
        malattiaLabel.setText(nuovaDescrizione);

        malattiaLabel.setVisible(true);
        malattiaLabel.setManaged(true);

        malattiaModificabile.setVisible(false);
        malattiaModificabile.setManaged(false);

        tastoSalvaMalattia.setVisible(false);
        tastoSalvaMalattia.setManaged(false);

        tastoModificaMalattia.setVisible(true);
        tastoModificaMalattia.setManaged(true);
    }
    
    ////////////////////////////FINE MALATTIA ////////////////////////////////////////
    
    ////////////////////////////INIZIO TERAPIA ///////////////////////////////////////
    
    //////////////////////////// INIZIO TABELLA FARMACI //////////////////////////////
    
    @FXML private TableView<Farmaco> tabellaFarmaci;
    @FXML private TableColumn<Farmaco, String> nomeCol;
    @FXML private TableColumn<Farmaco, Integer> quantitaCol;
    @FXML private TableColumn<Farmaco, Integer> assunzioniCol;
    
    @FXML private Button bottoneAggiungiFarmaco;
    @FXML private Button bottoneSalvaFarmaco;
    @FXML private TextField nomeFarmacoModificabile;
	@FXML private TextField quantitaFarmacoModificabile;
	@FXML private TextField assunzioniFarmacoModificabile;
    
    public void popolaTabellaFarmaci() 
    {
        if (tabellaFarmaci != null) 
        {
        	nomeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        	quantitaCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantitaPerAssunzione()));
            assunzioniCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAssunzioniGiornaliere()));

            ObservableList<Farmaco> lista = FXCollections.observableArrayList(pazienteSelezionato.getTerapia().getFarmaci());
            tabellaFarmaci.setItems(lista);
        }
    }

    @FXML
    private void handleAggiungiFarmaco()
    {
    	scrollPosition = scrollPane.getVvalue();  // Salva posizione
    	
    	bottoneAggiungiFarmaco.setVisible(false); //si nasconde
    	bottoneAggiungiFarmaco.setManaged(false);

    	bottoneSalvaFarmaco.setVisible(true); //si attiva
    	bottoneSalvaFarmaco.setManaged(true);
    	
    	nomeFarmacoModificabile.setVisible(true); //si attiva
    	nomeFarmacoModificabile.setManaged(true);
    	
    	quantitaFarmacoModificabile.setVisible(true); //si attiva
    	quantitaFarmacoModificabile.setManaged(true);
    	
    	assunzioniFarmacoModificabile.setVisible(true); //si attiva
    	assunzioniFarmacoModificabile.setManaged(true);
    }
    
    @FXML
    private void handleSalvaFarmaco()
    {
    	String nomeF = nomeFarmacoModificabile.getText().trim();
    	int quantitaF = Integer.parseInt(quantitaFarmacoModificabile.getText().trim());
    	int assunzioniF = Integer.parseInt(assunzioniFarmacoModificabile.getText().trim());
    	String idF = Integer.toHexString(Objects.hash(nomeF, quantitaF, assunzioniF));
    	
    	Farmaco nuovoF = new Farmaco(idF, nomeF, quantitaF, assunzioniF);
    	
    	Terapia terapiaAttuale = pazienteSelezionato.getTerapia();
    	
    	// Se la terapia è null, ne creo una nuova e la associo
        if (terapiaAttuale == null) 
        {
        	String terapiaId = UUID.randomUUID().toString();
            terapiaAttuale = new Terapia(terapiaId);
            pazienteSelezionato.setTerapia(terapiaAttuale);
        }
        
    	if(terapiaAttuale.getFarmaci().contains(nuovoF) == false)
    	{
        	terapiaAttuale.aggiungiFarmaco(nuovoF);
    	}
    	
    	nomeFarmacoModificabile.clear();
    	quantitaFarmacoModificabile.clear();
    	assunzioniFarmacoModificabile.clear();
    	popolaTabellaFarmaci();
    	
    	//esatto opposto di aggiungi farmaco
    	bottoneAggiungiFarmaco.setVisible(true); //si attiva
    	bottoneAggiungiFarmaco.setManaged(true);

    	bottoneSalvaFarmaco.setVisible(false); //si disattiva
    	bottoneSalvaFarmaco.setManaged(false);
    	
    	nomeFarmacoModificabile.setVisible(false); //si disattiva
    	nomeFarmacoModificabile.setManaged(false);
    	
    	quantitaFarmacoModificabile.setVisible(false); //si disattiva
    	quantitaFarmacoModificabile.setManaged(false);
    	
    	assunzioniFarmacoModificabile.setVisible(false); //si disattiva
    	assunzioniFarmacoModificabile.setManaged(false);
    }
    
    //////////////////////////// FINE TABELLA FARMACI ///////////////////////////// 
    
    //////////////////////////// INIZIO INDICAZIONI ///////////////////////////////
    
    @FXML private Label IndicazioniTerapiaLabel;
    @FXML private TextArea IndicazioniTerapiaModificabile;
    @FXML private Button tastoModificaIndicazioniTerapia;
    @FXML private Button tastoSalvaIndicazioniTerapia;
    
    @FXML
    private void handleModificaIndicazioniTerapia()
    {
    	scrollPosition = scrollPane.getVvalue();  // Salva posizione
    	
    	if(pazienteSelezionato.getTerapia() != null)IndicazioniTerapiaModificabile.setText(pazienteSelezionato.getTerapia().getIndicazioni()); //modificabile repura il testo della malattia di prima 
    	
    	IndicazioniTerapiaLabel.setVisible(false); // Mostra bottone Salva, nasconde Modifica
    	IndicazioniTerapiaLabel.setManaged(false);

    	IndicazioniTerapiaModificabile.setVisible(true);
    	IndicazioniTerapiaModificabile.setManaged(true);
    	
    	tastoModificaIndicazioniTerapia.setVisible(false);
    	tastoModificaIndicazioniTerapia.setManaged(false);

    	tastoSalvaIndicazioniTerapia.setVisible(true);
    	tastoSalvaIndicazioniTerapia.setManaged(true);
    }
    
    @FXML
    private void handleSalvaIndicazioniTerapia() 
    {
        String nuovaIndicazione = "";
        
        if(IndicazioniTerapiaModificabile.getText() != null)
        {
        	nuovaIndicazione = IndicazioniTerapiaModificabile.getText().trim();
        }
        
        if(pazienteSelezionato.getTerapia() == null)
        {
        	String terapiaId = UUID.randomUUID().toString();
        	pazienteSelezionato.setTerapia(new Terapia(terapiaId));
        }

        pazienteSelezionato.getTerapia().setIndicazioni(nuovaIndicazione);
        IndicazioniTerapiaLabel.setText(nuovaIndicazione);
        
        IndicazioniTerapiaLabel.setVisible(true);
        IndicazioniTerapiaLabel.setManaged(true);

        IndicazioniTerapiaModificabile.setVisible(false);
        IndicazioniTerapiaModificabile.setManaged(false);

        tastoSalvaIndicazioniTerapia.setVisible(false);
        tastoSalvaIndicazioniTerapia.setManaged(false);

        tastoModificaIndicazioniTerapia.setVisible(true);
        tastoModificaIndicazioniTerapia.setManaged(true);
    }
    
    //////////////////////////// FINE INDICAZIONI ////////////////////////////////////
    
    //////////////////////////// FINE TERAPIA ////////////////////////////////////////
    
    //////////////////////////// INIZIO SINTOMI //////////////////////////////////////
    
    @FXML private ListView<String> sintomiView; 
    
    private void aggiornaSintomiView()
    {
    	sintomiView.getItems().clear();
    	if (pazienteSelezionato.getListaSintomi() != null)
    	{
    		for (Sintomo s : pazienteSelezionato.getListaSintomi())
    		{
    			sintomiView.getItems().add(s.getDescrizione());
    		}
    	}
    }
    
    //////////////////////////// FINE SINTOMI /////////////////////////////////////////
    
    ////////////////////////////// INIZIO NOTIFICHE ///////////////////////////////////
    
    @FXML private ListView<String> notificheListView;
    
    @FXML
    private void handleVisualizzaAlert(ActionEvent event) 
    {
    	 try 
         {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_diabetologo_notifiche.fxml"));
             Parent root = loader.load();

             DiabetologoController controller = loader.getController();
             controller.setDiabetologo(diabetologo);
             controller.aggiornaNotifiche();

             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
             stage.setScene(new Scene(root));
             stage.setTitle("Notifiche Diabetolgo");
         } 
         catch (IOException e) 
         {
             e.printStackTrace();
         }	
    }
    
    private void aggiornaNotifiche()
    {
    	 notificheListView.getItems().clear();
    	 for (Alert a : diabetologo.getNotifiche()) 
    	 {
    		 notificheListView.getItems().add(a.toString());
    	 }
    }
    
    @FXML
    private void handleTorna(ActionEvent event) 
    {
        try 
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/vista_diabetologo.fxml"));
            Parent root = loader.load();

            DiabetologoController controller = loader.getController();
            controller.setDiabetologo(diabetologo);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Telemedicina Diabete T2");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    ////////////////////////////// FINE NOTIFICHE /////////////////////////////////////
    
}