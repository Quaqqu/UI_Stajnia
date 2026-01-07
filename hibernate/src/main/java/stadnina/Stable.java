package stadnina;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stables")
public class Stable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String stableName;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @OneToMany(mappedBy = "stable", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Horse> stableHorses = new ArrayList<>();

    public Stable() {}

    public Stable(String name, int maxCapacity) {
        this.stableName = name;
        this.maxCapacity = maxCapacity;
    }

    public String getStableName() { return stableName; }
    public int getMaxCapacity() { return maxCapacity; }
    public List<Horse> getStableHorses() { return stableHorses; }

    public void addHorse(Horse horse) {
        stableHorses.add(horse);
        horse.setStable(this);
    }

    public void removeHorse(Horse horse) {
        stableHorses.remove(horse);
        horse.setStable(null);
    }

    public Horse searchPartial(String fragment) {
        for (Horse h : stableHorses) {
            if (h.getName().toLowerCase().contains(fragment.toLowerCase())) return h;
        }
        return null;
    }
}