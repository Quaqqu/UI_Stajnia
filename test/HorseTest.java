import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class HorseTest {
    private Horse horse1;
    private Horse horse2;
    private Horse horse3;

    @BeforeEach
    void setUp() {
        horse1 = new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 5, 50000, 450);
        horse2 = new Horse("Bella", "Polski", HorseType.ZIMNOKRWISTY,
                HorseCondition.TRENING, 3, 35000, 500);
        horse3 = new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 5, 50000, 450);
    }

    @Test
    @DisplayName("Test tworzenia konia")
    void testHorseCreation() {
        assertNotNull(horse1);
        assertEquals("Azor", horse1.getName());
        assertEquals("Arabski", horse1.getBreed());
        assertEquals(HorseType.GORACOKRWISTY, horse1.getType());
        assertEquals(HorseCondition.ZDROWY, horse1.getStatus());
        assertEquals(5, horse1.getAge());
        assertEquals(50000, horse1.getPrice());
        assertEquals(450, horse1.getWeight());
    }

    @Test
    @DisplayName("Test zmiany statusu konia")
    void testSetStatus() {
        horse1.setStatus(HorseCondition.CHORY);
        assertEquals(HorseCondition.CHORY, horse1.getStatus());
    }

    @Test
    @DisplayName("Test zmiany wagi konia")
    void testSetWeight() {
        horse1.setWeight(480.5);
        assertEquals(480.5, horse1.getWeight());
    }

    @Test
    @DisplayName("Test compareTo - ten sam koń")
    void testCompareToSame() {
        assertEquals(0, horse1.compareTo(horse3));
    }

    @Test
    @DisplayName("Test compareTo - różne imiona")
    void testCompareToDifferentNames() {
        int result = horse1.compareTo(horse2);
        assertTrue(result < 0); // "Azor" < "Bella"
    }

    @Test
    @DisplayName("Test compareTo - to samo imię, różne rasy")
    void testCompareToSameNameDifferentBreed() {
        Horse horse4 = new Horse("Azor", "Mustang", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 5, 50000, 450);
        int result = horse1.compareTo(horse4);
        assertTrue(result < 0); // "Arabski" < "Mustang"
    }

    @Test
    @DisplayName("Test compareTo - to samo imię i rasa, różny wiek")
    void testCompareToSameNameAndBreed() {
        Horse horse4 = new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 7, 50000, 450);
        int result = horse1.compareTo(horse4);
        assertTrue(result < 0); // 5 < 7
    }

    @Test
    @DisplayName("Test toString nie zwraca null")
    void testToString() {
        String str = horse1.toString();
        assertNotNull(str);
        assertTrue(str.contains("Azor"));
        assertTrue(str.contains("Arabski"));
    }

    @Test
    @DisplayName("Test wszystkich getterów")
    void testGetters() {
        assertEquals("Azor", horse1.getName());
        assertEquals("Arabski", horse1.getBreed());
        assertEquals(HorseType.GORACOKRWISTY, horse1.getType());
        assertEquals(HorseCondition.ZDROWY, horse1.getStatus());
        assertEquals(5, horse1.getAge());
        assertEquals(50000, horse1.getPrice(), 0.01);
        assertEquals(450, horse1.getWeight(), 0.01);
    }

    @Test
    @DisplayName("Test print nie rzuca wyjątku")
    void testPrint() {
        assertDoesNotThrow(() -> horse1.print());
    }
}