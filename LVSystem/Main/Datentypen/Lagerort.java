package LVSystem.Main.Datentypen;

import java.io.Serializable;

import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Lager.Regal;

public class Lagerort implements Serializable{
    Lagerhalle lagerhalle;
    Regal regal;
    int platz;
    Lagerart lagerart;

    public Lagerort(Lagerhalle lagerhalle, Regal regal, int platz, Lagerart lagerart){
        this.lagerhalle = lagerhalle;
        this.regal = regal;
        this.platz = platz;
        this.lagerart = lagerart;
    }

    public String toString(){
        return "Lager: " + lagerhalle.getName() + " - Regal: " + regal.getID() + " - " + lagerart.getRegalart() + ": " + platz;
    }

    public void auslagern(){
        regal.auslagern(platz);
    }

    public Lagerhalle getLagerhalle(){
        return this.lagerhalle;
    }
}
