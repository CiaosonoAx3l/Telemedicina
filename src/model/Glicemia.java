package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Glicemia 
{
    private int valore; // valore glicemico in mg/dL
    private boolean dopoPasto; //si intende dopo 2 ore dall' inizio della consumazione del pasto
    private LocalDateTime dataOra;

    public Glicemia(int valore, boolean dopoPasto, LocalDateTime dataOra) 
    {
        this.valore = valore;
        this.dopoPasto = dopoPasto;
        this.dataOra = dataOra;
    }

    public int getValore() 
    {
        return valore;
    }

    public void setValore(int valore) 
    {
        this.valore = valore;
    }

    public boolean isDopoPasto() 
    {
        return dopoPasto;
    }

    public void setDopoPasto(boolean dopoPasto) 
    {
        this.dopoPasto = dopoPasto;
    }

    public LocalDateTime getDataOra() 
    {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) 
    {
        this.dataOra = dataOra;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Glicemia{" +
                "valore=" + valore +
                ", dopoPasto=" + dopoPasto +
                ", dataOra=" + dataOra.format(formatter) +
                '}';
    }

}
