package model;

import java.util.ArrayList;
import java.util.List;

public class Paziente extends Utente 
{
	//variabili
	private Diabetologo diabetologo;
	private Terapia terapia;
	private String malattia;
	private List<Glicemia> listaGlicemie = new ArrayList<>();
	private List<Sintomo> listaSintomi = new ArrayList<>();
	private int sgarri;
	
	//costruttore
	public Paziente(String codiceFiscale, String nome, String cognome, String mail, String password)
	{
		super(codiceFiscale, nome, cognome, mail, password);
		diabetologo = null;
		terapia = null;
		this.sgarri = 0;
	}
	public void resetSgarri()
	{
		sgarri = 0;
	}
	public void incrementaSgarro()
	{
		sgarri++;
	}
	public int getSgarri()
	{
		return sgarri;
	}
	public List<Glicemia> getGlicemia()
	{
		return listaGlicemie;
	}
	
	public void aggiungiGlicemia(Glicemia glicemia)
	{
		listaGlicemie.add(glicemia); 
	}
	
	//metodi per la gestione del diabetologo
	public Diabetologo getDiabetologo()
	{
		return diabetologo;
	}
	
	public void setDiabetologo(Diabetologo diabetologo)
	{
		this.diabetologo = diabetologo; 
	}
	
	//metodi per la gestione della terapia
	public Terapia getTerapia()
	{
		return terapia;
	}
	
	public void setTerapia(Terapia terapia)
	{
		this.terapia = terapia;
	}
	

    public String getMalattia() {
        return malattia;
    }

    public void setMalattia(String malattia) {
        this.malattia = malattia;
    }
    
    public List<Sintomo> getListaSintomi()
    {
    	return listaSintomi;
    }
    
    public void aggiungiSintomi(String descrizioneDelSintomo)
    {
    	listaSintomi.add(new Sintomo(descrizioneDelSintomo));
    }
    
    public String toString()
    {
    	return getCodiceFiscale();
    }
}
