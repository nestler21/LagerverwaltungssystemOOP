package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.GroesseFest;
import LVSystem.Main.Datentypen.GroesseFluessig;

import static org.junit.jupiter.api.Assertions.*;

public class TestGroesseEnums {
    
    @Test
    void test_getGroesse_fest_small1(){
        float a = 20;
        float b = 30;
        float c = 30;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.SMALL, gr);
    }

    @Test
    void test_getGroesse_fest_small2(){
        float a = 30;
        float b = 30;
        float c = 20;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.SMALL, gr);
    }

    @Test
    void test_getGroesse_fest_medium(){
        float a = 55;
        float b = 48;
        float c = 28;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.MEDIUM, gr);
    }
    
    @Test
    void test_getGroesse_fest_large(){
        float a = 80;
        float b = 100;
        float c = 28;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.LARGE, gr);
    }

    @Test
    void test_getGroesse_fest_extralarge1(){
        float a = 100.5f;
        float b = 48;
        float c = 28;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.EXTRALARGE, gr);
    }

    @Test
    void test_getGroesse_fest_extralarge2(){
        float a = 100;
        float b = 100;
        float c = 100;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.EXTRALARGE, gr);
    }

    @Test
    void test_getGroesse_fest_toolarge(){
        float a = 201;
        float b = 100;
        float c = 100;
        GroesseFest gr = GroesseFest.getGroesse(a, b, c);
        assertEquals(GroesseFest.TOOLARGE, gr);
    }

    
    @Test
    void test_getGroesse_fluessig_small1(){
        float volumen = 0.1f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.SMALL, gr);
    }

    @Test
    void test_getGroesse_fluessig_small2(){
        float volumen = 10;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.SMALL, gr);
    }

    @Test
    void test_getGroesse_fluessig_medium1(){
        float volumen = 10.1f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.MEDIUM, gr);
    }

    @Test
    void test_getGroesse_fluessig_medium2(){
        float volumen = 100f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.MEDIUM, gr);
    }

    @Test
    void test_getGroesse_fluessig_large1(){
        float volumen = 100.1f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.LARGE, gr);
    }

    @Test
    void test_getGroesse_fluessig_large2(){
        float volumen = 1000f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.LARGE, gr);
    }

    @Test
    void test_getGroesse_fluessig_extralarge1(){
        float volumen = 1000.1f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.EXTRALARGE, gr);
    }

    @Test
    void test_getGroesse_fluessig_extralarge2(){
        float volumen = 5000f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.EXTRALARGE, gr);
    }

    @Test
    void test_getGroesse_fluessig_toolarge(){
        float volumen = 5000.1f;
        GroesseFluessig gr = GroesseFluessig.getGroesse(volumen);
        assertEquals(GroesseFluessig.TOOLARGE, gr);
    }

}
