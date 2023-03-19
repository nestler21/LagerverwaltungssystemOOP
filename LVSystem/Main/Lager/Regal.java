package LVSystem.Main.Lager;

import java.io.Serializable;
import java.util.Objects;

import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;

public abstract class Regal implements Serializable{

    private int id;
    private Lagerhalle lagerhalle;
    private static final int anzahl = 10;
    private Ware[] waren = new Ware[anzahl];
    private int anzahlBelegt = 0;      // zaehlt die Anzahl eingelagerter Waren
    private Lagerart lagerart;

    public Regal(int id, Lagerhalle lagerhalle, Lagerart lagerart){
        this.id = id;
        this.lagerhalle = lagerhalle;
        this.lagerart = lagerart;
    }

    public int getID(){
        return id;
    }

    public static int getAnzahl(){
        return anzahl;
    }

    public int getAnzahlBelegt(){
        return anzahlBelegt;
    }

    public int getAnzahlFrei(){
        return anzahl - this.anzahlBelegt;
    }

    public Ware getWare(int fach){
        if (!isValidesFach(fach)){
            return null;
        }
        return waren[fach-1];
    }

    private boolean isValidesFach(int fach){
        if (fach <= 0 || fach > anzahl){
            return false;
        }
        return true;
    }

    public abstract <T> T getGroesse();

    public abstract boolean kannLagern(Warentyp wt);                // Methode zum ueberpruefen, ob das Regal einen Warentyp lagern kann
    
    public boolean kannLagern(Ware ware){                           // Methode zum ueberpruefen, ob das Regal eine Ware von der Groesse und vom Platz her lagern kann
        return kannLagern(ware.getWarentyp());                      // ueberprueft, ob das Regal den Warentyp der Ware lagern kann 
    }

    public abstract int vergleichGroesse(Warentyp wt);

    public Lagerart getLagerart(){
        return lagerart;
    }

    public Lagerort einlagern(Ware ware) {                          // Methode zum Einlagern einer Ware
        int index = -1;
        for (int i = 0; i < anzahl; i++){
            if(Objects.isNull(waren[i])){                           // Iteration durch Waren, um freien Platz zu finden
                index = i;                                          // Speichern des Index eines freien Platzes
                break;
            }
        }
        if (index == -1) return null;                               // wenn kein freier Platz gefunden wurde: return null (sollte nicht passieren, da einlagern in Lagerhalle genau das abfaengt)
        this.anzahlBelegt++;                                        // anzahlBelegt wird entsprechend angepasst
        waren[index] = ware;                                        // Die Ware wird an dem entsprechenden Platz abgelegt
        return new Lagerort(lagerhalle, this, index+1, this.getLagerart());   // Rueckgabe des Lagerortes der neu eingelagerten Ware
    }

    public void auslagern(int fach){                                // Methode zum Auslagern von Waren
        if (!isValidesFach(fach)) return;               // return bei falschem Fach
        if (Objects.isNull(waren[fach-1])) return;      // return bei leerem Fach
        waren[fach-1] = null;                           // Ware wird aus dem waren Array geloescht
        anzahlBelegt--;                                 // anzahlBelegt wird entsprechend angepasst
    }

}