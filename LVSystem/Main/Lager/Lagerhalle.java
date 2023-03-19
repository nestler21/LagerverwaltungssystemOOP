package LVSystem.Main.Lager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.utils.Meldung;

public class Lagerhalle implements Serializable{

    String name;
    String beschreibung;
    ArrayList<Integer> anzahlRegalFest;
    ArrayList<Integer> anzahlRegalFluessig;
    ArrayList<Regal> regale = new ArrayList<Regal>();

    public Lagerhalle(String name, ArrayList<Integer> anzahlRegalFest, ArrayList<Integer> anzahlRegalFluessig, String beschreibung){
        this.name = name;
        this.beschreibung = beschreibung;
        addRegaleFest(anzahlRegalFest);
        addRegaleFluessig(anzahlRegalFluessig);
    }

    private void addRegaleFest(ArrayList<Integer> anzahl){        // Legt alle Regale fuer feste Stoffe entsprechend der Angaben an und speichert sie in die regale ArrayList ab
        if (anzahl.size() != GroesseFest.values().length-1){                    // Hat die uebergebene ArrayList eine falsche Groesse werden keine Regale angelegt (passiert im Normalfall nicht, sollte aber abgefangen werden)
            this.anzahlRegalFluessig = new ArrayList<Integer>();                //ebenfalls wird dann das Anzahl Attribut einheitlich auf 0 gesetzt
            for (int i = 0; i < GroesseFest.values().length-1; i++){
                this.anzahlRegalFest.add(0);
            }
            return;
        }
        this.anzahlRegalFest = anzahl;
        for (int i=0; i < GroesseFest.values().length-1; i++){                                  // Iteration ueber die verschiedenen Groessen
            for(int j=0; j < anzahl.get(i); j++){                                               // Iteration nach Anzahl der anzulegenden Regale
                regale.add(new RegalFest(regale.size()+1, this, GroesseFest.values()[i]));      // Regale werden angelegt
            }
        }
    }

    private void addRegaleFluessig(ArrayList<Integer> anzahl){          // Legt alle Regale fuer fluessige Stoffe entsprechend der Angaben an und speichert sie in die regale ArrayList ab
        if (anzahl.size() != GroesseFluessig.values().length-1){                // Hat die uebergebene ArrayList eine falsche Groesse werden keine Regale angelegt (passiert im Normalfall nicht, sollte aber abgefangen werden)
            this.anzahlRegalFluessig = new ArrayList<Integer>();                // ebenfalls wird dann das Anzahl Attribut einheitlich auf 0 gesetzt
            for (int i = 0; i < GroesseFest.values().length-1; i++){
                this.anzahlRegalFluessig.add(0);
            }
            return;
        }
        this.anzahlRegalFluessig = anzahl;
        for (int i=0; i < GroesseFluessig.values().length-1; i++){                                      // Iteration ueber die verschiedenen Groessen
            for(int j=0; j < anzahl.get(i); j++){                                                       // Iteration nach Anzahl der anzulegenden Regale
                regale.add(new RegalFluessig(regale.size()+1, this, GroesseFluessig.values()[i]));      // Regale werden angelegt
            }
        }
    }

    public String getName(){
        return name;
    }

    public String getBeschreibung() {
		return beschreibung;
	}

    public ArrayList<Integer> getRegalInfo(){                       // Fuegt alle Anzahlen der jeweiligen Regale zusammen und gibt sie zurueck
        ArrayList<Integer> inhaltInt = new ArrayList<Integer>();
        inhaltInt.addAll(anzahlRegalFest);
        inhaltInt.addAll(anzahlRegalFluessig);
        return inhaltInt;
    }

    public int getAnzahlBelegt(){
        int count = 0;
        for(Regal regal : regale){
            count += regal.getAnzahlBelegt();
        }
        return count;
    }

    public int getAnzahlFrei(Warentyp wt){                          // Methode zum Erhalten der Anzahl freier Lagerplaetze fuer einen bestimmten Warentyp
        int count = 0;                                              // Counter der mitzaehlt
        for(Regal regal : regale){
            if (regal.kannLagern(wt)){                              // Fuer jedes Regal wird geprueft, ob es den Warentyp lagern kann
                count += regal.getAnzahlFrei();                     // wenn ja, wird die Anzahl freier Plaetze zu count dazu addiert
            }
        }
        return count;        // Rueckgabe der ermittelten Anzahl
    }

    public boolean isLeer(){                                        // Methode zum ueberpruefen, ob die Lagerhalle leer ist
        for(Regal regal : regale){
            if (regal.getAnzahlBelegt() != 0){
                return false;                                       // false zurueckgeben, wenn eines der Lager belegt ist
            }
        }
        return true;                                                // andernfalls true
    }

    public int vergleichZuKleinstpassendenFach(Warentyp wt){        // Liefert den kleinsten Abstand aus den Abstaenden zwischen den Groessen der passenden Regale und der Groesse der Ware
        ArrayList<Integer> result = new ArrayList<>();
        for (Regal rg : regale){
            if (rg.kannLagern(wt)){
                int n = rg.vergleichGroesse(wt);
                if (n >= 0) result.add(n);
            }
        }
        if (result.size() == 0) return -1;
        result.sort(null);
        return result.get(0);
    }

    public Lagerort einlagern(Ware ware){                           // Methode zum Einlagern von Waren
        for(Regal regal : regale){
            if (regal.kannLagern(ware)){                            // ueberpruefen, ob das Regal die Ware einlagern kann und ob noch Platz ist
                return regal.einlagern(ware);                       // Ware einlagern und Lagerort der Ware zurueckgeben
            }
        }
        return null;                                                // wenn kein Lagerplatz gefunden werden konnte
    }

    public Meldung getLagerortDetails(int regalID, int fachID){
        if (this.regale.size() < regalID){
            return new Meldung(true, "Regal " + regalID + " wurde nicht gefunden.");
        }
        Regal rg = regale.get(regalID-1);
        if (Regal.getAnzahl() < fachID || fachID <= 0){
            String Regalart = rg.getLagerart().getRegalart();
            return new Meldung(true, Regalart + " " + fachID + " wurde nicht gefunden.");
        }


        Ware ware = rg.getWare(fachID);
        ArrayList<String> inhaltStr = new ArrayList<>();
        Lagerart lagerart = rg.getLagerart();
        Lagerort lagerort = new Lagerort(this, rg, fachID, lagerart);
        inhaltStr.add(lagerort.toString());
        inhaltStr.add(lagerart.toString());
        if (Objects.isNull(ware)){
            return new Meldung(false, "Keine Ware gelagert", inhaltStr, null, null);
        }
        inhaltStr.add(ware.getName());
        inhaltStr.add(ware.getEingangszeitpunktStr());
        return new Meldung(false, "Ware gelagert", inhaltStr, null, null);
    }

}
