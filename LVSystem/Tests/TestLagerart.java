package LVSystem.Tests;

import org.junit.jupiter.api.Test;

import LVSystem.Main.Datentypen.Lagerart;

import static org.junit.jupiter.api.Assertions.*;

public class TestLagerart {
    
    @Test
    void test_getRegalart_fest(){
        assertEquals("Fach", Lagerart.FEST.getRegalart());
    }

    @Test
    void test_getRegalart_fluessig(){
        assertEquals("Tank", Lagerart.FLUESSIG.getRegalart());
    }
}
