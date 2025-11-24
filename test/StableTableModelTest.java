import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StableTableModelTest {
    private StableTableModel model;

    @BeforeEach
    void setUp() {
        model = new StableTableModel();
    }

    @Test
    void testEmptyModel() {
        assertEquals(0, model.getRowCount());
        assertEquals(4, model.getColumnCount());
    }

    @Test
    void testSetStables() {
        List<StableFacade.StableInfo> stables = List.of(
                new StableFacade.StableInfo("Test", 5, 10, 50.0)
        );
        model.setStables(stables);
        assertEquals(1, model.getRowCount());
    }

    @Test
    void testGetValueAt() {
        List<StableFacade.StableInfo> stables = List.of(
                new StableFacade.StableInfo("Test Stable", 5, 10, 50.0)
        );
        model.setStables(stables);

        assertEquals("Test Stable", model.getValueAt(0, 0));
        assertEquals(5, model.getValueAt(0, 1));
        assertEquals(10, model.getValueAt(0, 2));
    }

    @Test
    void testGetColumnName() {
        assertEquals("Nazwa", model.getColumnName(0));
        assertEquals("Konie", model.getColumnName(1));
        assertEquals("Pojemność", model.getColumnName(2));
    }

    @Test
    void testGetStableAt() {
        List<StableFacade.StableInfo> stables = List.of(
                new StableFacade.StableInfo("Test", 5, 10, 50.0)
        );
        model.setStables(stables);

        StableFacade.StableInfo info = model.getStableAt(0);
        assertNotNull(info);
        assertEquals("Test", info.getName());
    }

    @Test
    void testGetStableAtInvalidIndex() {
        StableFacade.StableInfo info = model.getStableAt(999);
        assertNull(info);
    }
}