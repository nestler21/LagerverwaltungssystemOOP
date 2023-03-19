package LVSystem.Client.Daten;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Datenverwalter {

    private static String PFAD = Paths.get("").toAbsolutePath().toString() + "/LVSystem/Projektlager/";
    private static String DATEI = "projektLager";
    private static String ENDUNG = ".ser";

    
    public static boolean speichern(ArrayList<Projekt> data){       // Methode zum Abspeichern des SessionFacade Objekts unter einem bestimmten Namen
        String dateipfad = getOwnDateipfad();                       // Bestimmen des Dateipfads
        File ordner = new File(PFAD);
        if(!ordner.exists()){
            ordner.mkdirs();                                        // Ordner erstellen, wenn der Ordner nicht vorhanden ist (warum auch immer)
        }

        try{
            FileOutputStream fileOutputStream
                = new FileOutputStream(dateipfad);
            ObjectOutputStream objectOutputStream 
                = new ObjectOutputStream(fileOutputStream);         // Erstellen eines ObjectOutputStreams zum speichern des ArrayList Objekts
            objectOutputStream.writeObject(data);                   // Speichern der Projektliste
            objectOutputStream.flush();
            objectOutputStream.close();                             // Schliessen des ObjectOutputStreams
        }catch(IOException e){
            return false;                                           // Fehlermeldung wenn der Pfad nicht gefunden wurde oder Anderes
        }
        return true;                                                // fehlerfreie Rueckmeldung
    }

    public static ArrayList<Projekt> laden(){                       // Methode zum Laden einer bestimmten Version des SessionFacade-Objekts
        String dateipfad = getOwnDateipfad();                       // Bestimmen des Dateipfads
        try{
            FileInputStream fileInputStream
                = new FileInputStream(dateipfad);
            ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);           // Erstellen eines ObjectInputStream zum einlesen einer Serialisierungsdatei
            ArrayList<Projekt> liste = new ArrayList<>();           // Erstellen des ArrayList Objekts
            liste = (ArrayList<Projekt>) objectInputStream.readObject();     // Cast von dem eingelesenen Objekt auf ArrayList<Projekt>
            objectInputStream.close();                              // Schliessen des ObjectInputStream
            return liste;                                           // Rueckgabe der eingelesenen Liste
        }catch(ClassNotFoundException | IOException e){
            return new ArrayList<Projekt>();                        // Lere Liste, wenn von der Datei nicht gelesen werden konnte oder die Klasse nicht gefunden wurde
        }
    }

    private static String getOwnDateipfad(){                        // Methode zum Erhalten des vollstaendigen Dateipfads eines uebergebenen Namens
        return PFAD + DATEI + ENDUNG;                   // Pfad wird zusammengefuegt und zurueck gegeben
    }

}
