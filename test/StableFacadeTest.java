import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableFacadeTest {
    private StableFacade facade;

    @BeforeEach
    void setUp() {
        facade = new StableFacade();
    }

    @Test
    @DisplayName("Test dodawania stadniny z poprawnymi danymi")
    void testAddStableValid() {
        assertDoesNotThrow(() -> {
            facade.addStable("Test Stable", 10);
        });
    }

    @Test
    @DisplayName("Test dodawania stadniny z pustą nazwą")
    void testAddStableEmptyName() {
        StableException exception = assertThrows(StableException.class, () -> {
            facade.addStable("", 10);
        });
        assertTrue(exception.getMessage().contains("pusta"));
    }

    @Test
    @DisplayName("Test dodawania stadniny z ujemną pojemnością")
    void testAddStableNegativeCapacity() {
        StableException exception = assertThrows(StableException.class, () -> {
            facade.addStable("Test", -5);
        });
        assertTrue(exception.getMessage().contains("większa od 0"));
    }

    @Test
    @DisplayName("Test dodawania stadniny z pojemnością zero")
    void testAddStableZeroCapacity() {
        StableException exception = assertThrows(StableException.class, () -> {
            facade.addStable("Test", 0);
        });
        assertNotNull(exception);
    }

    @Test
    @DisplayName("Test pobierania nazw stadnin")
    void testGetStableNames() {
        List<String> names = facade.getStableNames();
        assertNotNull(names);
        assertTrue(names.size() >= 3); // Bo są 3 testowe
    }

    @Test
    @DisplayName("Test dodawania konia z poprawnymi danymi")
    void testAddHorseValid() {
        assertDoesNotThrow(() -> {
            facade.addHorse("Stadnina Zakopane", "TestHorse", "TestBreed",
                    HorseType.GORACOKRWISTY, HorseCondition.ZDROWY,
                    5, 10000, 450);
        });
    }

    @Test
    @DisplayName("Test dodawania konia z pustym imieniem")
    void testAddHorseEmptyName() {
        HorseException exception = assertThrows(HorseException.class, () -> {
            facade.addHorse("Stadnina Zakopane", "", "Breed",
                    HorseType.GORACOKRWISTY, HorseCondition.ZDROWY,
                    5, 10000, 450);
        });
        assertTrue(exception.getMessage().contains("puste"));
    }

    @Test
    @DisplayName("Test dodawania konia z ujemnym wiekiem")
    void testAddHorseNegativeAge() {
        HorseException exception = assertThrows(HorseException.class, () -> {
            facade.addHorse("Stadnina Zakopane", "Horse", "Breed",
                    HorseType.GORACOKRWISTY, HorseCondition.ZDROWY,
                    -5, 10000, 450);
        });
        assertTrue(exception.getMessage().contains("Wiek"));
    }

    @Test
    @DisplayName("Test dodawania konia z ujemną ceną")
    void testAddHorseNegativePrice() {
        HorseException exception = assertThrows(HorseException.class, () -> {
            facade.addHorse("Stadnina Zakopane", "Horse", "Breed",
                    HorseType.GORACOKRWISTY, HorseCondition.ZDROWY,
                    5, -10000, 450);
        });
        assertTrue(exception.getMessage().contains("Cena"));
    }

    @Test
    @DisplayName("Test dodawania konia z zerową wagą")
    void testAddHorseZeroWeight() {
        HorseException exception = assertThrows(HorseException.class, () -> {
            facade.addHorse("Stadnina Zakopane", "Horse", "Breed",
                    HorseType.GORACOKRWISTY, HorseCondition.ZDROWY,
                    5, 10000, 0);
        });
        assertTrue(exception.getMessage().contains("Waga"));
    }

    @Test
    @DisplayName("Test pobierania koni z istniejącej stadniny")
    void testGetHorsesInStable() {
        List<Horse> horses = facade.getHorsesInStable("Stadnina Zakopane");
        assertNotNull(horses);
        assertTrue(horses.size() >= 2); // Bo są 2 testowe
    }

    @Test
    @DisplayName("Test pobierania koni z nieistniejącej stadniny")
    void testGetHorsesInNonExistentStable() {
        List<Horse> horses = facade.getHorsesInStable("NonExistent");
        assertNotNull(horses);
        assertTrue(horses.isEmpty());
    }

    @Test
    @DisplayName("Test filtrowania koni po nazwie")
    void testFilterHorsesByName() {
        List<Horse> filtered = facade.filterHorsesByName("Stadnina Zakopane", "Az");
        assertNotNull(filtered);
    }

    @Test
    @DisplayName("Test filtrowania koni po statusie")
    void testFilterHorsesByCondition() {
        List<Horse> filtered = facade.filterHorsesByCondition("Stadnina Zakopane",
                HorseCondition.ZDROWY);
        assertNotNull(filtered);
    }

    @Test
    @DisplayName("Test pobierania informacji o wszystkich stadninach")
    void testGetAllStablesInfo() {
        List<StableFacade.StableInfo> infos = facade.getAllStablesInfo();
        assertNotNull(infos);
        assertTrue(infos.size() >= 3);
    }

    @Test
    @DisplayName("Test sortowania stadnin według zapełnienia")
    void testSortStablesByLoad() {
        List<StableFacade.StableInfo> stables = facade.getAllStablesInfo();
        facade.sortStablesByLoad(stables);

        // Sprawdź czy są posortowane malejąco
        for (int i = 0; i < stables.size() - 1; i++) {
            assertTrue(stables.get(i).getCurrentLoad() >= stables.get(i + 1).getCurrentLoad());
        }
    }
}