package LVSystem.Client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import LVSystem.Client.Daten.Datenverwalter;
import LVSystem.Client.Daten.Projekt;
import LVSystem.Main.SessionFacade;
import LVSystem.utils.Meldung;


public class ClientKonsole {

    static SessionFacade FACADE = SessionFacade.getInstance();      // Facade zum Zugriff auf die Business Logik
    static ArrayList<Projekt> PROJEKTE = new ArrayList<>();         // Alle gespeicherten Projekte

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Herzlich Willkommen im Lagerverwaltungssystem.");
        System.out.println("----------------------------------------------");

        projekte_laden();
        start_menue();
        System.out.println("Vielen Dank fuer die Benutzung!");
        System.exit(0);
    }

    private static void projekte_laden(){
        PROJEKTE = Datenverwalter.laden();
    }

    private static void start_menue() {
        while(true){
            PROJEKTE = Datenverwalter.laden();                      // ueberschreiben der PROJEKTE Liste mit dem gespeicherten Stand -> entkoppelung von Projekten und Singleton Instanz
            SessionFacade.reset();                                  // Zuruecksetzen der Singleton Instanz
            for (Projekt p : PROJEKTE) p.lock();                    // Alle Projekte sperren
            KTools.ueberschrift("Start Menue", 3);
            System.out.println("[1] Projekt laden");
            System.out.println("[2] Neues Projekt erstellen");
            System.out.println("[3] Projekt loeschen");
            System.out.println("[4] Programm beenden");
            int auswahl = KTools.eingabeAuswahl(4);
            System.out.println();
            
            switch(auswahl){
                case 1:
                    if(projekt_laden()){
                        haupt_menue();
                    }
                    break;
                case 2:
                    projekt_erstellen();
                    break;
                case 3:
                    projekt_loeschen();
                    break;
                default:
                    return;
            }
        }
    }

    private static boolean projekt_laden(){
        if (PROJEKTE.size() == 0){
            KTools.fehlermeldung("Keine Projekte vorhanden");
            return false;
        }
        KTools.ueberschrift("Projekt laden", 3);

        System.out.println("Projekte:");
        KTools.linieZeichnen(10);
        for (int i = 1; i <= PROJEKTE.size(); i++){
            Projekt p = PROJEKTE.get(i-1);
            System.out.println("[" + i + "] " + p.getName());
        }
        KTools.linieZeichnen(10);

        int index = KTools.eingabeAuswahl(PROJEKTE.size()) - 1;
        Projekt p = PROJEKTE.get(index);

        System.out.println("Auswahl: \"" + p.getName() + "\"");
        while(true){
            String passwort = KTools.eingabeStr("Passwort: ");
            p.unlock(passwort);
            if (p.isLocked()){
                KTools.fehlermeldung("Das eingegebene Passwort ist falsch.");
                if(!KTools.eingabeJaNein("Passwort erneut eingeben? ")){
                    return false;
                }
            }else{
                break;
            }
            
        }
        p.laden();            // Projektdaten werden in die Singletons geladen und sind somit zentral verfuegbar     -> Koppelung von Projekt und Singleton Instanz
        KTools.bestaetigung("Projekt \"" + p.getName() + "\" wurde geladen!");
        return true;
        
    }

    private static boolean projekt_erstellen(){
        String name;
        String passwort;

        KTools.ueberschrift("Projekt erstellen", 2);
        while(true){
            name = KTools.eingabeStr("Name: ");
            if(name.equals("")){
                KTools.fehlermeldung("Bitte geben Sie einen Namen ein.");
            }else if(projekt_vorhanden(name)){
                KTools.fehlermeldung("Ein Projekt mit diesem Namen existiert bereits");
                if(!KTools.eingabeJaNein("Einen anderen Namen angeben? ")){
                    return false;
                }
            }else{
                name = name.replace(" ", "_");
                break;
            }
        }

        while(true){
            passwort = KTools.eingabeStr("Passwort: ");
            if (passwort.equals("")){
                KTools.fehlermeldung("Bitte geben Sie ein Passwort ein.");
                continue;
            }
            if (passwort.contains(" ")){
                KTools.fehlermeldung("Das Passwort darf keine Leerzeichen beinhalten.");
                continue;
            }
            break;
        }

        PROJEKTE.add(new Projekt(name, passwort));
        Datenverwalter.speichern(PROJEKTE);
        KTools.bestaetigung("Projekt \"" + name + "\" wurde angelegt.");
        return true;
    }

    private static boolean projekt_vorhanden(String name){
        for (Projekt p : PROJEKTE){
            if (p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private static void projekt_loeschen(){
        if (PROJEKTE.size() == 0){
            KTools.fehlermeldung("Keine Projekte vorhanden");
            return;
        }
        KTools.ueberschrift("Projekt loeschen", 3);

        System.out.println("Projekte:");
        KTools.linieZeichnen(10);
        for (int i = 1; i <= PROJEKTE.size(); i++){
            Projekt p = PROJEKTE.get(i-1);
            System.out.println("[" + i + "] " + p.getName());
        }
        KTools.linieZeichnen(10);

        int index = KTools.eingabeAuswahl(PROJEKTE.size()) - 1;
        Projekt p = PROJEKTE.get(index);
        System.out.println("Auswahl: \"" + p.getName() + "\"");
        
        while(true){
            String passwort = KTools.eingabeStr("Passwort: ");
            p.unlock(passwort);
            if (p.isLocked()){
                KTools.fehlermeldung("Das eingegebene Passwort ist falsch.");
                if(!KTools.eingabeJaNein("Passwort erneut eingeben? ")){
                    return;
                }
            }else{
                break;
            }
        }
        if (KTools.eingabeJaNein("Sind Sie sicher, dass das Projekt \"" + p.getName() + "\" geloescht werden soll? ")){
            PROJEKTE.remove(index);
            Datenverwalter.speichern(PROJEKTE);
            KTools.bestaetigung("Projekt \"" + p.getName() + "\" wurde geloescht!");
        }
        return;
    }

    private static void haupt_menue() {
        while(true){
            KTools.ueberschrift("Hauptmenue", 4);
            System.out.println("[1] Bestand verwalten");
            System.out.println("[2] Warentypen verwalten");
            System.out.println("[3] Lager verwalten");
            System.out.println("[4] Speichern...");
            System.out.println("[5] Projekt verlassen");
            int auswahl = KTools.eingabeAuswahl(5);
            System.out.println();
            
            switch(auswahl){
                case 1:
                    bestand_verwalten();
                    break;
                case 2:
                    warentypen_verwalten();
                    break;
                case 3:
                    lager_verwalten();
                    break;
                case 4:
                    speicher_menue();
                    break;
                default:
                    projekt_verlassen();
                    return;
            }
        }
    }

    private static void projekt_verlassen() {
        if (KTools.eingabeJaNein("Moechten sie den aktuellen Stand speichern? ")){
            projekt_speichern();
        }
    }


    private static void bestand_verwalten() {                       // Menue: Bestand verwalten
        while(true){
            KTools.ueberschrift("Bestand verwalten", 2);
            System.out.println("[1] Waren einlagern");
            System.out.println("[2] Waren auslagern");
            System.out.println("[3] Ware anzeigen");
            System.out.println("[4] Lagerort anzeigen");
            System.out.println("[5] Zurueck");
            int auswahl = KTools.eingabeAuswahl(5);
            System.out.println("");

            switch(auswahl){
                case 1:
                    waren_einlagern();
                    break;
                case 2:
                    waren_auslagern();
                    break;
                case 3:
                    waren_detailansicht();
                    break;
                case 4:
                    lagerort_anzeigen();
                    break;
                default:
                    return;
            }
        }
    }

    private static void waren_einlagern() {                         // Methode zum Einlagern von Waren
        KTools.ueberschrift("Ware einlagern", 4);

        String warentyp;                                            // Variablendefinition
        int anzahl;
        String lager = "";

        warentyp = KTools.eingabeStr("Welchen Warentypen moechten Sie einlagern? ");     //Eingabe des Warentypen
        if (!FACADE.warentyp_vorhanden(warentyp)) {                                     // ueberpruefung ob Warentyp vorhanden
            KTools.fehlermeldung("Warentyp nicht vorhanden.");
            return;
        }

        anzahl =  KTools.eingabeInt("Wie viele Waren vom Typ \"" + warentyp + "\" moechten Sie einlagern? ");     // Abfrage der einzulagernden Anzahl

        boolean lager_angeben = KTools.eingabeJaNein("Bevorzugtes Lager angeben? ");    // Abfrage, ob ein bevorzugtes Lager angegeben werden soll

        while(lager_angeben){
            lager = KTools.eingabeStr("Lager: ");                   // Eingabe des Lagers
            if (!FACADE.lager_vorhanden(lager)){
                KTools.fehlermeldung("Lager nicht gefunden.");      // Fehlermeldung, wenn Lager nicht gefunden
                lager_angeben = KTools.eingabeJaNein("Anderes Lager angeben? ");        // lager_angeben wird genutzt um die Schleife zu steuern
            }else{
                break;                                              // Ausbruch aus der Schleife
            }
        }
        if (!lager_angeben) lager = "";                             // Lager Variable zuruecksetzen, falls kein Lager angegeben werden soll

        Meldung meldung = FACADE.einlagern(warentyp, anzahl, lager);    // Versuch die Waren einzulagern
        
        if (meldung.isFehler()){                                    // Handling von Fehlern
            if (!Objects.isNull(meldung.getInhaltInt())){           // Wenn inhaltInt bei einer Fehlermeldung etwas beeinhaltet soll der Fehler gesondert behandelt werden
                KTools.fehlermeldung(meldung.getMessage());         // Bei dieser Fehlermeldung wird versucht mehr Waren einzulagern als Platz ist
                int anzahlFrei = meldung.getInhaltInt().get(0);
                KTools.fehlermeldung("Fuer den angegebenen Warentyp (und Lager) sind nur " + anzahlFrei + " Plaetze frei.");
                boolean neu_einlagern = KTools.eingabeJaNein("Statt " + anzahl + " nur " + anzahlFrei + " Waren des Typs \"" + warentyp + "\" einlagern? ");                // Dem Nutzer wird die Moeglichkeit gegeben nur die freie Anzahl einzulagern
                if (neu_einlagern){
                    meldung = FACADE.einlagern(warentyp, anzahlFrei, lager);    // Erneuter Versuch Waren einzulagern, diesmal mit einer validen Anzahl
                }else{
                    return;         // wenn nicht: zurueck zum Menue
                }
            }else{
                KTools.fehlermeldung(meldung.getMessage());                    // Anzeigen der anderen Fehlermeldungen
                return;
            }
        }

        anzahl = meldung.getInhaltInt().get(0);                         // Entpacken des Inhalts der Meldung
        ArrayList<String> inhaltStr = meldung.getInhaltStr();

        KTools.bestaetigung("Es wurden " + anzahl + " Waren des Typs \"" + warentyp + "\" an den folgenden Lagerorten eingelagert:");    // Bestaetigung fuer den Nutzer
        waren_anzeigen(inhaltStr);          // Alle angelgten und eingelagerten Waren werden zusammen mit ihrem Lagerort aufgelistet
    }

    private static void waren_auslagern() {                         // Menue: Auslagern
        KTools.ueberschrift("Auslagern", 6);
        System.out.println("[1] Ware auslagern");
        System.out.println("[2] Warentyp auslagern");
        int auswahl = KTools.eingabeAuswahl(2);
        System.out.println("");
        
        switch(auswahl){
            case 1:
                ware_auslagern();
                break;
            case 2:
                warentyp_auslagern();
                break;
            default:
                return;
        }     
    }

    private static void ware_auslagern() {                          // Methode zum Auslagern einer bestimmten Ware
        KTools.ueberschrift("Ware auslagern", 4);

        Meldung meldung;
        String ware = KTools.eingabeStr("Welche Ware soll ausgelagert werden? ");   // Eingabe des Warennamens
        meldung = FACADE.ware_auslagern(ware);                          // Versuch die eingegebene Ware auszulagern
        if (meldung.isFehler()){                                        // Handling der Fehler
            KTools.fehlermeldung(meldung.getMessage());                 // Fehlermeldung fuer den Nutzer
        }else{
            KTools.bestaetigung("Folgende Ware wurde ausgelagert:");    // Bestaetigung fuer den Nutzer
            waren_anzeigen(meldung.getInhaltStr());                     // Alle ausgelagerten Waren werden zusammen mit ihrem ehemaligen Lagerort aufgelistet
        }
        
    }

    private static void warentyp_auslagern() {                      // Methode zum Auslagern einer bestimmten Anzahl eines Warentyps
        KTools.ueberschrift("Warentyp auslagern", 2);
        
        String warentyp;            // Variablendefinition

        warentyp = KTools.eingabeStr("Warentyp: ");  // Eingabe des Warentyps
        if (!FACADE.warentyp_vorhanden(warentyp)){
            KTools.fehlermeldung("Warentyp nicht vorhanden.");                      // Fehlermeldung, wenn der Warentyp nicht gefunden wurde
            return;
        }

        int anzahl =  KTools.eingabeInt("Anzahl: "); // Eingabe der auszulagernden Anzahl

        Meldung meldung = FACADE.warentyp_auslagern(warentyp, anzahl);          // Versuch die Waren auszulagern
        if (meldung.isFehler()){                                                // Handling von Fehlern
            if (!Objects.isNull(meldung.getInhaltInt())){                       // Wenn inhaltInt bei einer Fehlermeldung etwas beeinhaltet soll der Fehler gesondert behandelt werden
                KTools.fehlermeldung(meldung.getMessage());                            // Bei dieser Fehlermeldung wird versucht mehr Waren auszulagern als eingelagert sind
                int anzahlVorhanden = meldung.getInhaltInt().get(0);
                KTools.fehlermeldung("Von dem angegebenen Warentyp sind nur " + anzahlVorhanden + " Waren eingelagert.");
                boolean neu_auslagern = KTools.eingabeJaNein("Statt " + anzahl + " nur " + anzahlVorhanden + " Waren des Typs \"" + warentyp + "\" auslagern? ");                        // Dem Nutzer wird die Moeglichkeit gegeben nur die vorhandene Anzahl auszulagern
                if (neu_auslagern){
                    meldung = FACADE.warentyp_auslagern(warentyp, anzahlVorhanden);
                }else{
                    return;             // wenn nicht: zurueck zum Menue
                }
            }else{
                KTools.fehlermeldung(meldung.getMessage());                       // Anzeigen der anderen Fehlermeldungen
                return;
            } 
        }
        KTools.bestaetigung("Folgende Waren wurden ausgelagert:");       // Bestaetigung fuer den Nutzer
        waren_anzeigen(meldung.getInhaltStr());                         // Alle ausgelagerten Waren werden zusammen mit ihrem ehemaligen Lagerort aufgelistet
    }

    private static void waren_detailansicht(){                       // Methode zum Anzeigen von Details einer Ware
        KTools.ueberschrift("Details einer Ware", 5);

        String ware = KTools.eingabeStr("Ware: ");              // Eingabe der Ware

        Meldung meldung = FACADE.getWareDetails(ware);
        if (meldung.isFehler()){
            KTools.fehlermeldung(meldung.getMessage());                        // Fehlermeldung fuer den Nutzer
            return;
        }

        ArrayList<String> inhaltStr = meldung.getInhaltStr();           // Entpacken der Inhalte

        KTools.linieZeichnen(30);                                       // Printen der Inhalte
        System.out.println("Name: " + inhaltStr.get(0));
        System.out.println("Warentyp: " + inhaltStr.get(1) + " (" + inhaltStr.get(2) + ")");    // Warentyp mit Lagerart in Klammern
        System.out.println("Groesse: " + inhaltStr.get(3));
        System.out.println("Lagerort: " + inhaltStr.get(4));
        System.out.println("Eingangszeitpunkt: " + inhaltStr.get(5));
        KTools.linieZeichnen(30);
    }

    private static void lagerort_anzeigen(){
        KTools.ueberschrift("Details eines Lagerortes", 3);

        String lagerhalle = KTools.eingabeStr("Lager: ");
        if (!FACADE.lager_vorhanden(lagerhalle)){
            KTools.fehlermeldung("Lager nicht gefunden!");
            return;
        }

        int regalID = KTools.eingabeInt("Regal Nummer: ");
        int fachID = KTools.eingabeInt("Fach Nummer: ");
        Meldung meldung = FACADE.getLagerortDetails(lagerhalle, regalID, fachID);

        if (meldung.isFehler()){
            KTools.fehlermeldung(meldung.getMessage());
            return;
        }
        ArrayList<String> inhaltStr = meldung.getInhaltStr();
        
        KTools.linieZeichnen(20);
        System.out.println(inhaltStr.get(0));
        System.out.println("Lagerart: " + inhaltStr.get(1).toLowerCase());
        if (inhaltStr.size() == 4){
            System.out.println("Ware: " + inhaltStr.get(2));
            System.out.println("Eingangszeitpunkt: " + inhaltStr.get(3));
        }else{
            System.out.println("Ware: Zurzeit ist hier keine Ware eingelagert.");
        }
        KTools.linieZeichnen(20);
    }

    private static void waren_anzeigen(ArrayList<String> warenInfo){        // Methode zum Auflisten der uebergebene Inhalte (Name1, Lagerort1, Name2, Lagerort2, ...)
        for(int i = 0; i < warenInfo.size(); i+=2){
            System.out.println(warenInfo.get(i) + " | " + warenInfo.get(i+1));
        }
    } 


    private static void warentypen_verwalten() {                    // Menue: Warentypen verwalten
        while(true){
            KTools.ueberschrift("Warentypen verwalten", 2);
            System.out.println("[1] Warentyp anlegen");
            System.out.println("[2] Warentyp loeschen");
            System.out.println("[3] Listenansicht");
            System.out.println("[4] Detailansicht");
            System.out.println("[5] Zurueck");
            int auswahl = KTools.eingabeAuswahl(5);
            System.out.println("");

            switch(auswahl){
                case 1:
                    warentyp_anlegen();
                    break;
                case 2:
                    warentyp_loeschen();
                    break;
                case 3:
                    warentyp_listenansicht();
                    break;
                case 4:
                    warentyp_detailansicht();
                    break;
                default:
                    return;
            }
        } 
    }

    private static void warentyp_anlegen() {                        // Methode zum Anlegen von Warentypen
        KTools.ueberschrift("Warentyp anlegen", 2);
        
        String name;                                                // Variablendefinition
        String warenart;
        float volumen = 0;
        float hoehe = 0;
        float breite = 0;
        float tiefe = 0;
        String beschreibung;

        while (true) {                                              // Eingabe des Warentypnamens
            name = KTools.eingabeStr("Name: ");
            name.replace(" ", "_");
            if(name.equals("")){
                KTools.fehlermeldung("Bitte geben Sie einen Namen ein.");   // Fehlermeldung bei leerer Eingabe
            }else if (FACADE.warentyp_vorhanden(name)) {                  // ueberpruefung ob bereits ein Warentyp mit diesem Namen vorhanden ist
                KTools.fehlermeldung("Name bereits vorhanden.");           // Fehlermeldung + Erneute Eingabe eines Warentypnamens
            }else{
                if (name.contains("-")){
                    KTools.fehlermeldung("Name darf keinen Bindestrich (-) enthalten");    // Fehlermeldung + Erneute Eingabe eines Warentypnamens
                }else{
                    break;                                          // Wenn keine Fehlermeldung, Ausbruch aus der Schleife
                }
            }
        }
                                              
        System.out.println("[1] fest");
        System.out.println("[2] fluessig");            // Eingabe des der Warenart ueber eine Zahl
        int warenartInt = KTools.eingabeAuswahl(2);
        switch(warenartInt){
            case 1:
                warenart = "fest";
                hoehe =  KTools.eingabeFloat("Hoehe (cm): ");   // Abfrage der Hoehe/Breite/Tiefe bei festen Warentypen
                breite =  KTools.eingabeFloat("Breite (cm): ");
                tiefe =  KTools.eingabeFloat("Tiefe (cm): ");
                break;
            default:
                warenart = "fluessig";
                volumen =  KTools.eingabeFloat("Volumen (Liter): ");    // Abfrage des Volumens bei fluessigen Warentypen
                break;
        }

        beschreibung = KTools.eingabeStr("Beschreibung: ");     // Eingabe einer optionalen Beschreibung

        Meldung meldung = FACADE.warentyp_anlegen(name, warenart, volumen, hoehe, breite, tiefe, beschreibung);     // Versuch den Warentyp anzulegen
        if (meldung.isFehler()){
            KTools.fehlermeldung("Warentyp konnte nicht angelegt werden: " + meldung.getMessage());        // Fehlermeldung fuer den Nutzer
        }else{
            KTools.bestaetigung("Warentyp \"" + name + "\" wurde angelegt.");                        // Bestaetigung fuer den Nutzer
        }
    }

    private static void warentyp_loeschen() {                        // Methode zum Loeschen von Warentypen
        KTools.ueberschrift("Warentyp loeschen", 2);
               
        String name = KTools.eingabeStr("Warentyp: ");     // Abfrage des Warentypes 

        if (FACADE.warentyp_vorhanden(name)) {                              // ueberpruefung ob Warentyp vorhanden
            Meldung meldung = FACADE.warentyp_loeschen(name);                // Versuch den Warentyp zu loeschen
            if (meldung.isFehler()){
                KTools.fehlermeldung("Warentyp konnte nicht geloescht werden: " + meldung.getMessage());
            }else{
                KTools.bestaetigung("Warentyp \"" + name + "\" wurde geloescht.");
            }
        }else {
            KTools.fehlermeldung("Warentyp nicht vorhanden.");                     // Fehlermeldung, wenn Warentyp nicht vorhanden
        }

    }

    private static void warentyp_listenansicht() {                  // Methoden zum Auflisten aller Warentypen
        KTools.ueberschrift("Bestehende Warentypen", 2);
        
        ArrayList<String> namen = FACADE.getWarentypListe();        // Abrufen aller Namen
        if (namen.size() == 0) {
            KTools.fehlermeldung("Zurzeit gibt es keine Warentypen.");  // Fehlermeldung / Info, wenn keine Warentypen vorhanden
            return;
        }
        
        for(String name : namen){
            System.out.println("- " + name);                        // Ausgeben aller Namen
        }
        KTools.linieZeichnen(27);
    }

    private static void warentyp_detailansicht() {                  // Methode zum Anzeigen von Details eines Warentyps
        KTools.ueberschrift("Details eines Warentypen", 2);

        String warentyp = KTools.eingabeStr("Warentyp: ");          // Eingabe des Warentypen

        Meldung meldung = FACADE.getWarentypDetails(warentyp);
        if (meldung.isFehler()){
            KTools.fehlermeldung(meldung.getMessage());                        // Fehlermeldung fuer den Nutzer
            return;
        }

        ArrayList<String> inhaltStr = meldung.getInhaltStr();           // Entpacken der Inhalte
        ArrayList<Integer> inhaltInt = meldung.getInhaltInt();
        ArrayList<Float> inhaltFloat = meldung.getInhaltFloat();

        KTools.linieZeichnen(30);                                       // Printen der Inhalte
        System.out.println("Name: " + inhaltStr.get(0));
        System.out.println("Beschreibung: " + inhaltStr.get(1));
        System.out.println("Art: " + inhaltStr.get(2));
        if (inhaltStr.get(2).equals("fest")){                           // bei festen Warentypen werden Hoehe/Breite/Tiefe ausgegeben
            System.out.println("Hoehe: " + inhaltFloat.get(0) + "cm");
            System.out.println("Breite: " + inhaltFloat.get(1) + "cm");
            System.out.println("Tiefe: " + inhaltFloat.get(2) + "cm");
        }else{                                                          // bei fluessigen Warentypen wird das Volumen ausgegeben
            System.out.println("Volumen: " + inhaltFloat.get(0) + "l");
        }
        System.out.println("Groesse: " + inhaltStr.get(3));
        System.out.println("Anlagezeitpunkt: " + inhaltStr.get(4));
        System.out.println("Bestand: " + inhaltInt.get(0));
        KTools.linieZeichnen(30);
        if(inhaltInt.get(0) == 0){
            return;                                                     // keine Waren auflisten, wenn keine vorhanden
        }
        boolean waren_auflisten = KTools.eingabeJaNein(inhaltInt.get(0) + " Waren auflisten? ");        // Abfrage, ob der Nutzer die Waren auflisten moechte
        if (waren_auflisten){
            System.out.println("Waren:");
            KTools.linieZeichnen(30);
            meldung = FACADE.getWarenInfo(warentyp);                    // Abrufen aller Namen und Lagerorte
            if (meldung.isFehler()){
                KTools.fehlermeldung("Interner Fehler: Waren konnten nicht aufgelistet werden.");     // Fehlermeldung (sollte nicht ausgeloest werden koennen)
                return;
            }
            waren_anzeigen(meldung.getInhaltStr());                     // Aufruf augelagerte Funktion zum Auflisten der Informationen
            KTools.linieZeichnen(30);
        }
    }


    private static void lager_verwalten() {                         // Menue: Lager verwalten
        while(true){
            KTools.ueberschrift("Lager verwalten", 3);
            System.out.println("[1] Lager anlegen");
            System.out.println("[2] Lager loeschen");
            System.out.println("[3] Listenansicht");
            System.out.println("[4] Detailansicht");
            System.out.println("[5] Zurueck");
            int auswahl = KTools.eingabeAuswahl(5);
            System.out.println("");

            switch(auswahl){
                case 1:
                    lager_anlegen();
                    break;
                case 2:
                    lager_loeschen();
                    break;
                case 3:
                    lager_listenansicht();
                    break;
                case 4:
                    lager_detailansicht();
                    break;
                default:
                    return;
            }
        }
    }
    
    private static void lager_anlegen() {                           // Methode zum anlegen eines Lagers
        if(FACADE.isLagerMaximumErreicht()){                        // Fehlermeldung, wenn das Maximum an Lagerhallen erreicht ist
            KTools.fehlermeldung("Es koennen ncht mehr als 100 Lager angelegt werden!");
            KTools.fehlermeldung("Um ein neues Lager anzulegen, muss ein anderes geloescht werden.");
        }
        
        KTools.ueberschrift("Lager anlegen", 3);
        
        String name;                                                // Variablendefinition
        ArrayList<Integer> anzahlRegalFest = new ArrayList<Integer>();
        ArrayList<Integer> anzahlRegalFluessig = new ArrayList<Integer>();

        while(true){                                                // Eingabe des Lagernamen
            name = KTools.eingabeStr("Lagername: ");                               
            if(name.equals("")){
                KTools.fehlermeldung("Bitte geben Sie einen Namen ein.");
            }else if (FACADE.lager_vorhanden(name)) {                     // ueberpruefung ob der Lagername bereits vorhanden ist
                KTools.fehlermeldung("Name bereits vorhanden.");           // Erneute Eingabe eines Lagernamens
            }else{
                break;                                              // Wenn noch nicht vorhanden, Ausbruch aus der Schleife
            }
        }

        // Eingabe der Anzahl an Regalen fuer Feststoffe mit der jeweiligen Groesse
        anzahlRegalFest.add( KTools.eingabeInt("Wie viele Regale der Groesse S? "));
        anzahlRegalFest.add( KTools.eingabeInt("Wie viele Regale der Groesse M? "));
        anzahlRegalFest.add( KTools.eingabeInt("Wie viele Regale der Groesse L? "));
        anzahlRegalFest.add( KTools.eingabeInt("Wie viele Regale der Groesse XL? "));

        // Eingabe der Anzahl an Regalen fuer Fluessigkeiten mit der jeweiligen Groesse
        anzahlRegalFluessig.add( KTools.eingabeInt("Wie viele Tank-Regale der Groesse S? "));
        anzahlRegalFluessig.add( KTools.eingabeInt("Wie viele Tank-Regale der Groesse M? "));
        anzahlRegalFluessig.add( KTools.eingabeInt("Wie viele Tank-Regale der Groesse L? "));
        anzahlRegalFluessig.add( KTools.eingabeInt("Wie viele Tank-Regale der Groesse XL? "));

        String beschreibung = KTools.eingabeStr("Beschreibung: ");      // Eingabe einer optionalen Beschreibung

        Meldung meldung = FACADE.lager_anlegen(name, anzahlRegalFest, anzahlRegalFluessig, beschreibung);   // Versuch das Lager anzulegen
        if (meldung.isFehler()){
            KTools.fehlermeldung("Lager konnte nicht angelegt werden: " + meldung.getMessage());                   // Fehlermeldung fuer den Nutzer
        }else{
            KTools.bestaetigung("Lager \"" + name + "\" wurde angelegt.");                                   // Bestaetigung fuer den Nutzer
        }
    }

    private static void lager_loeschen() {                           // Methode zum loeschen eines Lagers
        KTools.ueberschrift("Lager loeschen", 3);
                
        String lager = KTools.eingabeStr("Welches Lager moechten Sie loeschen?: ");

        if (FACADE.lager_vorhanden(lager)) {                            // ueberpruefung ob Lager vorhanden
            Meldung meldung = FACADE.lager_loeschen(lager);              // Versuch das Lager zu loeschen
            if (meldung.isFehler()){
                KTools.fehlermeldung("Lager konnte nicht geloescht werden: " + meldung.getMessage());   // Fehlermeldung fuer den Nutzer
            }else{
                KTools.bestaetigung("Lager \"" + lager + "\" wurde geloescht.");                  // Bestaetigung fuer den Nutzer
            }
        }else {
            KTools.fehlermeldung("Lager \"" + lager + "\" nicht vorhanden.");                          // Fehlermeldung fuer den Nutzer
        }
    }

    private static void lager_listenansicht() {                      // Methoden zum Auflisten aler lager
        KTools.ueberschrift("Bestehende Lager", 4);

        ArrayList<String> namen = FACADE.getLagerListe();                   // Erhalten einer Liste aller Lagernamen
        if (namen.size() == 0) {
            KTools.fehlermeldung("Zurzeit gibt es keine Lager.");  // Fehlermeldung / Info, wenn keine Lager vorhanden
            return;
        }

        for(String name : namen){
            System.out.println("- " + name);                                // Bestehende Lager ausgeben
        }
        KTools.linieZeichnen(26);
    }

    private static void lager_detailansicht() {                      // Methode zum Anzeigen von Details eines Lagers
        KTools.ueberschrift("Details eines Lagers", 2);

        String lager = KTools.eingabeStr("Lager: ");                // Eingabe eines Lagers

        Meldung meldung = FACADE.getLagerDetails(lager);            // Versuch Details abzurufen
        if (meldung.isFehler()){
            KTools.fehlermeldung(meldung.getMessage());                    // Fehlermeldung fuer den Nutzer
        }else{
            ArrayList<String> inhaltStr = meldung.getInhaltStr();       // Entpacken der Informationen
            ArrayList<Integer> inhaltInt = meldung.getInhaltInt();
            KTools.linieZeichnen(30);                                   // Ausgabe aller Informationen
            System.out.println("Name: " + inhaltStr.get(0));
            System.out.println("Beschreibung: " + inhaltStr.get(1));
            System.out.println("Gelagerte Waren: " + inhaltInt.get(8));
            System.out.println("Anzahl Regale:");
            System.out.println("Regale  S: " + inhaltInt.get(0));
            System.out.println("Regale  M: " + inhaltInt.get(1));
            System.out.println("Regale  L: " + inhaltInt.get(2));
            System.out.println("Regale XL: " + inhaltInt.get(3));
            System.out.println("Tank-Regale  S: " + inhaltInt.get(4));
            System.out.println("Tank-Regale  M: " + inhaltInt.get(5));
            System.out.println("Tank-Regale  L: " + inhaltInt.get(6));
            System.out.println("Tank-Regale XL: " + inhaltInt.get(7));
            KTools.linieZeichnen(30);
        }
    }


    private static void speicher_menue() {
        KTools.ueberschrift("Speichermenue", 3);
        while(true){
            System.out.println("[1] Speichern");
            System.out.println("[2] Als neues Projekt speichern");
            System.out.println("[3] Zurueck");
            int auswahl = KTools.eingabeAuswahl(3);
            System.out.println();
            
            switch(auswahl){
                case 1:
                    if(projekt_speichern()){
                        return;
                    }
                    break;
                case 2:
                    if(projekt_speichern_als()){
                        return;
                    }
                    break;
                default:
                    return;
            }
        }
    }
    
    private static boolean projekt_speichern(){                     // Methode zum speichern des aktuellen Stands in der aktuellen Arbeitsdatei
        if(Datenverwalter.speichern(PROJEKTE)){
            KTools.bestaetigung("Projekt wurde gespeichert!");
            return true;
        }else{
            KTools.fehlermeldung("Interner Fehler: Projekt konnte nicht gespeichert werden.");
            KTools.fehlermeldung("Bitte Versuche es errneut");
            return false;
        }
    }

    private static boolean projekt_speichern_als(){
        ArrayList<Projekt> zwischenstand = PROJEKTE;
        PROJEKTE = Datenverwalter.laden();
        System.out.println("Bitte legen Sie ein neues Projekt an:");
        if(projekt_erstellen()){                                    // Beim Erstellen eines Projekts wird automatische die Singleton Instanz als Projekt Instanz geladen
            KTools.bestaetigung("Aktueller Stand wurde als neues Projekt gespeichert!");
            System.out.println("Projekt wird geoeffnet...");
            return true;
        }else{
            KTools.fehlermeldung("Es wurde kein neues Projekt angelegt und nicht gespeichert!");
            PROJEKTE = zwischenstand;                       
            return false;
        }
        
    }

    public static void backup_speichern(){
        PROJEKTE = Datenverwalter.laden();
        String pattern = "dd_MM_yyyy_HH_mm_ss";
        DateFormat df = new SimpleDateFormat(pattern);       
        String name = "Backup_" + df.format(new Date());
        String passwort = getRandomPasswort();
        PROJEKTE.add(new Projekt(name, passwort));
        System.out.println();
        KTools.fehlermeldung("Aktueller Stand wurde als \"" + name + "\" gespeichert.");
        KTools.fehlermeldung("Passwort: " + passwort);
    }

    private static String getRandomPasswort(){      // https://www.baeldung.com/java-random-string
        int leftLimit = 48; // 0
        int rightLimit = 122; // z
        int targetStringLength = 10;
        Random random = new Random();

        String passwort = random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))      // 
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

        return passwort;
    }


}
