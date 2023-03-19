package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Lager.LagerVerwalter;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFest;
import LVSystem.Main.Waren.WarentypFluessig;
import LVSystem.Main.Waren.WarentypVerwalter;
import LVSystem.utils.Meldung;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Objects;

public class TestWarentypVerwalter {

    WarentypVerwalter wtv = WarentypVerwalter.getInstance();
    LagerVerwalter lv = LagerVerwalter.getInstance();

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
    void test_reset(){
        wtv.reset();
        assertEquals(0, wtv.getWarentypListe().size());
        String name = "name";
        wtv.warentyp_anlegen(name, "fluessig", 200, 0, 0, 0, "beschreibung");
        assertEquals(1, wtv.getWarentypListe().size());
        wtv.reset();
        assertEquals(0, wtv.getWarentypListe().size());
    }

    @Test
    void test_warentyp_anlegen_volumen_zu_gross() {
        wtv.reset();
        float volumen = 5001;
        Meldung meldung = wtv.warentyp_anlegen("name", "fluessig", volumen, 0, 0, 0, "beschreibung");
        assertTrue(meldung.isFehler());
        assertEquals("Volumen zu gross.", meldung.getMessage());
    }

    @Test
    void test_warentyp_anlegen_masse_zu_gross() {
        wtv.reset();
        float hoehe = 2001;
        float breite = 151;
        float tiefe = 151;
        Meldung meldung = wtv.warentyp_anlegen("name", "fest", 0, hoehe, breite, tiefe, "beschreibung");
        assertTrue(meldung.isFehler());
        assertEquals("Masse zu gross.", meldung.getMessage());
    }

    @Test
    void test_warentyp_anlegen_kein_fehler(){
        wtv.reset();
        Meldung meldung = wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        assertFalse(meldung.isFehler());
        assertTrue(Objects.isNull(meldung.getMessage()));
    }

    @Test
    void test_warentyp_anlegen_warentyp_vorhanden(){
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        Meldung meldung = wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        assertTrue(meldung.isFehler());
        assertEquals("Name bereits vorhanden.", meldung.getMessage());
    }

    @Test
    void test_warentyp_vorhanden_nein(){
        wtv.reset();
        assertFalse(wtv.warentyp_vorhanden("name"));
    }

    @Test
    void test_warentyp_vorhanden_ja(){
        wtv.reset();
        String name = "name";
        Meldung meldung = wtv.warentyp_anlegen(name, "fluessig", 400, 0, 0, 0, "beschreibung");
        assertFalse(meldung.isFehler());
        assertTrue(wtv.warentyp_vorhanden(name));
    }

    @Test
    void test_getWarentyp_erfolgreich(){
        wtv.reset();
        String name = "name";
        String beschreibung = "beschreibung123";
        wtv.warentyp_anlegen(name, "fluessig", 400, 0, 0, 0, beschreibung);
        assertTrue(wtv.warentyp_vorhanden(name));
        Warentyp wt = wtv.getWarentyp(name);
        assertTrue(wt instanceof Warentyp);
        assertEquals(name, wt.getName());
        assertEquals(beschreibung, wt.getBeschreibung());
    }

    @Test
    void test_getWarentyp_fehler(){
        wtv.reset();
        String name = "name";
        assertFalse(wtv.warentyp_vorhanden(name));
        Warentyp wt = wtv.getWarentyp(name);
        assertTrue(Objects.isNull(wt));
    }

    @Test
    void test_warentyp_loeschen_nicht_vorhanden(){
        wtv.reset();
        String name = "name";
        assertFalse(wtv.warentyp_vorhanden(name));

        Meldung meldung = wtv.warentyp_loeschen(name);
        assertTrue(meldung.isFehler());
        assertEquals("Warentyp nicht vorhanden.", meldung.getMessage());
    }

    @Test
    void test_warentyp_loeschen_wird_benutzt(){
        wtv.reset();
        String name = "name";
        wtv.warentyp_anlegen(name, "fluessig", 400, 0, 0, 0, "beschreibung");
        Warentyp wt = wtv.getWarentyp(name);
        wt.einlagern(null);
        assertTrue(wt.isBenutzt());

        Meldung meldung = wtv.warentyp_loeschen(name);
        assertTrue(meldung.isFehler());
        assertEquals("Warentyp wird benutzt.", meldung.getMessage());
    }

    @Test
    void test_warentyp_loeschen_erfolgreich(){
        wtv.reset();
        String name = "name";
        wtv.warentyp_anlegen(name, "fluessig", 400, 0, 0, 0, "beschreibung");
        assertTrue(wtv.warentyp_vorhanden(name));
        Warentyp wt = wtv.getWarentyp(name);
        assertTrue(wt instanceof Warentyp);
        assertFalse(wt.isBenutzt());

        Meldung meldung = wtv.warentyp_loeschen(name);
        assertFalse(meldung.isFehler());
        assertTrue(Objects.isNull(meldung.getMessage()));
        assertFalse(wtv.warentyp_vorhanden(name));
    }

