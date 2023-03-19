package LVSystem.Main.Waren;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Lager.LagerVerwalter;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.utils.Meldung;

public class WarentypVerwalter implements Serializable{

    ArrayList<Warentyp> warentypen = new ArrayList<Warentyp>();
    static LagerVerwalter lagerVerwalter = LagerVerwalter.getInstance();

    private static WarentypVerwalter INSTANCE;
   
    private WarentypVerwalter() {        
    }
    
    public static WarentypVerwalter getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new WarentypVerwalter();
        }
        
        return INSTANCE;
    }

    public void laden(WarentypVerwalter wv_neu){
        warentypen = wv_neu.warentypen;
    }

    public void reset(){
        warentypen = new ArrayList<Warentyp>();
    }
	
	public Meldung warentyp_anlegen(String name,String warenart,float volumen,float hoehe,float breite,float tiefe, String beschreibung) {      // Methode zum Anlegen eines Warentyps
		if (this.warentyp_vorhanden(name)){
            return new Meldung(true, "Name bereits vorhanden.");                            // Fehlermeldung, wenn der Name bereits vorhanden ist
        }

        if (warenart.equals("fluessig")){
            if (GroesseFluessig.getGroesse(volumen) == GroesseFluessig.TOOLARGE){
                return new Meldung(true, "Volumen zu gross.");                               // Fehlermeldung, wenn das Volumen zu gross fuer alle moeglichen Tanks ist
            }
            warentypen.add(new WarentypFluessig(name, volumen, beschreibung));              // neuer WarenytpFluessig wird angelegt und zu warentypen hinzgefuegt
        }else{ 
            if (GroesseFest.getGroesse(hoehe, breite, tiefe) == GroesseFest.TOOLARGE){
                return new Meldung(true, "Masse zu gross.");                                  // Fehlermeldung, wenn die Masse zu gross fuer alle moeglichen Faecher sind
            }
            warentypen.add(new WarentypFest(name, hoehe, breite, tiefe, beschreibung));     // neuer WarenytpFest wird angelegt und zu warentypen hinzgefuegt
        }
        return new Meldung(false, null);                                                    // fehlerfreie Rueckmeldung
	}

    public boolean warentyp_vorhanden(String name){                 // Methode zum ueberpruefen, ob ein Warentyp vorhanden ist
        for (Warentyp wt : warentypen){
            if (wt.getName().equals(name)) {                        // ueberpruefung ob der Eingegebene Name gefunden wurde
                return true;                                        // return true, wenn ja
            }
        }
		return false;                                               // andernfalls return false
	}

    public Meldung warentyp_loeschen(String name) {                  // Methode Warentyp loeschen
        if (!warentyp_vorhanden(name)){
            return new Meldung(true, "Warentyp nicht vorhanden.");  // Fehlermeldung, wenn der Warentyp nicht vorhanden ist
        }

        Warentyp wt = getWarentyp(name);                        // Warentyp Objekt aus Name
        if (wt.isBenutzt()){
            return new Meldung(true, "Warentyp wird benutzt.");     // Fehlermeldung, wenn Waren von diesem Warentypen existieren
        }

        Iterator<Warentyp> it = warentypen.iterator();              // Erstellen eines Iterators fuer die warentypen
        while(it.hasNext()){
            wt = it.next();                     
            if (wt.getName().equals(name)) {                        // ueberpruefung ob der eingegebene Name uebereinstimmt
                it.remove();                                        // Warentyp Objekt wird aus warentypen geloescht
                return new Meldung(false, null);                    // fehlerfreie Rueckmeldung
            }
        }
        return new Meldung(true, "unerwarteter Fehler");    // Fehlermeldung (wird nie erreicht, da bereits geprueft wurde, ob das Lager vorhanden ist)
    }

	public Warentyp getWarentyp(String name) {                  // Methode zum Erhalten eines Warentyp Objektes aus dem Namen
        for(Warentyp wt : warentypen){
            if (wt.getName().equals(name)) {                        // ueberpruefung ob der Eingegebene Name gefunden wurde
                return wt;                                          // Das entsprechende Objekt wird zurueckgegeben
            }
        }
        return null;                                                // Zurueckgeben von null wenn kein Warentyp mit dem angegebenen Namen gefunden wurde
	}

    public ArrayList<String> getWarentypListe(){                    // Methode zum Erhalten einer Liste aller Warentypnamen
        ArrayList<String> namen = new ArrayList<String>();          // ArrayList namen erstellen
        for(Warentyp warentypen : warentypen){
			namen.add(warentypen.getName());                        // Namen der Warentpyen in ArrayList namen schreiben
        }
        return namen;        	                                    // ArrayList zurueckgeben
    }

    public Meldung getWarentypDetails(String name){                 // Methode zum Erhalten der Details eines Warentypen
        Warentyp wt = getWarentyp(name);                            // Objekt aus Name
        if (Objects.isNull(wt)){
            return new Meldung(true, "Warentyp nicht vorhanden.");  // Fehlermeldung wenn Warentyp nicht gefunden
        }
        ArrayList<String> inhaltStr = new ArrayList<>();            // Vorbereiten der Meldung-Komponenten
        ArrayList<Integer> inhaltInt = new ArrayList<>();
        ArrayList<Float> inhaltFloat = new ArrayList<>();
        inhaltStr.add(wt.getName());                                // Beschreiben der Komponenten mit Informationen
        inhaltStr.add(wt.getBeschreibung());
        inhaltStr.add(wt.getLagerart().toString().toLowerCase());
        inhaltStr.add(wt.getGroesse().toString());
        inhaltStr.add(wt.getAnlagezeitpunktStr());
        inhaltInt.add(wt.getAnzahl());
        if (wt.getLagerart() == Lagerart.FEST){                     // Angabe von Hoehe/Breite/Tiefe bei festen Warentypen
            WarentypFest wtFest = (WarentypFest) wt;                // Cast zu WarentypFest um Funktionen aufrufen zu koennen
            inhaltFloat.add(wtFest.getHoehe());
            inhaltFloat.add(wtFest.getBreite());
            inhaltFloat.add(wtFest.getTiefe());
        }else if (wt.getLagerart() == Lagerart.FLUESSIG){           // Angabe vom Volumen bei festen Warentypen
            WarentypFluessig wtFluessig = (WarentypFluessig) wt;    // Cast zu WarentypFluessig um Funktionen aufrufen zu koennen
            inhaltFloat.add(wtFluessig.getVolumen());
        }
        
        return new Meldung(false, null, inhaltStr, inhaltInt, inhaltFloat);     // fehlerfreie Rueckmeldung mit Informationen
    }

    public Meldung getWareDetails(String name){                     // Methode zum Erhalten der Details einer Ware
        Ware ware = ware_finden(name);
        if (Objects.isNull(ware)) return new Meldung(true, "Ware nicht gefunden.");     // Fehlermeldung, wenn die Ware nicht gefunden wurde
        
        ArrayList<String> inhaltStr = new ArrayList<>();            // Vorbereiten der Meldung-Komponenten
        inhaltStr.add(ware.getName());                              // Beschreiben der Komponenten mit Informationen
        inhaltStr.add(ware.getWarentyp().getName());
        inhaltStr.add(ware.getWarentyp().getLagerart().toString().toLowerCase());
        inhaltStr.add(ware.getWarentyp().getGroesse().toString());
        inhaltStr.add(ware.getLagerort().toString());
        inhaltStr.add(ware.getEingangszeitpunktStr());
        return new Meldung(false, null, inhaltStr, null, null);     // fehlerfreie Rueckmeldung mit Informationen
    }

    public Meldung getWarenInfo(String warentypname){               // Methode zum Erhalten der Namen und Lagerorte aller Waren eines Warentyps
        Warentyp warentyp = getWarentyp(warentypname);
        if (Objects.isNull(warentyp)){
            return new Meldung(true, "Warentyp nicht vorhanden");       // Fehlermeldung, wenn Warentyp nicht vorhanden
        }
        ArrayList<String> inhaltStr = new ArrayList<>();                // Anlage der Meldungs-Komponente inhaltStr
        for (Ware ware : warentyp.getWaren()){                          // Hinzufuegen aller Namen und Lagerorte
            inhaltStr.add(ware.getName());
            inhaltStr.add(ware.getLagerort().toString());
        }
        return new Meldung(false, null, inhaltStr, null, null);         // fehlerfreie Rueckmeldung
    }

    public Meldung einlagern(String warentypname, int anzahl, String lagername){    // Methode zum Einlagern von Waren
        Warentyp warentyp = getWarentyp(warentypname);
        if (Objects.isNull(warentyp)){
            return new Meldung(true, "Warentyp nicht gefunden.");                   // Fehlermeldung, wenn der Warentyp nicht gefunden wurde
        }
        Meldung meldung = lagerVerwalter.checkEinlagern(warentyp, anzahl, lagername);     // Check, ob die Waren mit den angegebenen Daten eingelagert werden koennen
        if (meldung.isFehler()){
            return meldung;         // Fehlermeldung, wenn der Check Fehlgeschlagen ist
        }

        Lagerhalle lager = lagerVerwalter.lager_finden(lagername);
        ArrayList<Ware> waren = new ArrayList<Ware>();                  // Anlegen einer Liste aller neuen Waren
        for (int i = 0; i < anzahl; i++){
           waren.add(warentyp.einlagern(lager));                        // Hinzufuegen der neu angelegten und eingelagerten Waren zur Liste
        }
        
        ArrayList<String> inhaltStr = new ArrayList<String>();          // Anlegen der Meldungs-Komponenten
        ArrayList<Integer> inhaltInt = new ArrayList<Integer>();
        for (Ware ware : waren){                                        // Befuellen der Meldungs-Komponenten
            inhaltStr.add(ware.getName());
            inhaltStr.add(ware.getLagerort().toString());
        }
        inhaltInt.add(anzahl);
        return new Meldung(false, "", inhaltStr, inhaltInt, null);      // fehlerfreie Rueckmeldung samt Namen und Lagerorte der Waren
    }

    public Ware ware_finden(String name){                           // Methode zum Erhalten einer Ware ueber ihren Namen
        int seperator = name.indexOf('-');                          // index des Bindestrichs wird festgestellt
        int id;
        String idStr = name.substring(seperator+1, name.length());  // Darueber werden die entsprechenden Substrings des Namen ermittelt
        try{
            id = Integer.parseInt(idStr);                           // sollte der ID Teil des namens wider Erwarten kein Integer sein, wird null zurueckgegeben
        }catch(NumberFormatException e){
            return null;
        }
        String warentypname = name.substring(0, seperator);
        Warentyp warentyp = getWarentyp(warentypname);          // ueber den warentypnamen wird der Warentyp ermittelt
        if (Objects.isNull(warentyp)){
            return null;                                            // sollte der Warentyp nicht gefunden werden, wird null zurueckgegeben
        }
        return warentyp.getWare(id);                                // ueber den warentyp wird die entsprechende Ware ermittelt und zurueckgegeben
    }

    public Meldung ware_auslagern(String warenname){                // Methode zum auslagern einer einzelnen Ware
        Ware ware = ware_finden(warenname);                                 // Objekt aus name
        if (Objects.isNull(ware)){
            return new Meldung(true, "Ware konnte nicht gefunden werden.");  // Fehlermeldung, wenn kein Objekt gefunden wurde
        }
        Warentyp warentyp = ware.getWarentyp();                      // Ermittlung des Warentypen der Ware
        return warentyp.ware_auslagern(ware);                        // Auslagern ueber den Warentyp + Rueckmeldung weiterreichen
    }
    
    public Meldung warentyp_auslagern(String warentypname, int anzahl){
        Warentyp warentyp = getWarentyp(warentypname);          // Objekt aus Name
        if (anzahl > warentyp.getAnzahl()){                         // Fehlermeldung wenn mehr Waren ausgelagert werden sollen, als vorhanden sind
            ArrayList<Integer> inhaltInt = new ArrayList<>();       // Anlegen der Meldungs-Komponente
            inhaltInt.add(warentyp.getAnzahl());                    // Befuellen der Meldungs-Komponente
            return new Meldung(true, "Nicht genuegend eingelagerte Waren.", null, inhaltInt, null);  // Fehlermeldung mit Anzahl an Waren als Inhalt
        }
        return warentyp.waren_auslagern(anzahl);                     // Auslagern ueber den Warentyp + Rueckmeldung weiterreichen
    }

}

