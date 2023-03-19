package LVSystem.Main.Lager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.utils.Meldung;

public class LagerVerwalter implements Serializable{

    private static final int max = 100;

    ArrayList<Lagerhalle> lagerhallen = new ArrayList<Lagerhalle>();
    
    private static LagerVerwalter INSTANCE;
   
    private LagerVerwalter() {        
    }
    
    public static LagerVerwalter getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LagerVerwalter();
        }
        return INSTANCE;
    }

    public void laden(LagerVerwalter lv_neu){
        lagerhallen = lv_neu.lagerhallen;
    }

    public void reset(){
        lagerhallen = new ArrayList<Lagerhalle>();
    }


    public boolean isLagerMaximumErreicht(){
        if (lagerhallen.size() >= max){                 // Pruefen ob die zulaessige Gesamtzahl von 100 Lagerhallen bereits erreicht wurde
            return true;
        }
        return false;
    }

    public Meldung lager_anlegen(String name, ArrayList<Integer> anzahlRegalFest, ArrayList<Integer> anzahlRegalFluessig, String beschreibung){     // Methode zum Anlegen eines Lagers
        if (lagerhallen.size() >= max){                 // Pruefen ob die zulaessige Gesamtzahl von 100 Lagerhallen ueberschritten wird
            return new Meldung(true, "Es koennen nur maximal 100 Lager angelegt werden.");               // Fehlermeldung
        }

        int anzahlRegalGesamt =  0;                     // Gesamtzahl an Regalen wird ermittelt
        for(Integer n : anzahlRegalFest)
            anzahlRegalGesamt += n;
        for(Integer n : anzahlRegalFluessig)
            anzahlRegalGesamt += n;

        if (anzahlRegalGesamt <= 100){                  // Pruefen ob die zulaessige Gesamtzahl von 100 Regalen pro Lager eingehalten wird
            lagerhallen.add(new Lagerhalle(name, anzahlRegalFest, anzahlRegalFluessig, beschreibung));  // Anlegen und Abspeichern des neuen Lagerhallenobjekts in der lagerhallen ArrayList
            return new Meldung(false, null);                                                            // fehlerfreie Rueckmeldung
        }else{
            return new Meldung(true, "Es koennen maximal 100 Regale angelegt werden.");                  // Fehlermeldung
        }
    }

    public Meldung lager_loeschen(String lagername){                 // Methode zum Loeschen eines Lagers
        if (!lager_vorhanden(lagername)){
            return new Meldung(true, "Lager nicht gefunden.");                  // Fehlermeldung, wenn Lager nicht vorhanden
        }
        Lagerhalle lh = lager_finden(lagername);                                // Lagerhallen Objekt aus Name
        if (!lh.isLeer()){
            return new Meldung(true, "Lager wird benutzt.");                    // Fehlermeldung, wenn Waren im Lager liegen
        }

        Iterator<Lagerhalle> it = lagerhallen.iterator();           // Erstellen eines Iterators fuer die lagerhallen
        while(it.hasNext()){
            lh = it.next();                     
            if (lh.getName().equals(lagername)) {                   // ueberpruefung ob der eingegebene Name uebereinstimmt
                it.remove();                                        // Lagerhallen Objekt wird aus lagerhallen geloescht
                return new Meldung(false, null);                    // fehlerfreie Rueckmeldung
            }
        }
        return new Meldung(true, "unerwarteter Fehler");                // Fehlermeldung (wird nie erreicht, das bereits geprueft wurde, ob das Lager vorhanden ist)
    }

    public boolean lager_vorhanden(String lagername){               // Methode zum ueberpruefen, ob ein Lager vorhanden ist
        for(Lagerhalle lh : lagerhallen){
            if (lh.getName().equals(lagername)) {
                return true;                                        // Wenn das Lager gefunden wurde true zurueckgeben
            }
        }
        return false;                                               // Ansonsten false
    }

    public Lagerhalle lager_finden(String name){                    // Methode zum Erhalten eines Lagerhalle Objektes aus dem Namen
		for(Lagerhalle lh : lagerhallen){
            if (lh.getName().equals(name)) {                        // ueberpruefung ob der Eingegebene Name gefunden wurde
                return lh;                                          // Das entsprechende Objekt wird zurueckgegeben
            }
        }
        return null;                                                // Zurueckgeben von null wenn kein Lager mit dem angegebenen Namen gefunden wurde
    }

    public ArrayList<String> getLagerListe(){                       // Methode zum Erhalten einer Liste aller Lager
        ArrayList<String> namen = new ArrayList<String>();          // ArrayList namen erstellen
        for(Lagerhalle lh : lagerhallen){
            namen.add(lh.getName());                                // Namen der Lager zu namen hinzufuegen
        }
        return namen;        	                                    // Liste wird zurueckgegeben
    }

    public Meldung getLagerDetails(String lager){                   // Methode zum Erhalten von Details eines betimmten Lagers
        Lagerhalle lh = lager_finden(lager);
        if (Objects.isNull(lh)){
            return new Meldung(true, "Lager nicht vorhanden.");                  // Fehlermeldung, wenn Lager nicht vorhanden
        }
        ArrayList<Integer> inhaltInt = lh.getRegalInfo();
        inhaltInt.add(lh.getAnzahlBelegt());                                    // Integer ArrayList mit Regalanzahl und Anzahl eingelagerter Waren
        ArrayList<String> inhaltStr = new ArrayList<String>();
        inhaltStr.add(lh.getName());
        inhaltStr.add(lh.getBeschreibung());                                    // String ArrayList mit Name und Beschreibung
        return new Meldung(false, null, inhaltStr, inhaltInt, null);            // fehlerfreie Rueckmeldung mit Informationen
    }

    public Meldung checkEinlagern(Warentyp warentyp, int anzahl, String lager){
        int anzahlFrei = 0;

        if (lager.equals("")){
            for(Lagerhalle lh : lagerhallen){
                anzahlFrei += lh.getAnzahlFrei(warentyp);
            }
        }else{
            Lagerhalle lh = lager_finden(lager);
            if (Objects.isNull(lh)){
                return new Meldung(true, "Lagerhalle nicht gefunden.");            
            }else{
                anzahlFrei = lh.getAnzahlFrei(warentyp);
            }
        }

        if (anzahl <= anzahlFrei){
            return new Meldung(false, null);            
        }else{
            ArrayList<Integer> frei = new ArrayList<Integer>();
            frei.add(anzahlFrei);
            return new Meldung(true, "Nicht genuegend freie Plaetze.", null, frei, null);
        }
        
    }

    public Lagerort einlagern(Ware ware){                           // Methode zum Einlagern einer uebergebenen Ware in ein beliebiges Lager
        ArrayList<Lagerhalle> lh_liste = new ArrayList<>();
        for (Lagerhalle lh : lagerhallen) lh_liste.add(lh);
        int maxV;                                                   // maximaler Vergleichswert, der aus dem Vergleichen von Groessen resultieren kann

        if (ware.getWarentyp().getLagerart() == Lagerart.FEST){
            maxV = GroesseFest.values().length - 1;                 // maxV wird entsprechend der Groessen Enums gesetzt (TOOLARGE wird heraus gerechnet)
        }else{
            maxV = GroesseFluessig.values().length - 1;
        }

        for (int i = 0; i < maxV; i++){                                         // Es wird nach einem Lager gesucht, wo die Groesse des kleinst passenden freien Faches sich um i von der einzulagernden Ware unterscheidet
            for (Lagerhalle lh : lh_liste){                                     // laesst sich kein entsprechenden Lager finden wird i um eins erhoeht, bis i vMax-1 erreicht (bei 4 Groessen kann die groesste Differenz nur 3 sein)
                if (lh.getAnzahlFrei(ware.getWarentyp()) > 0 &&
                lh.vergleichZuKleinstpassendenFach(ware.getWarentyp()) == i){
                    return lh.einlagern(ware);
                }
            }
        }
        return null;                                                            // laesst sich generell kein Lager mit einem passenden Fach finden, wird null zurueckgegeben (kommt real nicht vor, dank checkEinlagern)
    }

    public Meldung getLagerortDetails(String lagerName, int regalID, int fachID){
        Lagerhalle lh = lager_finden(lagerName);
        if(Objects.isNull(lh)){
            return new Meldung(true, "Lager nicht vorhanden.");
        }
        return lh.getLagerortDetails(regalID, fachID);
    }


}
