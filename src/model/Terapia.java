package model;

import java.util.ArrayList;
import java.util.List;

public class Terapia 
{
	//variabili
	private final String id;
	private List<Farmaco> listaFarmaci = new ArrayList<>();
	private String indicazioni; //attività sportiva o altro
	
	
	//costruttore
	public Terapia(String id)
	{
		this.id = id;
	}
	
	public String getId()
	{
		return id;
		
	}
	
	public String getIndicazioni()
	{
		return indicazioni;
	}
	
	public void setIndicazioni(String indicazioni)
	{
		this.indicazioni = indicazioni;
	}
	
	public List<Farmaco> getFarmaci()
	{
		return listaFarmaci;
	}
	
	public void aggiungiFarmaco(Farmaco farmaco)
	{
		  if (listaFarmaci.contains(farmaco) == false) 
	      {
			  listaFarmaci.add(farmaco);
	      }
	}
	
	public void rimuoviFarmaco(Farmaco farmaco)
	{
		  if (listaFarmaci.contains(farmaco) == true) 
	      {
			  listaFarmaci.remove(farmaco);
	      }
	}
	
	
	
	public String toString() 
	{
	    String terapia = "La tua terapia include i seguenti farmaci:\n";
	    
	    for (Farmaco farmaco : listaFarmaci) 
	    {
	        terapia += "- " + farmaco.toString() + "\n";
	    }

	    if (indicazioni == null)
	    {
	    	terapia += "Nessuna indicazione";
	    }
	    else
	    {
	    	terapia += "Indicazioni: " + indicazioni; 
	    }
	    	    
	    return terapia;   
	}

	
}