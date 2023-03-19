package LVSystem.utils;

import java.util.ArrayList;

public class Meldung {

    private boolean fehler;
    private String message;
    private ArrayList<String> inhaltStr = new ArrayList<String>();
    private ArrayList<Integer> inhaltInt = new ArrayList<Integer>();
    private ArrayList<Float> inhaltFloat = new ArrayList<Float>();


    
    public Meldung(boolean fehler, String message){
        this.fehler = fehler;
        this.message = message;
    }

    public Meldung(boolean fehler, String message, ArrayList<String> inhaltStr, ArrayList<Integer> inhaltInt, ArrayList<Float> inhaltFloat){
        this.fehler = fehler;
        this.message = message;
        this.inhaltStr = inhaltStr;
        this.inhaltInt = inhaltInt;
        this.inhaltFloat = inhaltFloat;
    }

    public boolean isFehler(){
        return this.fehler;
    }

    public String getMessage(){
        return this.message;
    }

    public ArrayList<String> getInhaltStr(){
        return this.inhaltStr;
    }

    public ArrayList<Integer> getInhaltInt(){
        return this.inhaltInt;
    }

    public ArrayList<Float> getInhaltFloat(){
        return this.inhaltFloat;
    }
}
