package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Lager.Regal;
import LVSystem.Main.Lager.RegalFest;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class TestLagerort {
    
    ArrayList<Integer> anzahlRegalFest = get_anzahl(0);
    ArrayList<Integer> anzahlRegalFluessig = get_anzahl(4);

    private ArrayList<Integer> get_anzahl(int n){
        ArrayList<Integer> anzahl = new ArrayList<>();
        anzahl.add(1+n);
        anzahl.add(2+n);
        anzahl.add(3+n);
        anzahl.add(4+n);
        return anzahl;
    }

    @Test
    void test_toString(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Regal rg = new RegalFest(1, lh, GroesseFest.SMALL);
        Lagerort lo = new Lagerort(lh, rg, 1, Lagerart.FEST);

        assertEquals("Lager: name - Regal: 1 - Fach: 1", lo.toString());
    }

    @Test
    void test_getLagerhalle(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Regal rg = new RegalFest(1, lh, GroesseFest.SMALL);
        Lagerort lo = new Lagerort(lh, rg, 1, Lagerart.FEST);

        assertEquals(lh, lo.getLagerhalle());
    }

    @Test
    void test_auslagern(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Regal rg = new RegalFest(1, lh, GroesseFest.SMALL);
        Warentyp wt = new WarentypFest("name", 5, 5, 5, "beschreibung");
        Ware ware = new Ware(wt, 1);
        Lagerort lo = new Lagerort(lh, rg, 1, Lagerart.FEST);

        rg.einlagern(ware);
        assertEquals(1, rg.getAnzahlBelegt());
        lo.auslagern();
        assertEquals(0, rg.getAnzahlBelegt());
    }
}
