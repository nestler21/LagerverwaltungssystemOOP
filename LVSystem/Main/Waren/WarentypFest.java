package LVSystem.Main.Waren;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.Lagerart;

public class WarentypFest extends Warentyp{

    private GroesseFest groesse;
    private float hoehe;
    private float breite;
    private float tiefe;
    
    public WarentypFest(String name,float hoehe, float breite, float tiefe, String beschreibung){
        super(name, beschreibung, Lagerart.FEST);
        this.groesse = GroesseFest.getGroesse(breite, hoehe, tiefe);
        this.hoehe = hoehe;
        this.breite = breite;
        this.tiefe = tiefe;
    }

    public GroesseFest getGroesse(){
        return groesse;
    }

    public float getHoehe(){
        return hoehe;
    }
    
    public float getBreite(){
        return breite;
    }

    public float getTiefe(){
        return tiefe;
    }
}