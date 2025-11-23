import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fasada oddzielająca warstwę UI od logiki biznesowej
 */
public class StableFacade {
    private StableManager manager;

    public StableFacade() {
        this.manager = new StableManager();
        initializeTestData();
    }
    private void initializeTestData() {
        // Dane testowe do demonstracji
        try {
            addStable("Stadnina Zakopane", 10);
            addStable("Stadnina Warszawa", 8);
            addStable("Stadnina Kraków", 5);
        } catch (StableException e) {
            System.err.println("Błąd inicjalizacji stadnin: " + e.getMessage());
        }

        try {
            addHorse("Stadnina Zakopane", "Azor", "Arabski", HorseType.GORACOKRWISTY, 
                    HorseCondition.ZDROWY, 5, 50000, 450);
            addHorse("Stadnina Zakopane", "Bella", "Konik Polski", HorseType.ZIMNOKRWISTY, 
                    HorseCondition.TRENING, 3, 35000, 500);
            addHorse("Stadnina Warszawa", "Thunder", "Mustang", HorseType.GORACOKRWISTY, 
                    HorseCondition.ZDROWY, 7, 65000, 480);
        } catch (Exception e) {
            System.err.println("Błąd inicjalizacji danych: " + e.getMessage());
        }
    }

    public List<String> getStableNames() {
        List<String> names = new ArrayList<>();
        for (Stable s : manager.getAllStables()) {
            names.add(s.getStableName());
        }
        return names;
    }

    public void addStable(String name, int capacity) throws StableException {
        if (name == null || name.trim().isEmpty()) {
            throw new StableException("Nazwa stadniny nie może być pusta!");
        }
        if (capacity <= 0) {
            throw new StableException("Pojemność musi być większa od 0!");
        }
        manager.addStable(name, capacity);
    }

    public void removeStable(String name) throws StableException {
        if (name == null || name.trim().isEmpty()) {
            throw new StableException("Nie wybrano stadniny do usunięcia!");
        }
        Stable stable = manager.getStable(name);
        if (stable != null && !stable.getStableHorses().isEmpty()) {
            throw new StableException("Nie można usunąć stadniny z końmi! Usuń najpierw konie.");
        }
        manager.removeStable(name);
    }

    public List<Horse> getHorsesInStable(String stableName) {
        if (stableName == null) return new ArrayList<>();
        Stable stable = manager.getStable(stableName);
        return stable != null ? new ArrayList<>(stable.getStableHorses()) : new ArrayList<>();
    }

    public void addHorse(String stableName, String name, String breed, HorseType type,
                        HorseCondition status, int age, double price, double weight) throws HorseException {
        if (name == null || name.trim().isEmpty()) {
            throw new HorseException("Imię konia nie może być puste!");
        }
        if (age <= 0) {
            throw new HorseException("Wiek musi być większy od 0!");
        }
        if (price < 0) {
            throw new HorseException("Cena nie może być ujemna!");
        }
        if (weight <= 0) {
            throw new HorseException("Waga musi być większa od 0!");
        }

        Stable stable = manager.getStable(stableName);
        if (stable == null) {
            throw new HorseException("Nie znaleziono stadniny: " + stableName);
        }

        Horse horse = new Horse(name, breed, type, status, age, price, weight);
        stable.addHorse(horse);
    }

    public void removeHorse(String stableName, Horse horse) throws HorseException {
        if (horse == null) {
            throw new HorseException("Nie wybrano konia do usunięcia!");
        }
        Stable stable = manager.getStable(stableName);
        if (stable == null) {
            throw new HorseException("Nie znaleziono stadniny!");
        }
        stable.removeHorse(horse);
    }

    public List<Horse> filterHorsesByName(String stableName, String nameFilter) {
        Stable stable = manager.getStable(stableName);
        if (stable == null || nameFilter == null || nameFilter.trim().isEmpty()) {
            return getHorsesInStable(stableName);
        }
        return stable.searchPartial(nameFilter);
    }

    public List<Horse> filterHorsesByCondition(String stableName, HorseCondition condition) {
        Stable stable = manager.getStable(stableName);
        if (stable == null) return new ArrayList<>();
        
        return stable.getStableHorses().stream()
                .filter(h -> condition == null || h.getStatus() == condition)
                .collect(Collectors.toList());
    }

    public List<StableInfo> getAllStablesInfo() {
        List<StableInfo> infos = new ArrayList<>();
        for (Stable s : manager.getAllStables()) {
            infos.add(new StableInfo(
                s.getStableName(),
                s.getStableHorses().size(),
                s.getMaxCapacity(),
                calculateLoad(s)
            ));
        }
        return infos;
    }

    public void sortStablesByLoad(List<StableInfo> stables) {
        stables.sort(Comparator.comparingDouble(StableInfo::getCurrentLoad).reversed());
    }

    private double calculateLoad(Stable stable) {
        if (stable.getMaxCapacity() == 0) return 0;
        return (double) stable.getStableHorses().size() / stable.getMaxCapacity() * 100;
    }

    // Klasa pomocnicza do przekazywania info o stadninach
    public static class StableInfo {
        private String name;
        private int currentHorses;
        private int maxCapacity;
        private double currentLoad;

        public StableInfo(String name, int currentHorses, int maxCapacity, double currentLoad) {
            this.name = name;
            this.currentHorses = currentHorses;
            this.maxCapacity = maxCapacity;
            this.currentLoad = currentLoad;
        }

        public String getName() { return name; }
        public int getCurrentHorses() { return currentHorses; }
        public int getMaxCapacity() { return maxCapacity; }
        public double getCurrentLoad() { return currentLoad; }
    }
}
