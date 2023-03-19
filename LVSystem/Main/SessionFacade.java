package LVSystem.Main;

import LVSystem.Main.Lager.LagerVerwalter;
import LVSystem.Main.Waren.WarentypVerwalter;
import LVSystem.utils.Meldung;

import java.io.Serializable;
import java.util.ArrayList;


public class SessionFacade implements Serializable{

    private static SessionFacade INSTANCE;
     
    private WarentypVerwalter warentypVerwalter;
    private LagerVerwalter lagerVerwalter; 
    
    private SessionFacade() {      
        warentypVerwalter = WarentypVerwalter.getInstance();
        lagerVerwalter = LagerVerwalter.getInstance();
    }
    
    public static SessionFacade getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new SessionFacade();
        }
        
        return INSTANCE;
    }

    public static void laden(SessionFacade facade_neu) {            // Methode zum laden der Daten einer anderen Instanz in die Singleton Instanz
        WarentypVerwalter.getInstance().laden(facade_neu.warentypVerwalter);
        LagerVerwalter.getInstance().laden(facade_neu.lagerVerwalter);
    }

    public static void reset(){                                     // Methode zum Zuruecksetzen der Verwalter (Loeschen aller Daten)
        WarentypVerwalter.getInstance().reset();
        LagerVerwalter.getInstance().reset();
    }
   
    
    public Meldung warentyp_anlegen(String name,String warenart,float volumen,float hoehe,float breite,float tiefe, String beschreibung) {
        return warentypVerwalter.warentyp_anlegen(name, warenart, volumen, hoehe, breite, tiefe, beschreibung);    
    }

    public Meldung warentyp_loeschen(String name) {
        return warentypVerwalter.warentyp_loeschen(name);
    }

    public boolean warentyp_vorhanden(String name){
        return warentypVerwalter.warentyp_vorhanden(name);
    }

    public ArrayList<String> getWarentypListe(){
        return warentypVerwalter.getWarentypListe();
    }

    public Meldung getWarentypDetails(String name){
        return warentypVerwalter.getWarentypDetails(name);
    }

    public Meldung getWarenInfo(String warentyp){
        return warentypVerwalter.getWarenInfo(warentyp);
    }
    
    public boolean isLagerMaximumErreicht(){
        return lagerVerwalter.isLagerMaximumErreicht();
    }

    public Meldung lager_anlegen(String lager, ArrayList<Integer> anzahlRegalFest, ArrayList<Integer> anzahlRegalFluessig, String beschreibung) {
        return lagerVerwalter.lager_anlegen(lager, anzahlRegalFest, anzahlRegalFluessig, beschreibung);
    }
    
    public Meldung lager_loeschen(String lager) {
        return lagerVerwalter.lager_loeschen(lager);
    }

    public boolean lager_vorhanden(String lager){
        return lagerVerwalter.lager_vorhanden(lager);
    }

    public ArrayList<String> getLagerListe(){
        return lagerVerwalter.getLagerListe();
    }

    public Meldung getLagerDetails(String lager){
        return lagerVerwalter.getLagerDetails(lager);
    }
    
    public Meldung einlagern(String warentyp, int anzahl, String lager) {
        return warentypVerwalter.einlagern(warentyp, anzahl, lager);
    }
    
    public Meldung ware_auslagern(String ware) {
        return warentypVerwalter.ware_auslagern(ware);
    }

    public Meldung warentyp_auslagern(String warentyp, int anzahl){
        return warentypVerwalter.warentyp_auslagern(warentyp, anzahl);
    }
    
    public Meldung getWareDetails(String ware){
        return warentypVerwalter.getWareDetails(ware);
    }

    public Meldung getLagerortDetails(String lagerhalle, int regalID, int fachID){
        return lagerVerwalter.getLagerortDetails(lagerhalle, regalID, fachID);
    }

}

