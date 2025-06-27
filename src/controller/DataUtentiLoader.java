package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Diabetologo;
import model.Paziente;
import model.Glicemia;
import model.Farmaco;
import model.Terapia;

public class DataUtentiLoader 
{
    private static List<Paziente> pazienti = new ArrayList<>();
    private static List<Diabetologo> diabetologi = new ArrayList<>();
    private static boolean inizializzato = false;

    
    
    public static void inizializzaUtenti() 
    {
    	if(inizializzato == false)
    	{
	    	//diabetologi
	        diabetologi.add(new Diabetologo(
	                "DBTLGI80A01H501Z",
	                "Mario",
	                "Rossi",
	                "mario.rossi@ospedale.it",
	                "123"
	        ));
	        
	        diabetologi.add(new Diabetologo(
	                "dfusahIKLUOFWDSUUG7IF9KDS",
	                "Mario",
	                "Rossi",
	                "mario",
	                "123"
	        ));
	
	        // Pazienti
	        
	        Paziente marco = new Paziente(
	                "MRCBNCMR95C03H501X",
	                "Marco",
	                "Bianchi",
	                "marco",
	                "123"
	        );
	        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	        // Supponendo che 'marco' sia un oggetto paziente con metodo aggiungiGlicemia
	        Glicemia newGlicemia;

	        // Glicemia dopo cena (31 giorni fa)
	        newGlicemia = new Glicemia(160, true, LocalDateTime.now().minusDays(31).withHour(21).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);
	        // Glicemia dopo cena (30 giorni fa)
	        newGlicemia = new Glicemia(160, true, LocalDateTime.now().minusDays(30).withHour(21).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);
	        // Glicemia dopo cena (20 giorni fa)
	        newGlicemia = new Glicemia(160, true, LocalDateTime.now().minusDays(20).withHour(21).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia dopo pranzo (10 giorni fa)
	        newGlicemia = new Glicemia(180, true, LocalDateTime.now().minusDays(10).withHour(14).withMinute(15));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia serale prima di cena (9 giorni fa)
	        newGlicemia = new Glicemia(130, false, LocalDateTime.now().minusDays(9).withHour(19).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia dopo cena (8 giorni fa)
	        newGlicemia = new Glicemia(160, true, LocalDateTime.now().minusDays(8).withHour(21).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia dopo colazione (7 giorni fa)
	        newGlicemia = new Glicemia(145, true, LocalDateTime.now().minusDays(7).withHour(8).withMinute(30));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia serale prima di cena (6 giorni fa)
	        newGlicemia = new Glicemia(130, false, LocalDateTime.now().minusDays(6).withHour(19).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);
	        
	        // Glicemia dopo pranzo (5 giorni fa)
	        newGlicemia = new Glicemia(180, true, LocalDateTime.now().minusDays(5).withHour(11).withMinute(15));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia prima di pranzo (5 giorni fa)
	        newGlicemia = new Glicemia(110, false, LocalDateTime.now().minusDays(5).withHour(12).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia dopo colazione (4 giorni fa)
	        newGlicemia = new Glicemia(145, true, LocalDateTime.now().minusDays(4).withHour(8).withMinute(30));
	        marco.aggiungiGlicemia(newGlicemia);

	        // Glicemia prima di pranzo (3 giorni fa)
	        newGlicemia = new Glicemia(110, false, LocalDateTime.now().minusDays(3).withHour(12).withMinute(0));
	        marco.aggiungiGlicemia(newGlicemia);

	        
	        
	        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	        
	        Farmaco farmaco= new Farmaco("444", "tachipirina", 100, 1);
	        Terapia T = new Terapia ("1234");
	        T.aggiungiFarmaco(farmaco);
	        farmaco= new Farmaco("4", "insulina", 100, 2);
	        T.aggiungiFarmaco(farmaco);
	        
	        marco.setTerapia(T);
	        
	        
	        
	        marco.aggiungiSintomi("mi fa male la testa");
	        marco.aggiungiSintomi("mi pulsa l'occhio");
	        
	        pazienti.add(marco);
	
	        pazienti.add(new Paziente(
	                "PZNTAN88T20H501R",
	                "Anna",
	                "Verdi",
	                "anna.verdi@email.com",
	                "123"
	        ));
	        inizializzato = true;
    	}
    }

    public static List<Paziente> getPazienti() 
    {
        return pazienti;
    }

    public static List<Diabetologo> getDiabetologi() 
    {
        return diabetologi;
    }
}
