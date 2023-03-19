package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFest;
import LVSystem.Main.Waren.WarentypFluessig;
import LVSystem.utils.Meldung;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class TestWarentyp {
    
    String name = "name";
    String beschreibung = "beschreibung";
    float hoehe = 10;
    float breite = 20;
    float tiefe = 30;
    Date timestamp1 = new Date();
    WarentypFest wtfe = new WarentypFest(name, hoehe, breite, tiefe, beschreibung);
    float volumen = 100;
    WarentypFluessig wtfl = new WarentypFluessig(name, volumen, beschreibung);
    Date timestamp2 = new Date();

    ArrayList<Integer> anzahlRegalFest = get_anzahl();
    ArrayList<Integer> anzahlRegalFluessig = get_anzahl();
    
    private ArrayList<Integer> get_anzahl(){
        ArrayList<Integer> anzahl = new ArrayList<>();
        anzahl.add(3);
        anzahl.add(3);
        anzahl.add(3);
        anzahl.add(3);
        return anzahl;
    }


    @Test
    void test_getName(){
        assertEquals(name, wtfe.getName());
        assertEquals(name, wtfl.getName());
    }

    @Test
    void test_getBeschreibung(){
        assertEquals(beschreibung, wtfl.getBeschreibung());
        assertEquals(beschreibung, wtfe.getBeschreibung());
    }

    @Test
    void test_getAnlagezeitpunkt(){
        Date date1 = wtfe.getAnlagezeitpunkt();
        Date date2 = wtfl.getAnlagezeitpunkt();
        assertTrue(date1.compareTo(timestamp1) >= 0 && date1.compareTo(timestamp2) <= 0);
        assertTrue(date2.compareTo(timestamp1) >= 0 && date2.compareTo(timestamp2) <= 0);
    }

    @Test
    void test_getEingangszeitpunktStr(){
        String dateStr1 = wtfe.getAnlagezeitpunktStr();
        String dateStr2 = wtfe.getAnlagezeitpunktStr();
        assertTrue(dateStr1.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}"));
        assertTrue(dateStr2.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}"));
    }

    @Test
    void test_getLagerart(){
        assertEquals(Lagerart.FEST, wtfe.getLagerart());
        assertEquals(Lagerart.FLUESSIG, wtfl.getLagerart());
    }

    @Test
    void test_getGroesse(){
        assertTrue(wtfe.getGroesse() instanceof GroesseFest);
        assertTrue(wtfl.getGroesse() instanceof GroesseFluessig);
    }

    @Test
    void test_getHoehe(){
        assertEquals(hoehe, wtfe.getHoehe());
    }

    @Test
    void test_getBreite(){
        assertEquals(breite, wtfe.getBreite());
    }

    @Test
    void test_getTiefe(){
        assertEquals(tiefe, wtfe.getTiefe());
    }

    @Test
    void test_getVolumen(){
        assertEquals(volumen, wtfl.getVolumen());
    }

    @Test
    void test_einlagern_ohne_lagerhalle(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Ware ware = wt.einlagern(null);
        assertTrue(ware instanceof Ware);
        assertEquals(1, ware.getId());
        assertEquals("name-1", ware.getName());
        assertEquals(wt, ware.getWarentyp());
    }

    @Test
    void test_einlagern_mit_lagerhalle(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Lagerhalle lh = new Lagerhalle("LH1", anzahlRegalFest, anzahlRegalFluessig, beschreibung);
        Ware ware = wt.einlagern(lh);
        assertTrue(ware instanceof Ware);
        assertEquals(1, ware.getId());
        assertEquals("name-1", ware.getName());
        assertEquals(wt, ware.getWarentyp());
        assertEquals(lh, ware.getLagerort().getLagerhalle());
    }

    @Test
    void test_getWare_null(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Ware ware = wt.getWare(5);
        assertTrue(Objects.isNull(ware));
    }

    @Test
    void test_getWare(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Ware ware = wt.einlagern(null);
        int id = ware.getId();
        Ware ware2 = wt.getWare(id);
        assertEquals(ware, ware2);
    }

    @Test
    void test_ware_auslagern(){
        Lagerhalle lh = new Lagerhalle("LH1", anzahlRegalFest, anzahlRegalFluessig, beschreibung);
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Ware ware = wt.einlagern(lh);
        Meldung meldung = wt.ware_auslagern(ware);
        assertFalse(meldung.isFehler());
        assertEquals(ware.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware.getLagerort().toString(), meldung.getInhaltStr().get(1));
    }

    @Test
    void test_ware_auslagern_falscher_warentyp(){
        Warentyp wt1 = new WarentypFluessig("name", 200, "beschreibung");
        Warentyp wt2 = new WarentypFluessig("name2", 400, "beschreibung2");
        Ware ware = wt2.einlagern(null);
        Meldung meldung = wt1.ware_auslagern(ware);
        assertTrue(meldung.isFehler());
        assertEquals("Ware hat einen falschen Warentyp", meldung.getMessage());
    } 

    @Test
    void test_ware_auslagern_getWare(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Ware ware = wt.einlagern(null);
        int id = ware.getId();
        Ware ware2 = wt.getWare(id);
        assertTrue(ware2 instanceof Ware);
        wt.ware_auslagern(ware);
        Ware ware3 = wt.getWare(id);
        assertTrue(Objects.isNull(ware3));
    }


    @Test
    void test_getWaren(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        assertEquals(0, wt.getWaren().size());
        Ware ware1 = wt.einlagern(null);
        assertEquals(ware1, wt.getWaren().get(0));
        Ware ware2 = wt.einlagern(null);
        assertEquals(ware2, wt.getWaren().get(1));
        wt.ware_auslagern(ware1);
        assertEquals(1, wt.getWaren().size());
    }

    @Test
    void test_getAnzahl(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        assertEquals(wt.getWaren().size(), wt.getAnzahl());
        Ware ware1 = wt.einlagern(null);
        assertEquals(wt.getWaren().size(), wt.getAnzahl());
        wt.einlagern(null);
        assertEquals(wt.getWaren().size(), wt.getAnzahl());
        wt.ware_auslagern(ware1);
        assertEquals(wt.getWaren().size(), wt.getAnzahl());
    }

    @Test
    void test_isBenutzt_unbenutzt1(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        assertFalse(wt.isBenutzt());
    }

    @Test
    void test_isBenutzt_benutzt(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        wt.einlagern(null);
        assertTrue(wt.isBenutzt());
    }

    @Test
    void test_isBenutzt_unbenutzt2(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Ware ware = wt.einlagern(null);
        wt.ware_auslagern(ware);
        assertFalse(wt.isBenutzt());
    }

    @Test
    void test_waren_auslagern_zu_viel(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        wt.einlagern(null);
        Meldung meldung = wt.waren_auslagern(2);
        assertTrue(meldung.isFehler());
        assertEquals("Anzahl zu gross.", meldung.getMessage());
    }

    @Test
    void test_waren_auslagern_exakt(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        wt.einlagern(null);
        wt.einlagern(null);
        wt.einlagern(null);
        Meldung meldung = wt.waren_auslagern(3);
        assertEquals(0, wt.getAnzahl());
        assertFalse(meldung.isFehler());
        assertEquals(6, meldung.getInhaltStr().size());
    }

    @Test
    void test_waren_auslagern_weniger(){
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        wt.einlagern(null);
        wt.einlagern(null);
        wt.einlagern(null);
        wt.einlagern(null);
        Meldung meldung = wt.waren_auslagern(2);
        assertEquals(2, wt.getAnzahl());
        assertFalse(meldung.isFehler());
        assertEquals(4, meldung.getInhaltStr().size());        
    }



}
