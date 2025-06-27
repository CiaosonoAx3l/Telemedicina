package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sintomo
{
	private String descrizione;
	private LocalDateTime data;
	
	public Sintomo(String descrizione)
	{
		this.descrizione = descrizione;
		this.data = LocalDateTime.now();
	}
	
	public String getDescrizione()
	{
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    return descrizione + " (" + data.format(formatter) + ")";
	}

	
}
