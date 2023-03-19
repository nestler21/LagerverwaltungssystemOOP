package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFest;
import LVSystem.Main.Waren.WarentypFluessig;
import LVSystem.utils.Meldung;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Objects;

public class TestLagerhalle {

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
    void test_getName(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertEquals("name", lh.getName());
    }
    
    @Test
    void test_getBeschreibung(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertEquals("beschreibung", lh.getBeschreibung());
    }

    @Test
    void test_getRegalInfo(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        ArrayList<Integer> result = lh.getRegalInfo();
        assertEquals(8, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
        assertEquals(4, result.get(3));
        assertEquals(5, result.get(4));
        assertEquals(6, result.get(5));
        assertEquals(7, result.get(6));
        assertEquals(8, result.get(7));
    }

    @Test
    void test_einlagern_korrekt(){
        Warentyp wt = new WarentypFluessig("wt1", 400, "beschreibung");
        Ware ware = new Ware(wt, 1);
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Lagerort lo = lh.einlagern(ware);
        assertTrue(lo instanceof Lagerort);
        assertFalse(Objects.isNull(lo));
        assertEquals(lh, lo.getLagerhalle());
    }

    @Test
    void test_getAnzahlFrei(){
        Warentyp wt = new WarentypFest("wt1", 100, 100, 100, "beschreibung");
        assertEquals(GroesseFest.EXTRALARGE, wt.getGroesse());
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertEquals(40, lh.getAnzahlFrei(wt));
        Ware ware = new Ware(wt, 1);
        for (int i = 0; i < 20; i++){
            lh.einlagern(ware);
        }
        assertEquals(20, lh.getAnzahlFrei(wt));
    }

    @Test
    void test_einlagern_fehler(){
        Warentyp wt = new WarentypFest("wt1", 10, 10, 10, "beschreibung");
        assertEquals(GroesseFest.SMALL, wt.getGroesse());
        Ware ware = new Ware(wt, 1);
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        int platz = lh.getAnzahlFrei(wt);
        for (int i = 0; i < platz; i++){
            lh.einlagern(ware);
        }
        assertEquals(0, lh.getAnzahlFrei(wt));
        Lagerort lo = lh.einlagern(ware);
        assertFalse(lo instanceof Lagerort);
        assertTrue(Objects.isNull(lo));
    }

    @Test
    void test_isLeer_ja(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertTrue(lh.isLeer());
    }

    @Test
    void test_isLeer_nein(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Warentyp wt = new WarentypFest("wt1", 10, 10, 10, "beschreibung");
        Ware ware = new Ware(wt, 1);
        lh.einlagern(ware);
        assertFalse(lh.isLeer());
    }

    @Test
    void test_getAnzahlBelegt(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Warentyp wt = new WarentypFest("wt1", 10, 10, 10, "beschreibung");
        Ware ware = new Ware(wt, 1);

        assertEquals(0, lh.getAnzahlBelegt());
        for (int i = 0; i < 20; i++){
            lh.einlagern(ware);
        }
        assertEquals(20, lh.getAnzahlBelegt());
    }

    @Test
    void test_getLagerortDetails_Regal_nicht_gefunden(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Meldung meldung = lh.getLagerortDetails(50, 5);
        assertTrue(meldung.isFehler());
        assertEquals("Regal 50 wurde nicht gefunden.", meldung.getMessage());
    }

    @Test
    void test_getLagerortDetails_Fach_nicht_gefunden(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Meldung meldung = lh.getLagerortDetails(1, 50);
        assertTrue(meldung.isFehler());
        assertEquals("Fach 50 wurde nicht gefunden.", meldung.getMessage());
    }

    @Test
    void test_getLagerortDetails_keine_Ware(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Meldung meldung = lh.getLagerortDetails(1, 1);
        assertFalse(meldung.isFehler());
        assertEquals("Keine Ware gelagert", meldung.getMessage());
        assertEquals("Lager: name - Regal: 1 - Fach: 1", meldung.getInhaltStr().get(0));
        assertEquals("FEST", meldung.getInhaltStr().get(1));
    }

    @Test
    void test_getLagerortDetails_Ware(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Warentyp wt = new WarentypFest("wt1", 10, 10, 10, "beschreibung");
        assertEquals(GroesseFest.SMALL, wt.getGroesse());
        Ware ware = new Ware(wt, 1);
        lh.einlagern(ware);
        Meldung meldung = lh.getLagerortDetails(1, 1);
        assertFalse(meldung.isFehler());
        assertEquals("Ware gelagert", meldung.getMessage());
        assertEquals("Lager: name - Regal: 1 - Fach: 1", meldung.getInhaltStr().get(0));
        assertEquals("FEST", meldung.getInhaltStr().get(1));
        assertEquals(ware.getName(), meldung.getInhaltStr().get(2));
        assertEquals(ware.getEingangszeitpunktStr(), meldung.getInhaltStr().get(3));
    }

    @Test
    void test_vergleichZuKleinstpassendenFach(){
        Lagerhalle lh = new Lagerhalle("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Warentyp wt = new WarentypFest("wt1", 10, 10, 10, "beschreibung");
        Ware ware = new Ware(wt, 1);
        assertEquals(GroesseFest.SMALL, wt.getGroesse());
        assertEquals(0, lh.vergleichZuKleinstpassendenFach(wt));
        int count = 10;
        for (int i = 0; i < count; i++){
            lh.einlagern(ware);
        }
        assertEquals(1, lh.vergleichZuKleinstpassendenFach(wt));
        count = 20;
        for (int i = 0; i < count; i++){
            lh.einlagern(ware);
        }
        assertEquals(2, lh.vergleichZuKleinstpassendenFach(wt));
        count = 30;
        for (int i = 0; i < count; i++){
            lh.einlagern(ware);
        }
        assertEquals(3, lh.vergleichZuKleinstpassendenFach(wt));
        count = 40;
        for (int i = 0; i < count; i++){
            lh.einlagern(ware);
        }
        assertEquals(-1, lh.vergleichZuKleinstpassendenFach(wt));
    }

}
