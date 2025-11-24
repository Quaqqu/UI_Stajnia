import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        HorseTest.class,
        StableTest.class,
        StableManagerTest.class,
        StableFacadeTest.class,
        StableTableModelTest.class,
        HorseTableModelTest.class,
})
public class AllTestsSuite {
    // Pusta klasa - adnotacje robią robotę
}