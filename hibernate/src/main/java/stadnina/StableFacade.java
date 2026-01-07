package stadnina;

import java.util.ArrayList;
import java.util.List;

public class StableFacade {
    private StableManager manager;

    public StableFacade() {
        this.manager = new StableManager();
    }

    public List<Stable> getAllStables() {
        return manager.getAllStables();
    }

    public void updateHorse(Horse horse) {
        manager.updateHorse(horse);
    }

    public void addStable(String name, int capacity) throws StableException {
        if (name == null || name.isEmpty()) throw new StableException("Brak nazwy");
        manager.addStable(name, capacity);
    }

    public void removeStable(String name) throws StableException {
        if (name == null || name.trim().isEmpty()) {
            throw new StableException("Nie wybrano stadniny!");
        }
        manager.removeStable(name);
    }

    public void addHorse(String stableName, String name, String breed, HorseType type,
                         HorseCondition status, int age, double price, double weight) throws HorseException {
        Stable stable = manager.getStable(stableName);
        if (stable == null) throw new HorseException("Brak stadniny");

        Horse horse = new Horse(name, breed, type, status, age, price, weight);
        stable.addHorse(horse);
        manager.updateStable(stable);
    }

    public void removeHorse(Horse horse) {
        manager.removeHorse(horse);
    }

    // --- METODY DO OCEN ---
    public void addRating(Horse horse, int score, String description) {
        Rating rating = new Rating(score, description, horse);
        horse.addRating(rating);
        manager.updateHorse(horse);
    }

    public List<String> getStableNames() {
        List<String> names = new ArrayList<>();
        for (Stable s : manager.getAllStables()) names.add(s.getStableName());
        return names;
    }

    public List<Horse> getHorsesInStable(String stableName) {
        Stable s = manager.getStable(stableName);
        return s != null ? s.getStableHorses() : new ArrayList<>();
    }

    // --- SERIALIZACJA I CSV ---
    public void exportCSV(String filename) {
        try {
            DataExporter.exportToCSV(new StableDAO(), filename);
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void saveBinary(String filename) throws Exception {
        List<Stable> stables = manager.getAllStables();
        DataExporter.saveStablesBinary(stables, filename);
    }

    public List<Stable> loadBinary(String filename) throws Exception {
        return DataExporter.loadStablesBinary(filename);
    }

    // --- TO JEST TA BRAKUJĄCA KLASA, KTÓRA POWODOWAŁA BŁĄD ---
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