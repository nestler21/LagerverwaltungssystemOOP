package LVSystem.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KTools {
    
    protected static String eingabeStr(String message){             // Methode zur Abfrage einer Konsoleneingabe
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));                      // reader-Objekt wird erstellt zum Einlesen aus der Konsole
        
        while(true) {
            System.out.print(message);
            try{
                String input = reader.readLine();                       // Eingabe einlesen
                if (input.matches("^$|[a-zA-Z0-9- _]+")){
                    return input;
                }
                fehlermeldung("Eingaben duerfen nur Alphanumerische Zeichen und [\"-\", \"_\", \" \"] enthalten.");    // Fehlermeldung bei Sonderzeichen, Umlauten etc.                
            }catch(IOException e){                                      // IOException wird abgefangen
                fehlermeldung("Systemfehler: " + e.getMessage());       // Dem Nutzer wird eine Fehlermeldung angezeigt
                ClientKonsole.backup_speichern();                       // Da bei einer IOException das Programm nicht weiter benutzt werden kann, wird ein Backup erstellt und das Programm geschlossen
                System.out.println("Programm wird beendet...");
                System.exit(0);
                return new String();                                    // Dieses return Statement wird nie erreicht, das kann java aber nicht wissen und will es deshalb.
            }
        }
    }

    protected static boolean eingabeJaNein(String message){         // Methode zur Abfrage von ja/nein Eingaben
        while(true){
            String input = eingabeStr(message);                     // Eingabe einlesen
            if (input.equals("ja")){
                return true;                                        // Bei Eingabe ja: gibt true zurueck
            }else if (input.equals("nein")){
                return false;                                       // Bei Eingabe nein: gibt false zurueck
            }
            else{
                fehlermeldung("Bitte geben Sie ja oder nein ein.");    // Programm verlaesst die Schleife erst bei erwarteter Eingabe
            }
        }
    }

    protected static float eingabeFloat(String message){            // Methode zur Abfrage von float Eingaben
        while (true){
            try {                                                   // Versuch die Eingabe in einen Float umzuwandeln
                float input = Float.parseFloat(eingabeStr(message).replace(",", "."));  // Kommas werden durch Punkte ersetzt um zu float konvertieren zu koennen
                if (input >= 0){
                    return input;
                }
                fehlermeldung("Bitte eine positive Zahl eingeben.");    // Fehlermeldung, wenn die Eingabe negativ ist
            }catch (NumberFormatException e) {                          // Fehlermeldung, wenn die Eingabe kein Float ist
                fehlermeldung("Bitte eine Zahl eingeben.");                       
            }    
        }
    }

    protected static int eingabeInt(String message){                // Methode zur Abfrage von int Eingaben
        while (true){
            try {                                                   // Versuch die Eingabe in einen Integer umzuwandeln
                int input = Integer.parseInt(eingabeStr(message));
                if (input >= 0){
                    return input;
                }
                fehlermeldung("Bitte eine positive Zahl eingeben.");    // Fehlermeldung, wenn die Eingabe negativ ist
            }catch (NumberFormatException e) {                          // Fehlermeldung, wenn die Eingabe kein Integer ist
                fehlermeldung("Bitte eine ganze Zahl eingeben.");                       
            }    
        }
    }

    protected static int eingabeAuswahl(int max){                     // Methode zur Abfrage von int Eingaben bis zu einer maximalen Zahl
        while(true){
            int input = eingabeInt("Auswahl: ");
            if (input <= max && input > 0){                         // Testen ob die Wingabe in den gewuenschten Zahlenraum faellt
                return input;
            }else{
                fehlermeldung("Bitte gib eine Zahl von 1 bis " + max + " an.");    // Falls die Eingabe nicht im Zahlenraum ist, Fehlermeldung schreiben
            }
        }
    }

    protected static void bestaetigung(String text){                  // Methode zum einheitlichen Ausgeben von Bestaetigungen
        System.out.println();
        // System.out.println("[OK] " + text);
        System.out.println("[   /]");
        System.out.println("[  / ]" + text);
        System.out.println("[\\/  ]");
        System.out.println();
    }

    protected static void fehlermeldung(String text){                 // Methode zum einheitlichen Ausgeben von Fehlern
        System.out.println();
        System.out.println("[!] " + text);
        System.out.println();
    }

    protected static void ueberschrift(String text, int abstand){
        int count = text.length() + 2*abstand + 2;
        System.out.println(new String(new char[count]).replace("\0", "-"));
        System.out.print("-" + new String(new char[abstand]).replace("\0", " "));
        System.out.print(text);
        System.out.println(new String(new char[abstand]).replace("\0", " ") + "-");
        System.out.println(new String(new char[count]).replace("\0", "-"));
    }

    protected static void linieZeichnen(int count){
        System.out.println(new String(new char[count]).replace("\0", "-"));
    }
    
}
