package LVSystem.Main.Waren;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Lager.LagerVerwalter;
import LVSystem.Main.Lager.Lagerhalle;

public class Ware implements Serializable{

    private Warentyp warentyp;
    private int id;
    private Date eingangszeitpunkt;
    private Lagerort lagerort;

    private LagerVerwalter lagerVerwalter = LagerVerwalter.getInstance();

    public Ware(Warentyp warentyp, int id){
        this.warentyp = warentyp;
        this.id = id;
        this.eingangszeitpunkt = new Date();
    }

    public int getId() {
        return id;
    }
    
    public Date getEingangszeitpunkt() {
        return eingangszeitpunkt;
    }

    public Warentyp getWarentyp(){
        return this.warentyp;
    }

    public Lagerort getLagerort(){
        return lagerort;
    }

    public String getName(){
        return warentyp.getName() + "-" + id;
    }

    public String getEingangszeitpunktStr(){                    // Methode zum Erhalten des Anlagezeitpunktes als String
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);       
        return df.format(eingangszeitpunkt);
    }

    public void einlagern(Lagerhalle lager){                    // Methode zum einlagern dieser Ware
        if (Objects.isNull(lager)){
            this.lagerort = lagerVerwalter.einlagern(this);     // wenn kein Lager angegeben wurde Einlagern ueber den LagerVerwalter
        }else{
            this.lagerort = lager.einlagern(this);              // wenn ein Lager angegeben ist, einlagern ueber dieses Lager
        }
    }
    
}
