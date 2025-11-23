import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StableManager {
    private Map<String, Stable> stables;

    public StableManager() {
        this.stables = new HashMap<>();
    }

    public void addStable(String name, int capacity) {
        if (stables.containsKey(name)) {
            System.out.println("Stadnina o nazwie '" + name + "' już istnieje.");
        } else {
            Stable newStable = new Stable(name, capacity);
            stables.put(name, newStable);
            System.out.println("Dodano nową stadninę: " + name);
        }
    }

    public void removeStable(String name) {
        if (stables.remove(name) != null) {
            System.out.println("Usunięto stadninę: " + name);
        } else {
            System.out.println("Nie znaleziono stadniny o nazwie: " + name);
        }
    }

    public List<Stable> findEmpty() {
        List<Stable> emptyStables = new ArrayList<>();
        for (Stable s : stables.values()) {
            if (s.getStableHorses().isEmpty()) {
                emptyStables.add(s);
            }
        }
        return emptyStables;
    }

    // NOWA METODA - zwraca wszystkie stadniny
    public List<Stable> getAllStables() {
        return new ArrayList<>(stables.values());
    }

    public void summary() {
        System.out.println("\n===== PODSUMOWANIE WSZYSTKICH STADNIN =====");
        if (stables.isEmpty()) {
            System.out.println("Brak stadnin do wyświetlenia.");
            return;
        }
        for (Stable s : stables.values()) {
            double occupancy = (double) s.getStableHorses().size() / s.getMaxCapacity() * 100;
            System.out.printf("Stadnina: '%s' | Zapełnienie: %.2f%% (%d/%d)\n",
                    s.getStableName(), occupancy, s.getStableHorses().size(), s.getMaxCapacity());
        }
    }

    public Stable getStable(String name) {
        return stables.get(name);
    }
}
