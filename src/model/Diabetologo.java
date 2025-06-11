package model;

import java.util.ArrayList;
import java.util.List;

public class Diabetologo extends Utente 
{
	//variabili
	private List<Paziente> listaPazienti = new ArrayList<>();
	
	//costruttori
	public Diabetologo(String codiceFiscale, String nome, String cognome, String mail, String password)
	{
		super(codiceFiscale, nome, cognome, mail, password);
	}
	
	
	//metodi per la gestione dei pazienti
    public List<Paziente> getPazienti() 
    { 
    	return listaPazienti; 
    }
    
    public void aggiungiPaziente(Paziente p) 
    {
        if (listaPazienti.contains(p) == false) 
        {
        	listaPazienti.add(p);
            p.setDiabetologo(this);
        }
    }
    public void togliPaziente(Paziente p) 
    {
    	if (listaPazienti.contains(p) == true) 
        {
    		listaPazienti.remove(p);
            p.setDiabetologo(null);
        }
        
    }
	
  //metodi per la gestione dei pazienti
    
    public void aggiungiTerapia(Paziente p, Terapia nuovaTerapia) //qui piu' che aggiungerla va creata
    {
    	p.setTerapia(nuovaTerapia);
    }
    
    public void rimuoviTerapia(Paziente p)
    {
    	p.setTerapia(null);
    }
}