    @Test
    void test_getWarentypListe_leer(){
        wtv.reset();
        assertEquals(0, wtv.getWarentypListe().size());
    }
    
    @Test
    void test_getWarentypListe(){
        wtv.reset();
        ArrayList<String> namen = new ArrayList<>();
        namen.add("name1");
        namen.add("name2");
        namen.add("name3");
        wtv.warentyp_anlegen(namen.get(0), "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.warentyp_anlegen(namen.get(1), "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.warentyp_anlegen(namen.get(2), "fluessig", 400, 0, 0, 0, "beschreibung");
        ArrayList<String> namen_result = wtv.getWarentypListe();

        assertTrue(namen_result.equals(namen));
    }

    @Test
    void test_getWarentypDetails_fehler(){
        wtv.reset();
        Meldung meldung = wtv.getWarentypDetails("name");
        assertTrue(meldung.isFehler());
        assertEquals("Warentyp nicht vorhanden.", meldung.getMessage());
    }

    @Test
    void test_getWarentypDetails_korrekt(){
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        Warentyp wt = wtv.getWarentyp("name");
        Meldung meldung = wtv.getWarentypDetails("name");
        assertFalse(meldung.isFehler());
        assertEquals(wt.getName(), meldung.getInhaltStr().get(0));
        assertEquals(wt.getBeschreibung(), meldung.getInhaltStr().get(1));
        assertEquals(wt.getLagerart().toString().toLowerCase(), meldung.getInhaltStr().get(2));
        assertEquals(wt.getGroesse().toString(), meldung.getInhaltStr().get(3));
        assertEquals(wt.getAnlagezeitpunktStr(), meldung.getInhaltStr().get(4));
        assertEquals(wt.getAnzahl(), meldung.getInhaltInt().get(0));
    }

    @Test
    void test_getWarentypDetails_korrekt_fluessig(){
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        Warentyp wt = wtv.getWarentyp("name");
        WarentypFluessig wtF = (WarentypFluessig) wt;
        Meldung meldung = wtv.getWarentypDetails("name");
        assertFalse(meldung.isFehler());
        assertEquals(wtF.getVolumen(), meldung.getInhaltFloat().get(0));
    }

    @Test
    void test_getWarentypDetails_korrekt_fest(){
        wtv.reset();
        wtv.warentyp_anlegen("name", "fest", 0, 50, 50, 50, "beschreibung");
        Warentyp wt = wtv.getWarentyp("name");
        WarentypFest wtF = (WarentypFest) wt;
        Meldung meldung = wtv.getWarentypDetails("name");
        assertFalse(meldung.isFehler());
        assertEquals(wtF.getHoehe(), meldung.getInhaltFloat().get(0));
        assertEquals(wtF.getBreite(), meldung.getInhaltFloat().get(1));
        assertEquals(wtF.getTiefe(), meldung.getInhaltFloat().get(2));
    }
    
    @Test
    void test_einlagern_falscher_warentyp(){
        wtv.reset();
        Meldung meldung = wtv.einlagern("name", 1, "");
        assertTrue(meldung.isFehler());
        assertEquals("Warentyp nicht gefunden.", meldung.getMessage());
    }
    
    @Test
    void test_einlagern_check_fehlgeschlagen(){
        lv.reset();
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        Warentyp wt = wtv.getWarentyp("name");
        Meldung meldung = wtv.einlagern("name", 1, "");
        assertTrue(meldung.isFehler());
        Meldung meldung2 = lv.checkEinlagern(wt, 1, "");
        assertEquals(meldung2.isFehler(), meldung.isFehler());
        assertEquals(meldung2.getMessage(), meldung.getMessage());
        assertEquals(meldung2.getInhaltStr(), meldung.getInhaltStr());
        assertEquals(meldung2.getInhaltInt(), meldung.getInhaltInt());
        assertEquals(meldung2.getInhaltFloat(), meldung.getInhaltFloat());
    }

    @Test
    void test_einlagern_einzenln(){
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        Meldung meldung = wtv.einlagern("name", 1, "");
        Warentyp wt = wtv.getWarentyp("name");
        Ware ware = wt.getWare(1);
        assertFalse(meldung.isFehler());
        assertEquals(ware.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware.getLagerort().toString(), meldung.getInhaltStr().get(1));
        assertEquals(1, meldung.getInhaltInt().get(0));
    }

    @Test
    void test_einlagern_mehrere(){
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        Meldung meldung = wtv.einlagern("name", 5, "");
        assertFalse(meldung.isFehler());
        assertEquals(5, meldung.getInhaltInt().get(0));
        assertEquals(10, meldung.getInhaltStr().size());
    }

    @Test
    void test_ware_finden_fehler(){
        wtv.reset();
        Ware ware = wtv.ware_finden("name");
        assertTrue(Objects.isNull(ware));
        assertFalse(ware instanceof Ware);
    }

    @Test
    void test_ware_finden_korrekt(){
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 3, "");
        Ware ware = wtv.ware_finden("name-2");
        assertFalse(Objects.isNull(ware));
        assertTrue(ware instanceof Ware);
        assertEquals("name-2", ware.getName());
    }

