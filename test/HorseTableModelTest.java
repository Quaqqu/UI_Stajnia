import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HorseTableModelTest {
    private HorseTableModel model;

    @BeforeEach
    void setUp() {
        model = new HorseTableModel();
    }

    @Test
    void testEmptyModel() {
        assertEquals(0, model.getRowCount());
        assertEquals(7, model.getColumnCount());
    }

    @Test
    void testSetHorses() {
        List<Horse> horses = List.of(
                new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                        HorseCondition.ZDROWY, 5, 50000, 450)
        );
        model.setHorses(horses);
        assertEquals(1, model.getRowCount());
    }

    @Test
    void testGetValueAt() {
        List<Horse> horses = List.of(
                new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                        HorseCondition.ZDROWY, 5, 50000, 450)
        );
        model.setHorses(horses);

        assertEquals("Azor", model.getValueAt(0, 0));
        assertEquals("Arabski", model.getValueAt(0, 1));
        assertEquals(5, model.getValueAt(0, 4));
    }

    @Test
    void testGetColumnName() {
        assertEquals("Imię", model.getColumnName(0));
        assertEquals("Rasa", model.getColumnName(1));
    }

    @Test
    void testGetHorseAt() {
        List<Horse> horses = List.of(
                new Horse("Azor", "Arabski", HorseType.GORACOKRWISTY,
                        HorseCondition.ZDROWY, 5, 50000, 450)
        );
        model.setHorses(horses);

        Horse horse = model.getHorseAt(0);
        assertNotNull(horse);
        assertEquals("Azor", horse.getName());
    }

    @Test
    void testGetHorseAtInvalidIndex() {
        Horse horse = model.getHorseAt(999);
        assertNull(horse);
    }
}