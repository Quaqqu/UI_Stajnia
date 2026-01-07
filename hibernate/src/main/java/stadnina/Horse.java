package stadnina;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horses")
public class Horse implements Comparable<Horse>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;

    @Enumerated(EnumType.STRING)
    private HorseType type;

    @Enumerated(EnumType.STRING)
    private HorseCondition status;

    private int age;
    private double price;
    private double weight;

    @ManyToOne
    @JoinColumn(name = "stable_id")
    private Stable stable;

    @OneToMany(mappedBy = "horse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Rating> ratings = new ArrayList<>();

    public Horse() {}

    public Horse(String name, String breed, HorseType type, HorseCondition status, int age, double price, double weight) {
        this.name = name;
        this.breed = breed;
        this.type = type;
        this.status = status;
        this.age = age;
        this.price = price;
        this.weight = weight;
    }

    // --- GETTERY ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getBreed() { return breed; }
    public HorseType getType() { return type; }
    public HorseCondition getStatus() { return status; }
    public int getAge() { return age; }
    public double getPrice() { return price; }
    public double getWeight() { return weight; }
    public Stable getStable() { return stable; }
    public List<Rating> getRatings() { return ratings; }

    // --- WSZYSTKIE SETTERY (Wymagane do pełnej edycji) ---
    public void setName(String name) { this.name = name; }
    public void setBreed(String breed) { this.breed = breed; }
    public void setType(HorseType type) { this.type = type; }
    public void setStatus(HorseCondition status) { this.status = status; }
    public void setAge(int age) { this.age = age; }
    public void setPrice(double price) { this.price = price; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setStable(Stable stable) { this.stable = stable; }

    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setHorse(this);
    }

    @Override
    public int compareTo(Horse other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name + " (" + breed + ")";
    }
}