    @Test
    void test_getWarenInfo_fehler(){
        wtv.reset();
        Meldung meldung = wtv.getWarenInfo("name");
        assertTrue(meldung.isFehler());
        assertEquals("Warentyp nicht vorhanden", meldung.getMessage());
    }

    @Test
    void test_getWarenInfo_einzeln(){
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 1, "");
        Ware ware = wtv.ware_finden("name-1");
        Meldung meldung = wtv.getWarenInfo("name");
        assertFalse(meldung.isFehler());
        assertEquals(ware.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware.getLagerort().toString(), meldung.getInhaltStr().get(1));
    }

    @Test
    void test_getWarenInfo_mehrere(){
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.reset();
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 2, "");
        Ware ware1 = wtv.ware_finden("name-1");
        Ware ware2 = wtv.ware_finden("name-2");
        Meldung meldung = wtv.getWarenInfo("name");
        assertFalse(meldung.isFehler());
        assertEquals(ware1.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware1.getLagerort().toString(), meldung.getInhaltStr().get(1));
        assertEquals(ware2.getName(), meldung.getInhaltStr().get(2));
        assertEquals(ware2.getLagerort().toString(), meldung.getInhaltStr().get(3));
    }
    
    @Test
    void test_getWareDetails_falsche_ware(){
        wtv.reset();
        Meldung meldung = wtv.getWareDetails("name");
        assertTrue(meldung.isFehler());
        assertEquals("Ware nicht gefunden.", meldung.getMessage());
    }

    @Test
    void test_getWareDetails_korrekt(){
        wtv.reset();
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 1, "");
        Ware ware = wtv.ware_finden("name-1");
        assertTrue(ware instanceof Ware);
        Meldung meldung = wtv.getWareDetails("name-1");
        assertFalse(meldung.isFehler());
        assertEquals(ware.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware.getWarentyp().getName(), meldung.getInhaltStr().get(1));
        assertEquals(ware.getWarentyp().getLagerart().toString().toLowerCase(), meldung.getInhaltStr().get(2));
        assertEquals(ware.getWarentyp().getGroesse().toString(), meldung.getInhaltStr().get(3));
        assertEquals(ware.getLagerort().toString(), meldung.getInhaltStr().get(4));
        assertEquals(ware.getEingangszeitpunktStr(), meldung.getInhaltStr().get(5));
    }

    @Test
    void test_ware_auslagern_falsche_ware(){
        wtv.reset();
        Meldung meldung = wtv.ware_auslagern("name");
        assertTrue(meldung.isFehler());
        assertEquals("Ware konnte nicht gefunden werden.", meldung.getMessage());
    }

    @Test
    void test_ware_auslagern_korrekt(){
        wtv.reset();
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 1, "");
        Ware ware = wtv.ware_finden("name-1");
        Meldung meldung = wtv.ware_auslagern("name-1");
        assertFalse(meldung.isFehler());
        assertEquals(ware.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware.getLagerort().toString(), meldung.getInhaltStr().get(1));
    }

    @Test
    void test_warentyp_auslagern_zu_viel(){
        wtv.reset();
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 2, "");
        Meldung meldung = wtv.warentyp_auslagern("name", 3);
        assertTrue(meldung.isFehler());
        assertEquals("Nicht genuegend eingelagerte Waren.", meldung.getMessage());
        assertEquals(2, meldung.getInhaltInt().get(0));
    }

    @Test
    void test_warentyp_auslagern_korrekt(){
        wtv.reset();
        lv.reset();
        lv.lager_anlegen("LH1", anzahlRegalFest, anzahlRegalFluessig, "beschreibung");
        wtv.warentyp_anlegen("name", "fluessig", 400, 0, 0, 0, "beschreibung");
        wtv.einlagern("name", 2, "");
        Ware ware1 = wtv.ware_finden("name-1");
        Ware ware2 = wtv.ware_finden("name-2");
        Meldung meldung = wtv.warentyp_auslagern("name", 2);
        assertFalse(meldung.isFehler());
        assertEquals(4, meldung.getInhaltStr().size());
        assertEquals(ware1.getName(), meldung.getInhaltStr().get(0));
        assertEquals(ware1.getLagerort().toString(), meldung.getInhaltStr().get(1));
        assertEquals(ware2.getName(), meldung.getInhaltStr().get(2));
        assertEquals(ware2.getLagerort().toString(), meldung.getInhaltStr().get(3));
    }

}
