package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.GroesseFluessig;
import LVSystem.Main.Datentypen.Lagerart;
import LVSystem.Main.Datentypen.Lagerort;
import LVSystem.Main.Lager.Lagerhalle;
import LVSystem.Main.Lager.Regal;
import LVSystem.Main.Lager.RegalFest;
import LVSystem.Main.Lager.RegalFluessig;
import LVSystem.Main.Waren.Ware;
import LVSystem.Main.Waren.Warentyp;
import LVSystem.Main.Waren.WarentypFest;
import LVSystem.Main.Waren.WarentypFluessig;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Objects;

class TestRegal {

    ArrayList<Integer> anzahlRegale = get_anzahl();
    Lagerhalle lh = new Lagerhalle("LH1", anzahlRegale, anzahlRegale, "beschreibung");
    RegalFest rgfe = new RegalFest(1, lh, GroesseFest.MEDIUM);
    RegalFluessig rgfl = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);

    private ArrayList<Integer> get_anzahl(){
        ArrayList<Integer> anzahl = new ArrayList<>();
        anzahl.add(3);
        anzahl.add(3);
        anzahl.add(3);
        anzahl.add(3);
        return anzahl;
    }


    @Test
    void test_getAnzahl(){
        assertEquals(10, Regal.getAnzahl());
    }

    @Test
    void test_getID(){
        assertEquals(1, rgfe.getID());
        assertEquals(1, rgfl.getID());
    }

    @Test
    void test_getLagerart(){
        assertEquals(Lagerart.FEST, rgfe.getLagerart());
        assertEquals(Lagerart.FLUESSIG, rgfl.getLagerart());
    }

    @Test
    void test_getGroesse(){
        assertEquals(GroesseFest.MEDIUM, rgfe.getGroesse());
        assertEquals(GroesseFluessig.MEDIUM, rgfl.getGroesse());
    }

    @Test
    void test_getGroesse2(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        assertEquals(GroesseFluessig.MEDIUM, regal.getGroesse());
    }

    @Test
    void test_einlagern_passt(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        Lagerort lo = regal.einlagern(ware);
        assertTrue(lo instanceof Lagerort);
        assertEquals("Lager: LH1 - Regal: 1 - Tank: 1", lo.toString());
    }

    @Test
    void test_einlagern_kein_platz(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        for (int i = 0; i <= Regal.getAnzahl(); i++){
            regal.einlagern(ware);
        }
        Lagerort lo = regal.einlagern(ware);
        assertTrue(Objects.isNull(lo));
        assertFalse(lo instanceof Lagerort);
    }

    @Test
    void test_kannLagern_richtige_groesse(){
        Warentyp wtfe = new WarentypFest("name", 10, 10, 10, "beschreibung");
        assertEquals(GroesseFest.SMALL, wtfe.getGroesse());
        assertEquals(GroesseFest.MEDIUM, rgfe.getGroesse());
        assertTrue(rgfe.kannLagern(wtfe));

        Warentyp wtfl = new WarentypFluessig("name", 10, "beschreibung");
        assertEquals(GroesseFluessig.SMALL, wtfl.getGroesse());
        assertEquals(GroesseFluessig.MEDIUM, rgfl.getGroesse());
        assertTrue(rgfl.kannLagern(wtfl));
    }

    @Test
    void test_kannLagern_falsche_groesse(){
        Warentyp wtfe = new WarentypFest("name", 100, 100, 100, "beschreibung");
        assertEquals(GroesseFest.EXTRALARGE, wtfe.getGroesse());
        assertEquals(GroesseFest.MEDIUM, rgfe.getGroesse());
        assertFalse(rgfe.kannLagern(wtfe));

        Warentyp wtfl = new WarentypFluessig("name", 2000, "beschreibung");
        assertEquals(GroesseFluessig.EXTRALARGE, wtfl.getGroesse());
        assertEquals(GroesseFluessig.MEDIUM, rgfl.getGroesse());
        assertFalse(rgfl.kannLagern(wtfl));
    }

    @Test
    void test_kannLagern_falscher_Typ(){
        Warentyp wtfe = new WarentypFest("name", 10, 10, 10, "beschreibung");
        assertEquals(GroesseFest.SMALL, wtfe.getGroesse());
        assertEquals(GroesseFluessig.MEDIUM, rgfl.getGroesse());
        assertFalse(rgfl.kannLagern(wtfe));

        Warentyp wtfl = new WarentypFluessig("name", 10, "beschreibung");
        assertEquals(GroesseFluessig.SMALL, wtfl.getGroesse());
        assertEquals(GroesseFest.MEDIUM, rgfe.getGroesse());
        assertFalse(rgfe.kannLagern(wtfl));
    }

    @Test
    void test_kannLagern_kein_platz(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Warentyp wt = new WarentypFluessig("name", 10, "beschreibung");
        Ware ware = new Ware(wt, 1);
        for (int i = 0; i <= Regal.getAnzahl(); i++){
            regal.einlagern(ware);
        }
        assertEquals(0, regal.getAnzahlFrei());
        assertFalse(regal.kannLagern(ware));
    }

    @Test
    void test_getWare_zu_gross(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = regal.getWare(50);
        assertTrue(Objects.isNull(ware));
        assertFalse(ware instanceof Ware);
    }

    @Test
    void test_getWare_nicht_vorhanden(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = regal.getWare(5);
        assertTrue(Objects.isNull(ware));
        assertFalse(ware instanceof Ware);
    }

    @Test
    void test_getWare_korrekt(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware1 = new Ware(null, 1);
        regal.einlagern(ware1);
        Ware ware2 = regal.getWare(1);
        assertEquals(ware1, ware2);
    }

    @Test
    void test_getAnzahlFrei(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        assertEquals(Regal.getAnzahl(), regal.getAnzahlFrei());
        regal.einlagern(ware);
        regal.einlagern(ware);
        assertEquals(Regal.getAnzahl()-2, regal.getAnzahlFrei());
    }

    @Test
    void test_getAnzahlBelegt(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        assertEquals(0, regal.getAnzahlBelegt());
        regal.einlagern(ware);
        regal.einlagern(ware);
        assertEquals(2, regal.getAnzahlBelegt());
    }

    @Test
    void test_auslagern_korrekt(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        regal.einlagern(ware);
        regal.einlagern(ware);
        assertTrue(regal.getWare(1) instanceof Ware);
        assertEquals(2, regal.getAnzahlBelegt());
        regal.auslagern(1);
        assertTrue(Objects.isNull(regal.getWare(1)));
        assertFalse(regal.getWare(1) instanceof Ware);
        assertEquals(1, regal.getAnzahlBelegt());
    }

    @Test
    void test_auslagern_zu_grosses_fach(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        regal.einlagern(ware);
        regal.auslagern(50);
        assertEquals(1, regal.getAnzahlBelegt());
    }

    @Test
    void test_auslagern_nicht_vorhanden(){
        Regal regal = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Ware ware = new Ware(null, 1);
        regal.einlagern(ware);
        regal.auslagern(5);
        assertEquals(1, regal.getAnzahlBelegt());
    }

    @Test
    void test_vergleichGroesse(){
        Regal rgfl = new RegalFluessig(1, lh, GroesseFluessig.MEDIUM);
        Warentyp wtflSmall = new WarentypFluessig("name", 5, "beschreibung");
        assertEquals(GroesseFluessig.SMALL, wtflSmall.getGroesse());
        Warentyp wtflMedium = new WarentypFluessig("name", 50, "beschreibung");
        assertEquals(GroesseFluessig.MEDIUM, wtflMedium.getGroesse());
        Warentyp wtflLarge = new WarentypFluessig("name", 500, "beschreibung");
        assertEquals(GroesseFluessig.LARGE, wtflLarge.getGroesse());
        Warentyp wtflExtralarge = new WarentypFluessig("name", 2000, "beschreibung");
        assertEquals(GroesseFluessig.EXTRALARGE, wtflExtralarge.getGroesse());
        assertEquals(1, rgfl.vergleichGroesse(wtflSmall));
        assertEquals(0, rgfl.vergleichGroesse(wtflMedium));
        assertEquals(-1, rgfl.vergleichGroesse(wtflLarge));
        assertEquals(-2, rgfl.vergleichGroesse(wtflExtralarge));

        Regal rgfe = new RegalFest(1, lh, GroesseFest.MEDIUM);
        Warentyp wtfeSmall = new WarentypFest("name", 10, 10, 10, "beschreibung");
        assertEquals(GroesseFest.SMALL, wtfeSmall.getGroesse());
        Warentyp wtfeMedium = new WarentypFest("name", 40, 40, 40, "beschreibung");
        assertEquals(GroesseFest.MEDIUM, wtfeMedium.getGroesse());
        Warentyp wtfeLarge = new WarentypFest("name", 40, 80, 80, "beschreibung");
        assertEquals(GroesseFest.LARGE, wtfeLarge.getGroesse());
        Warentyp wtfeExtralarge = new WarentypFest("name", 100, 100, 100, "beschreibung");
        assertEquals(GroesseFest.EXTRALARGE, wtfeExtralarge.getGroesse());
        assertEquals(1, rgfe.vergleichGroesse(wtfeSmall));
        assertEquals(0, rgfe.vergleichGroesse(wtfeMedium));
        assertEquals(-1, rgfe.vergleichGroesse(wtfeLarge));
        assertEquals(-2, rgfe.vergleichGroesse(wtfeExtralarge));
    }

}