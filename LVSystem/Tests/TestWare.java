package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Lager.LagerVerwalter;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFluessig;

class TestWare {

    String name = "WT1";
    int id = 1;
    Warentyp warentyp = new WarentypFluessig(name, 300, "Beschreibung");
    Date timestamp1 = new Date();
    Ware ware = new Ware(warentyp, id);
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
    void test_getID(){
        assertEquals(1, ware.getId());
    }
    
    @Test
    void test_getEingangszeitpunkt(){
        Date date = ware.getEingangszeitpunkt();
        assertTrue(date.compareTo(timestamp1) >= 0 && date.compareTo(timestamp2) <= 0);
    }

    @Test
    void test_getEingangszeitpunktStr(){
        String dateStr = ware.getEingangszeitpunktStr();
        assertTrue(dateStr.matches("[0-9]{2}/[0-9]{2}/[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}"));
    }

    @Test
    void test_getWarentyp(){
        assertEquals(warentyp, ware.getWarentyp());
    }

    @Test
    void test_getName(){
        String ware_name = name + "-" + id;
        assertEquals(ware_name, ware.getName());
    }

    @Test
    void test_getLagerort_null(){
        Ware ware = new Ware(warentyp, id);
        assertTrue(Objects.isNull(ware.getLagerort()));
    }

    @Test
    void test_einlagern_mit_Lagerhalle_ohne_lagerverwalter(){
        Ware ware = new Ware(warentyp, id);
        String name = "LH1";
        Lagerhalle lagerhalle = new Lagerhalle(name, anzahlRegalFest, anzahlRegalFluessig, "Beschreibung");
        LagerVerwalter lv = LagerVerwalter.getInstance();
        lv.reset();
        
        ware.einlagern(lagerhalle);
        Lagerort lo = ware.getLagerort();
        assertTrue(lo instanceof Lagerort);
        assertEquals(name, lo.getLagerhalle().getName());
    }

    @Test
    void test_einlagern_ohne_lagerhalle_mit_lagerverwalter(){
        Ware ware = new Ware(warentyp, id);
        LagerVerwalter lv = LagerVerwalter.getInstance();
        String name = "LH2";
        lv.reset();
        lv.lager_anlegen(name, anzahlRegalFest, anzahlRegalFluessig, "Beschreibung");

        ware.einlagern(null);
        Lagerort lo = ware.getLagerort();
        assertTrue(lo instanceof Lagerort);
        assertEquals(name, lo.getLagerhalle().getName());
    }

    @Test
    void test_einlagern_mit_Lagerhalle_mit_lagerverwalter(){
        Ware ware = new Ware(warentyp, id);
        LagerVerwalter lv = LagerVerwalter.getInstance();
        String name = "LH3";
        lv.reset();
        lv.lager_anlegen("anderer Name", anzahlRegalFest, anzahlRegalFluessig, "Beschreibung");
        Lagerhalle lagerhalle = new Lagerhalle(name, anzahlRegalFest, anzahlRegalFluessig, "Beschreibung");

        ware.einlagern(lagerhalle);
        Lagerort lo = ware.getLagerort();
        assertTrue(lo instanceof Lagerort);
        assertEquals(name, lo.getLagerhalle().getName());
    }

    @Test
    void test_einlagern_ohne_Lagerhalle_ohne_lagerverwalter(){
        Ware ware = new Ware(warentyp, id);
        LagerVerwalter lv = LagerVerwalter.getInstance();
        lv.reset();

        ware.einlagern(null);
        assertTrue(Objects.isNull(ware.getLagerort()));
    }

    
}
