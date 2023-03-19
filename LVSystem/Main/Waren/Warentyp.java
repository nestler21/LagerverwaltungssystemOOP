package LVSystem.Main.Waren;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.utils.Meldung;



public abstract class Warentyp implements Serializable{

    private String name;
    private String beschreibung;
    private Date anlagezeitpunkt;
    private Lagerart lagerart;
    private int id_count = 1;

    private ArrayList<Ware> waren = new ArrayList<Ware>();

    public Warentyp(String name, String beschreibung, Lagerart lagerart){
        this.name = name;
        this.beschreibung = beschreibung;
        this.anlagezeitpunkt = new Date();
        this.lagerart = lagerart;
    }

    public String getName() {                               // Getter
        return name;
    }
    
    public String getBeschreibung() {                       // Getter
        return beschreibung;
    }
    
    public Date getAnlagezeitpunkt() {                      // Getter
        return anlagezeitpunkt;
    }

    public Lagerart getLagerart(){                          // Getter
        return lagerart;
    }

    public abstract <T> T getGroesse();                     // muss implementiert werden, kann aber nicht hier implementiert werden, da der Groessentyp nicht bekannt ist

    public String getAnlagezeitpunktStr(){                  // Methode zum Erhalten des Anlagezeitpunktes als String
        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);       
        return df.format(anlagezeitpunkt);
    }

    public ArrayList<Ware> getWaren(){                      // Getter
        return waren;
    }

    public int getAnzahl(){                                 // Methode zum Erhalten der Anzahl an Waren des Warentyps 
        return waren.size();
    }

    public boolean isBenutzt(){                             // Methode zum ueberpruefen, ob der Warentyp von einer Ware benutzt wird
        if (waren.size() == 0){
            return false;                                   // return false, wenn die waren ArrayList leer ist
        }else{
            return true;                                    // andernfalls return true
        }
    }

    public Ware einlagern(Lagerhalle lager){                // Methode zum Anlegen einer Ware und Einlagern in einem bestimmten lager
        int id = id_count;                                  // Bestimmen der ID
        id_count++;                                         // Erhoehen des ID Counts
        Ware ware = new Ware(this, id);                     // anlegen eines neuen Wareobjekts
        ware.einlagern(lager);                              // Einlagern der Ware in ein bestimmtes Lager
        waren.add(ware);                                    // Hinzufuegen der Ware zur ArrayList waren
        return ware;                                        // Rueckgabe der angelegten Ware
    }


    public Meldung ware_auslagern(Ware ware){                  // Methode zum Auslagern einer angegebenen Ware
        if (!ware.getWarentyp().equals(this)){
            return new Meldung(true, "Ware hat einen falschen Warentyp");
        }
        if (!Objects.isNull(ware.getLagerort())){           // Unter Umstaenden kann der Lagerort einer Ware null sein. Nur wenn das nicht der Fall ist, soll die Methode auslagern() des Lagerortes aufgerufen werden
            ware.getLagerort().auslagern();                 // Ware wird aus dem Regal Objekt entfernt
        }
        waren.remove(ware);                                 // Das Waren Objekt wird aus der waren ArrayList entfernt. Zu diesem Objekt existiert nun keine Referenz mehr.
        ArrayList<String> inhaltStr = new ArrayList<>();
        inhaltStr.add(ware.getName());
        if (Objects.isNull(ware.getLagerort())) inhaltStr.add("");
        else inhaltStr.add(ware.getLagerort().toString());
        return new Meldung(false, null, inhaltStr, null, null);
    }

    public Meldung waren_auslagern(int anzahl){             // Methode zum Auslagern einer bestimmten Anzahl an Waren
        if (anzahl > waren.size()){                         // Auslagern unterbinden, wenn mehr Waren ausgelagert werden sollen, als vorhanden sind
            return new Meldung(true, "Anzahl zu gross.");
        }
        ArrayList<String> inhaltStr = new ArrayList<>();
        Ware ware;
        for (int i = 0; i < anzahl; i++){                   // Naechster Schritt anzahl-mal:
            ware = waren.get(0);                            // Die erste Ware aus waren wird ausgewaehlt
            Meldung meldung = ware_auslagern(ware);         // und ausgelagert
            inhaltStr.addAll(meldung.getInhaltStr());                           
        }
        return new Meldung(false, null, inhaltStr, null, null);
    }

    public Ware getWare(int id){                            // Methode zum erhalten einer Ware ueber ihre ID
        for (Ware ware : waren){
            if (ware.getId() == id){
                return ware;                                // wenn die entsprechende Ware gefunden wurde, wird sie zurueckgegeben
            }
        }
        return null;                                        // andernfall null
    }
}
