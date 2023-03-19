package LVSystem.Main.Waren;

import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;

public class WarentypFluessig extends Warentyp {

    private GroesseFluessig groesse;
    private float volumen;
    
    public WarentypFluessig(String name, float volumen, String beschreibung){
        super(name, beschreibung, Lagerart.FLUESSIG);
        this.groesse = GroesseFluessig.getGroesse(volumen);
        this.volumen = volumen;
    }

    public GroesseFluessig getGroesse(){
        return groesse;
    }

    public float getVolumen(){
        return volumen;
    }

}