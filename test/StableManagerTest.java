import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableManagerTest {
    private StableManager manager;

    @BeforeEach
    void setUp() {
        manager = new StableManager();
    }

    @Test
    @DisplayName("Test dodawania stadniny")
    void testAddStable() {
        manager.addStable("Test Stable", 10);
        Stable stable = manager.getStable("Test Stable");
        assertNotNull(stable);
        assertEquals("Test Stable", stable.getStableName());
        assertEquals(10, stable.getMaxCapacity());
    }

    @Test
    @DisplayName("Test dodawania duplikatu stadniny")
    void testAddDuplicateStable() {
        manager.addStable("Duplicate", 5);
        manager.addStable("Duplicate", 10);

        Stable stable = manager.getStable("Duplicate");
        // Powinna pozostać pierwsza dodana
        assertEquals(5, stable.getMaxCapacity());
    }

    @Test
    @DisplayName("Test usuwania stadniny")
    void testRemoveStable() {
        manager.addStable("ToRemove", 5);
        manager.removeStable("ToRemove");

        Stable stable = manager.getStable("ToRemove");
        assertNull(stable);
    }

    @Test
    @DisplayName("Test usuwania nieistniejącej stadniny")
    void testRemoveNonExistentStable() {
        assertDoesNotThrow(() -> manager.removeStable("NonExistent"));
    }

    @Test
    @DisplayName("Test znajdowania pustych stadnin")
    void testFindEmpty() {
        manager.addStable("Empty1", 10);
        manager.addStable("Empty2", 5);
        manager.addStable("NotEmpty", 5);

        // Dodaj konia do jednej stadniny
        Stable notEmpty = manager.getStable("NotEmpty");
        Horse horse = new Horse("Test", "Breed", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 5, 10000, 450);
        notEmpty.addHorse(horse);

        List<Stable> emptyStables = manager.findEmpty();
        assertEquals(2, emptyStables.size());
    }

    @Test
    @DisplayName("Test znajdowania pustych stadnin gdy wszystkie puste")
    void testFindEmptyAllEmpty() {
        manager.addStable("Empty1", 10);
        manager.addStable("Empty2", 5);

        List<Stable> emptyStables = manager.findEmpty();
        assertEquals(2, emptyStables.size());
    }

    @Test
    @DisplayName("Test znajdowania pustych stadnin gdy żadna nie jest pusta")
    void testFindEmptyNoneEmpty() {
        manager.addStable("NotEmpty1", 5);
        manager.addStable("NotEmpty2", 5);

        Stable stable1 = manager.getStable("NotEmpty1");
        Stable stable2 = manager.getStable("NotEmpty2");

        Horse horse1 = new Horse("H1", "B", HorseType.GORACOKRWISTY,
                HorseCondition.ZDROWY, 5, 10000, 450);
        Horse horse2 = new Horse("H2", "B", HorseType.ZIMNOKRWISTY,
                HorseCondition.ZDROWY, 5, 10000, 450);

        stable1.addHorse(horse1);
        stable2.addHorse(horse2);

        List<Stable> emptyStables = manager.findEmpty();
        assertEquals(0, emptyStables.size());
    }

    @Test
    @DisplayName("Test pobierania stadniny po nazwie")
    void testGetStable() {
        manager.addStable("GetMe", 7);

        Stable stable = manager.getStable("GetMe");
        assertNotNull(stable);
        assertEquals("GetMe", stable.getStableName());
        assertEquals(7, stable.getMaxCapacity());
    }

    @Test
    @DisplayName("Test pobierania nieistniejącej stadniny")
    void testGetNonExistentStable() {
        Stable stable = manager.getStable("DoesNotExist");
        assertNull(stable);
    }

    @Test
    @DisplayName("Test getAllStables zwraca listę")
    void testGetAllStables() {
        manager.addStable("S1", 10);
        manager.addStable("S2", 5);

        List<Stable> stables = manager.getAllStables();
        assertNotNull(stables);
        assertEquals(2, stables.size());
    }

    @Test
    @DisplayName("Test summary nie rzuca wyjątku")
    void testSummary() {
        manager.addStable("S1", 10);
        manager.addStable("S2", 5);

        assertDoesNotThrow(() -> manager.summary());
    }

    @Test
    @DisplayName("Test summary dla pustego managera")
    void testSummaryEmpty() {
        assertDoesNotThrow(() -> manager.summary());
    }
}