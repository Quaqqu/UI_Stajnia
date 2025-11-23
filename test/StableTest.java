import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableTest {
    private Stable stable;
    private Horse horse1;
    private Horse horse2;
    private Horse horse3;

    @BeforeEach
    void setUp() {
        stable = new Stable("Test Stable", 5);
        horse1 = new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 5, 50000, 450);
        horse2 = new Horse("Bella", "Polski", HorseType.ZIMNOKRWISTY,
                HorseCondition.TRENING, 3, 35000, 500);
        horse3 = new Horse("Thunder", "Mustang", HorseType.GORACOKRWISTY,
                HorseCondition.CHORY, 7, 40000, 480);
    }

    @Test
    @DisplayName("Test tworzenia stadniny")
    void testStableCreation() {
        assertNotNull(stable);
        assertEquals("Test Stable", stable.getStableName());
        assertEquals(5, stable.getMaxCapacity());
        assertTrue(stable.getStableHorses().isEmpty());
    }

    @Test
    @DisplayName("Test dodawania konia")
    void testAddHorse() {
        stable.addHorse(horse1);
        assertEquals(1, stable.getStableHorses().size());
        assertTrue(stable.getStableHorses().contains(horse1));
    }

    @Test
    @DisplayName("Test dodawania duplikatu konia")
    void testAddDuplicateHorse() {
        stable.addHorse(horse1);
        stable.addHorse(horse1);
        assertEquals(1, stable.getStableHorses().size());
    }

    @Test
    @DisplayName("Test dodawania konia do pełnej stadniny")
    void testAddHorseToFullStable() {
        Stable smallStable = new Stable("Small", 2);
        smallStable.addHorse(horse1);
        smallStable.addHorse(horse2);
        smallStable.addHorse(horse3); // Nie powinien się dodać
        assertEquals(2, smallStable.getStableHorses().size());
    }

    @Test
    @DisplayName("Test usuwania konia")
    void testRemoveHorse() {
        stable.addHorse(horse1);
        stable.removeHorse(horse1);
        assertTrue(stable.getStableHorses().isEmpty());
    }

    @Test
    @DisplayName("Test usuwania nieistniejącego konia")
    void testRemoveNonExistentHorse() {
        stable.addHorse(horse1);
        stable.removeHorse(horse2);
        assertEquals(1, stable.getStableHorses().size());
    }

    @Test
    @DisplayName("Test zmiany stanu konia")
    void testChangeCondition() {
        stable.addHorse(horse1);
        stable.changeCondition(horse1, HorseCondition.KARENCJA);
        assertEquals(HorseCondition.KARENCJA, horse1.getStatus());
    }

    @Test
    @DisplayName("Test oznaczania konia jako chorego")
    void testSickHorse() {
        stable.addHorse(horse1);
        stable.sickHorse(horse1);
        assertEquals(HorseCondition.CHORY, horse1.getStatus());
    }

    @Test
    @DisplayName("Test zmiany wagi konia")
    void testChangeWeight() {
        stable.addHorse(horse1);
        stable.changeWeight(horse1, 470);
        assertEquals(470, horse1.getWeight());
    }

    @Test
    @DisplayName("Test liczenia koni według statusu")
    void testCountByStatus() {
        stable.addHorse(horse1); // ZDROWY
        stable.addHorse(horse2); // TRENING
        stable.addHorse(horse3); // CHORY

        assertEquals(1, stable.countByStatus(HorseCondition.ZDROWY));
        assertEquals(1, stable.countByStatus(HorseCondition.TRENING));
        assertEquals(1, stable.countByStatus(HorseCondition.CHORY));
        assertEquals(0, stable.countByStatus(HorseCondition.KARENCJA));
    }

    @Test
    @DisplayName("Test sortowania po nazwie")
    void testSortByName() {
        stable.addHorse(horse3); // Thunder
        stable.addHorse(horse1); // Azor
        stable.addHorse(horse2); // Bella

        List<Horse> sorted = stable.sortByName();
        assertEquals("Azor", sorted.get(0).getName());
        assertEquals("Bella", sorted.get(1).getName());
        assertEquals("Thunder", sorted.get(2).getName());
    }

    @Test
    @DisplayName("Test sortowania po cenie")
    void testSortByPrice() {
        stable.addHorse(horse1); // 50000
        stable.addHorse(horse2); // 35000
        stable.addHorse(horse3); // 40000

        List<Horse> sorted = stable.sortByPrice();
        assertEquals(35000, sorted.get(0).getPrice());
        assertEquals(40000, sorted.get(1).getPrice());
        assertEquals(50000, sorted.get(2).getPrice());
    }

    @Test
    @DisplayName("Test wyszukiwania konia po nazwie")
    void testSearch() {
        stable.addHorse(horse1);
        stable.addHorse(horse2);

        Horse found = stable.search("Azor");
        assertNotNull(found);
        assertEquals("Azor", found.getName());

        Horse notFound = stable.search("NonExistent");
        assertNull(notFound);
    }

    @Test
    @DisplayName("Test wyszukiwania częściowego")
    void testSearchPartial() {
        stable.addHorse(horse1); // Azor, Arabski
        stable.addHorse(horse2); // Bella, Polski
        stable.addHorse(horse3); // Thunder, Mustang

        List<Horse> found = stable.searchPartial("or");
        assertEquals(1, found.size()); // Azor

        List<Horse> found2 = stable.searchPartial("ski");
        assertEquals(2, found2.size()); // Arabski, Polski
    }

    @Test
    @DisplayName("Test znajdowania najcięższego konia")
    void testMax() {
        stable.addHorse(horse1); // 450
        stable.addHorse(horse2); // 500
        stable.addHorse(horse3); // 480

        Horse heaviest = stable.max();
        assertNotNull(heaviest);
        assertEquals(500, heaviest.getWeight());
        assertEquals("Bella", heaviest.getName());
    }

    @Test
    @DisplayName("Test max dla pustej stadniny")
    void testMaxEmptyStable() {
        Horse max = stable.max();
        assertNull(max);
    }

    @Test
    @DisplayName("Test summary nie rzuca wyjątku")
    void testSummary() {
        stable.addHorse(horse1);
        assertDoesNotThrow(() -> stable.summary());
    }
}