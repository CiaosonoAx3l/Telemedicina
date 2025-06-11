package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Utente 
{
	//variabili
	private final String codiceFiscale;
	private String nome;
	private String cognome;
	private String mail;
	private String password;
	
	private List<Alert> notifiche = new ArrayList<>();


	
	//costruttore
	protected Utente(String codiceFiscale, String nome, String cognome, String mail, String password)
	{
		this.codiceFiscale = codiceFiscale;
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.password = Integer.toHexString(password.hashCode());
	}
	
	//metodi
	public String getCodiceFiscale()
	{
		return codiceFiscale;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public String getCognome()
	{
		return cognome;
	}
	
	public String getMail()
	{
		return mail;
	}
	
	public boolean Checkpass(String pass)
	{
		return password.equals(Integer.toHexString(pass.hashCode()));
	}
	

	public void aggiungiNotifica(Alert alert) 
	{
	    notifiche.add(alert); 
	}

	public List<Alert> getNotifiche() 
	{
	    return notifiche;
	}
	
}