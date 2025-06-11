package model;

import java.time.LocalDateTime;

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
		return this.descrizione + data.toString();
	}
	
}
