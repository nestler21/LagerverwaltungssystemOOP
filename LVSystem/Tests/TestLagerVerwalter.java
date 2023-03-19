package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Lager.LagerVerwalter;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFluessig;
import LVSystem.utils.Meldung;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Objects;

public class TestLagerVerwalter {

    LagerVerwalter lv = LagerVerwalter.getInstance();
    
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
    void test_reset(){
        lv.reset();
        assertEquals(0, lv.getLagerListe().size());
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertEquals(1, lv.getLagerListe().size());
        lv.reset();
        assertEquals(0, lv.getLagerListe().size());
    }

    @Test
    void test_lager_anlegen_korrekt(){
        lv.reset();
        Meldung meldung = lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertFalse(meldung.isFehler());
        assertTrue(Objects.isNull(meldung.getMessage()));

    }

    @Test
    void test_lager_anlegen_zu_viel(){
        lv.reset();
        for (int i = 0; i < 100; i++){
            lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        }
        Meldung meldung = lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertTrue(meldung.isFehler());
        assertEquals("Es koennen nur maximal 100 Lager angelegt werden.", meldung.getMessage());
    }

    @Test
    void test_lager_anlegen_zu_viele_Regale(){
        lv.reset();
        ArrayList<Integer> anzahl = new ArrayList<>();
        anzahl.add(50);
        anzahl.add(50);
        anzahl.add(50);
        anzahl.add(50);
        Meldung meldung = lv.lager_anlegen("name", anzahl, anzahl, "beschreibung");
        assertTrue(meldung.isFehler());
        assertEquals("Es koennen maximal 100 Regale angelegt werden.", meldung.getMessage());
    }
    
    @Test
    void test_lager_vorhanden(){
        lv.reset();
        assertFalse(lv.lager_vorhanden("name"));
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertTrue(lv.lager_vorhanden("name"));
    }

    @Test
    void test_lager_finden(){
        lv.reset();
        Lagerhalle lh = lv.lager_finden("name");
        assertFalse(lh instanceof Lagerhalle);
        assertTrue(Objects.isNull(lh));
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        lh = lv.lager_finden("name");
        assertTrue(lh instanceof Lagerhalle);
        assertFalse(Objects.isNull(lh));
    }

    @Test
    void test_getLagerListe_leer(){
        lv.reset();
        ArrayList<String> liste = lv.getLagerListe();
        assertEquals(0, liste.size());
    }

