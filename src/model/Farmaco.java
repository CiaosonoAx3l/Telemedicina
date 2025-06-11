package model;

public class Farmaco 
{
	private final String id;
	private String nome;
	private int quantitaPerAssunzione;
	private int assunzioniGiornaliere;
	//private Map<LocalDate, Boolean> storicoAssunzioni = new HashMap<>();

	
	//bool per giornaliero, settimanale o mensile /// gestione pillole

	public Farmaco(String id, String nome, int quantitaPerAssunzione, int assunzioniGiornaliere)
	{
		
		this.id = id;
		this.nome = nome;	
		this.quantitaPerAssunzione = quantitaPerAssunzione;
		this.assunzioniGiornaliere = assunzioniGiornaliere;

		
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public void setNome(String nome) {
	    this.nome = nome;
	}

	
	public int getQuantitaPerAssunzione()
	{
		return quantitaPerAssunzione;
	}
	
	public int getAssunzioniGiornaliere()
	{
		return assunzioniGiornaliere;
	}
	
	public void modificaQuantitaPerAssunzion(int quantitaPerAssunzione)
	{
		this.quantitaPerAssunzione = quantitaPerAssunzione;
	}
	
	public void modificaAssunzioniGiornaliere(int assunzioniGiornaliere)
	{
		this.assunzioniGiornaliere = assunzioniGiornaliere;
	}
	
	
	public String toString()
	{
		return nome + ", quantità per assunzione:" + quantitaPerAssunzione + " mg, " + assunzioniGiornaliere + " volte al giorno.";
	}
	
}