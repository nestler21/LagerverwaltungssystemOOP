package LVSystem.Main.Datentypen;

import java.util.Arrays;
import java.util.List;

public enum GroesseFest {
    SMALL, MEDIUM, LARGE, EXTRALARGE, TOOLARGE;

    //                    Abmessungen: z.B. (Hoehe, Breite, Tiefe)
    static final List<Float> s  = initialize(20, 30, 30);
    static final List<Float> m  = initialize(50, 60, 60);
    static final List<Float> l  = initialize(50, 100, 100);
    static final List<Float> xl = initialize(200, 150, 150);

    private static List<Float> initialize(float a, float b, float c){   // Methode zum initialisieren der Abmessungs-Konstanten
        List<Float> result = Arrays.asList(a, b, c);
        result.sort(null);
        return result;                                              // Gibt die Floats als sortiertes List-Objekt zurueck
    }

    public static GroesseFest getGroesse(float a, float b, float c){    // Methode zum Erhalten einer passenden Groesse zu den angegebenen Massen
        List<Float> ware = Arrays.asList(a, b, c);
        ware.sort(null);

        // Wenn alle uebergebenen Werte hoechstens so gross sind wie ihre korrespondierenden Werte der jeweiligen Groesse
        // wird die entsprechende Groesse zurueckgegeben, andernfalls wird die naechst groessere Groesse probiert
        // Wenn die uebergeben Werte zu keiner Groesse passen, wird TOOLARGE zurueckgegeben
        if ((ware.get(0) <= s.get(0)) && (ware.get(1) <= s.get(1)) && (ware.get(2) <= s.get(2))) return SMALL;
        else if ((ware.get(0) <= m.get(0)) && (ware.get(1) <= m.get(1)) && (ware.get(2) <= m.get(2))) return MEDIUM;
        else if ((ware.get(0) <= l.get(0)) && (ware.get(1) <= l.get(1)) && (ware.get(2) <= l.get(2))) return LARGE;
        else if ((ware.get(0) <= xl.get(0)) && (ware.get(1) <= xl.get(1)) && (ware.get(2) <= xl.get(2))) return EXTRALARGE;
        else return TOOLARGE;

    }
}
