package LVSystem.Main.Datentypen;

public enum GroesseFluessig {
    SMALL, MEDIUM, LARGE, EXTRALARGE, TOOLARGE;

    //Fuellmengen in Liter
    static final float s = 10;
    static final float m = 100;
    static final float l = 1000;
    static final float xl = 5000;

    public static GroesseFluessig getGroesse(float volumen){        // Methode zum Erhalten einer passenden Groesse zu dem angegebenen Volumen
        // Wenn das Volumen kleiner/gleich dem Volumen der jeweiligen Groesse ist,
        // wird die entsprechende Groesse zurueckgegeben, andernfalls wird die naechst groessere Groesse probiert
        // Wenn das uebergebene Volumen zu keiner Groesse passt, wird TOOLARGE zurueckgegeben
        if (volumen <= s) return SMALL;
        else if (volumen <= m) return MEDIUM;
        else if (volumen <= l) return LARGE;
        else if (volumen <= xl) return EXTRALARGE;
        else return TOOLARGE;
    }
}