    @Test
    void test_getLagerListe_mit_infos(){
        lv.reset();
        lv.lager_anlegen("name1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        lv.lager_anlegen("name2", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        ArrayList<String> liste = lv.getLagerListe();
        assertEquals(2, liste.size());
        assertEquals("name1", liste.get(0));
        assertEquals("name2", liste.get(1));
    }

    @Test
    void test_isLagerMaximumErreicht(){
        lv.reset();
        for (int i = 0; i < 99; i++){
            lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        }
        assertEquals(99, lv.getLagerListe().size());
        assertFalse(lv.isLagerMaximumErreicht());
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        assertEquals(100, lv.getLagerListe().size());
        assertTrue(lv.isLagerMaximumErreicht());
    }

    @Test
    void test_checkEinlagern_nicht_gefunden(){
        lv.reset();
        Warentyp wt = new WarentypFluessig("name", 200, "beschreibung");
        Meldung meldung = lv.checkEinlagern(wt, 10, "name");
        assertTrue(meldung.isFehler());
        assertEquals("Lagerhalle nicht gefunden.", meldung.getMessage());
    }

    @Test
    void test_checkEinlagern_korrekt(){
        lv.reset();
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Meldung meldung = lv.checkEinlagern(wt, 10, "");
        assertFalse(meldung.isFehler());
        assertTrue(Objects.isNull(meldung.getMessage()));
    }

    @Test
    void test_checkEinlagern_keine_plaetze_ohne_lager(){
        lv.reset();
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        int frei = lv.lager_finden("name").getAnzahlFrei(wt);
        Meldung meldung = lv.checkEinlagern(wt, frei+1, "");
        assertTrue(meldung.isFehler());
        assertEquals("Nicht genuegend freie Plaetze.", meldung.getMessage());
        assertEquals(frei, meldung.getInhaltInt().get(0));
    }

    @Test
    void test_checkEinlagern_keine_plaetze_mit_lager(){
        lv.reset();
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        lv.lager_anlegen("name2", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        int frei = lv.lager_finden("name").getAnzahlFrei(wt);
        
        Meldung meldung = lv.checkEinlagern(wt, frei+1, "name");
        assertTrue(meldung.isFehler());
        assertEquals("Nicht genuegend freie Plaetze.", meldung.getMessage());
        assertEquals(frei, meldung.getInhaltInt().get(0));
    }

    @Test
    void test_einlagern_null(){
        lv.reset();
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        Ware ware = new Ware(wt, 1);
        Lagerort lo = lv.einlagern(ware);
        assertFalse(lo instanceof Lagerort);
        assertTrue(Objects.isNull(lo));
    }

    @Test
    void test_einlagern_korrekt(){
        lv.reset();
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        Ware ware = new Ware(wt, 1);
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Lagerort lo = lv.einlagern(ware);
        assertTrue(lo instanceof Lagerort);
        assertFalse(Objects.isNull(lo));
    }

    @Test
    void test_lager_loeschen_nicht_gefunden(){
        lv.reset();
        Meldung meldung = lv.lager_loeschen("name");
        assertTrue(meldung.isFehler());
        assertEquals("Lager nicht gefunden.", meldung.getMessage());
    }

    @Test
    void test_lager_loeschen_benutzt(){
        lv.reset();
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        Ware ware = new Ware(wt, 1);
        lv.einlagern(ware);
        Meldung meldung = lv.lager_loeschen("name");
        assertTrue(meldung.isFehler());
        assertEquals("Lager wird benutzt.", meldung.getMessage());
    }

    @Test
    void test_lager_loeschen_korrekt(){
        lv.reset();
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Meldung meldung = lv.lager_loeschen("name");
        assertFalse(meldung.isFehler());
        assertTrue(Objects.isNull(meldung.getMessage()));
    }

    @Test
    void test_getLagerDetails_nicht_vorhanden(){
        lv.reset();
        Meldung meldung = lv.getLagerDetails("name");
        assertTrue(meldung.isFehler());
        assertEquals("Lager nicht vorhanden.", meldung.getMessage());
    }

    @Test
    void test_getLagerDetails_korrekt(){
        lv.reset();
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Warentyp wt = new WarentypFluessig("name", 2000, "beschreibung");
        Ware ware = new Ware(wt, 1);
        lv.einlagern(ware);
        Lagerhalle lh = lv.lager_finden("name");

        Meldung meldung = lv.getLagerDetails("name");
        assertFalse(meldung.isFehler());
        assertTrue(Objects.isNull(meldung.getMessage()));
        assertEquals(lh.getName(), meldung.getInhaltStr().get(0));
        assertEquals(lh.getBeschreibung(), meldung.getInhaltStr().get(1));
        assertEquals(1, meldung.getInhaltInt().get(0));
    }

    @Test
    void test_getLagerortDetails_nicht_vorhanden(){
        lv.reset();
        Meldung meldung = lv.getLagerortDetails("name", 1, 1);
        assertTrue(meldung.isFehler());
        assertEquals("Lager nicht vorhanden.", meldung.getMessage());
    }

    @Test
    void test_getLagerortDetails_korrekt(){
        lv.reset();
        lv.lager_anlegen("name", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        Lagerhalle lh = lv.lager_finden("name");
        Meldung meldung = lv.getLagerortDetails("name", 1, 1);
        assertFalse(meldung.isFehler());
        Meldung meldung2 = lh.getLagerortDetails(1, 1);
        assertEquals(meldung2.getInhaltStr(), meldung.getInhaltStr());
        assertEquals(meldung2.getMessage(), meldung.getMessage());
    }

}
