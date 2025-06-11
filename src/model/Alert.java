package model;

import java.time.LocalDateTime;

public class Alert 
{
	
	//destinatario , mittente (dottore o sistema), id dell'alert, messaggio, livello severità, data
	
	 public enum Gravita { BASSA, MEDIA, CRITICA };

	    private final String id;
	    @SuppressWarnings("unused") 
	    private Utente destinatario; //id 
	    private Paziente pazienteId;
	    private String messaggio;
	    private LocalDateTime timestamp;
	    private Gravita gravita;
	
	    //messaggio al paziente(destinatario) quando non prende le medicine
	    public Alert(String id, Utente destinatario, String messaggio, LocalDateTime timestamp)
	    {
	    	this.id = id;
	    	this.destinatario = destinatario;
	    	this.messaggio = messaggio;
	    	this.timestamp = timestamp;	    	
	    }
	    
	    //messaggio al diabetologo(destinatario) quando il paziente(id) non prende la medicina per + di 3 gg
	    public Alert(String id, Utente destinatario, Paziente pazienteId, String messaggio, LocalDateTime timestamp)
	    {
	    	this.id = id;
	    	this.destinatario = destinatario;
	    	this.pazienteId = pazienteId;
	    	this.messaggio = messaggio;
	    	this.timestamp = timestamp;	    	
	    
	    }
	    
	    //messaggio al diabetologo(destinatario) quando un paziente (id) ha glicemia irregolare
	    public Alert(String id, Utente destinatario,  Paziente pazienteId, String messaggio, LocalDateTime timestamp, Gravita gravita)
	    {
	    	this.id = id;
	    	this.destinatario = destinatario;
	    	this.pazienteId = pazienteId;
	    	this.messaggio = messaggio;
	    	this.timestamp = timestamp;	    	
	    	this.gravita = gravita;
	    }
	    
	   
	    
	    public String getId()
	    {
	    	return id;
	    }
	    
	    public Gravita getGravita()
	    {
	    	return gravita;
	    }
	    
	    public LocalDateTime timestamp()
	    {
	    	return timestamp;
	    }
	    
	    public String toString()
	    {
	    	String alert = "Attenzione: ";
	    	
	    	if (pazienteId == null)
		    {
		    	alert += messaggio;
		    }
		    else if (gravita == null)
		    {
		    	alert += pazienteId + messaggio; 
		    }
		    else 
		    	alert += pazienteId + messaggio + gravita;

		    return alert;   
	    }

    
}