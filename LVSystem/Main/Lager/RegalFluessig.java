package LVSystem.Main.Lager;

import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Waren.Warentyp;

public class RegalFluessig extends Regal {

    private GroesseFluessig groesse;

    public RegalFluessig(int id, Lagerhalle lagerhalle, GroesseFluessig groesse){
        super(id, lagerhalle, Lagerart.FLUESSIG);
        this.groesse = groesse;
    }

    public GroesseFluessig getGroesse(){
        return groesse;
    }

    public boolean kannLagern(Warentyp wt){
        if (this.getLagerart() == wt.getLagerart()){                // ueberpruefen, ob die Lagerart gleich ist 
            if (this.groesse.compareTo(wt.getGroesse()) >= 0){      // ueberpruefen, ob die Groesse des Lagers mindestens so gross ist wie die Groesse des Warentyps
                if (this.getAnzahlFrei() > 0){                      // uebgerpruefen, ob auch noch Platz ist
                    return true;                                    // return true, wenn beides der Fall ist
                }
            }
        }
        return false;                                               // andernfalls return false
    }

    public int vergleichGroesse(Warentyp wt){
        if (this.getLagerart() == wt.getLagerart()){                // ueberpruefen, ob die Lagerart gleich ist 
            return this.groesse.compareTo(wt.getGroesse());
        }
        return -1;
    }
    
}