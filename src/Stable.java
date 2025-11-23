import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Stable {
    private String stableName;
    private List<Horse> stableHorses;
    private int maxCapacity;

    public Stable(String name, int maxCapacity) {
        this.stableName = name;
        this.maxCapacity = maxCapacity;
        this.stableHorses = new ArrayList<>();
    }

    public String getStableName() {return stableName;}
    public List<Horse> getStableHorses() {return stableHorses;}
    public int getMaxCapacity() {return maxCapacity;}

    public void addHorse(Horse horse) {
        if (stableHorses.size() >= maxCapacity) {
            System.err.println("Błąd: Stadnina '" + stableName + "' jest pełna! Nie można dodać " + horse.getName());
            return;
        }
        for (Horse h : stableHorses) {
            if (h.getName().equals(horse.getName()) && h.getBreed().equals(horse.getBreed()) && h.getAge() == horse.getAge()) {
                System.out.println("Informacja: Koń " + horse.getName() + " już istnieje w tej stadninie.");
                return;
            }
        }
        stableHorses.add(horse);
        System.out.println("Dodano konia: " + horse.getName() + " do stadniny '" + stableName);
    }

    public void removeHorse(Horse horse) {
        boolean removed = stableHorses.remove(horse);
        if (removed) {
            System.out.println("Usunięto konia: " + horse.getName() + " ze stadniny '" + stableName);
        } else {
            System.out.println("Nie znaleziono konia: " + horse.getName() + " w stadninie '" + stableName);
        }
    }

    public void changeCondition(Horse horse, HorseCondition condition) {
        horse.setStatus(condition);
        System.out.println("Zmieniono stan konia " + horse.getName() + " na: " + condition);
    }

    public void sickHorse(Horse horse) {
        changeCondition(horse, HorseCondition.CHORY);
    }

    public void changeWeight(Horse horse, double kg){
        horse.setWeight(kg);
        System.out.println("Zmieniono wage konia " + horse.getName() + " na: " + kg + " kg");
    }

    public int countByStatus(HorseCondition status) {
        int count = 0;
        for (Horse h : stableHorses) {
            if (h.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    public List<Horse> sortByName() {
        List<Horse> sortedList = new ArrayList<>(stableHorses);
        Collections.sort(sortedList);
        return sortedList;
    }

    public List<Horse> sortByPrice() {
        List<Horse> sortedList = new ArrayList<>(stableHorses);
        sortedList.sort(Comparator.comparingDouble(Horse::getPrice));
        return sortedList;
    }

    public Horse search(String name) {
        for (Horse h : stableHorses) {
            if (h.getName().equalsIgnoreCase(name)) {
                return h;
            }
        }
        return null;
    }

    public List<Horse> searchPartial(String fragment) {
        List<Horse> foundHorses = new ArrayList<>();
        String lowerCaseFragment = fragment.toLowerCase();
        for (Horse h : stableHorses) {
            if (h.getName().toLowerCase().contains(lowerCaseFragment) || h.getBreed().toLowerCase().contains(lowerCaseFragment)) {
                foundHorses.add(h);
            }
        }
        return foundHorses;
    }

    public void summary() {
        System.out.println("\nPODSUMOWANIE STADNINY: " + stableName + "\n");
        System.out.println("Pojemność: " + stableHorses.size() + "/" + maxCapacity);
        for (Horse h : stableHorses) {
            h.print();
        }
        System.out.println(" KONIEC PODSUMOWANIA");
    }

    public Horse max() {
        if (stableHorses.isEmpty()) {
            return null;
        }
        return Collections.max(stableHorses, Comparator.comparingDouble(Horse::getWeight));
    }
}