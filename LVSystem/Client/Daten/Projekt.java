package LVSystem.Client.Daten;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import LVSystem.Main.SessionFacade;

public class Projekt implements Serializable {

    private String name;
    private byte[] passwortHash;
    private SessionFacade facade;
    private boolean locked;

    public Projekt(String name, String passwort){
        this.name = name;
        this.passwortHash = hashPasswort(passwort);
        this.facade = SessionFacade.getInstance();
    }

    private byte[] hashPasswort(String passwort){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(passwort.getBytes(StandardCharsets.UTF_8));
        }catch(NoSuchAlgorithmException e){
        }
        return null;        
    }

    public String getName(){
        return this.name;
    }

    public boolean isLocked(){
        return locked;
    }

    public void unlock(String passwort){
        if (Arrays.equals(hashPasswort(passwort), passwortHash)){
            locked = false;                 // Projekt entsperren, wenn das Passwort stimmt
        }
    }

    public void lock(){
        locked = true;
    }

    public void laden(){
        if (isLocked()) return;                         // Nicht laden wenn das Projekt gesperrt ist
        SessionFacade.laden(this.facade);               // Die Facade des projekts in die Singleton Facade laden
        this.facade = SessionFacade.getInstance();      // Die Referenz der Projekt Facade auf die Singleton Facade setzen
    }
    

}
