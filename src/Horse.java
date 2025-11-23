public class Horse implements Comparable<Horse> {
    private String name;
    private String breed;
    private HorseType type;
    private HorseCondition status;
    private int age;
    private double price;
    private double weight;

    public Horse(String name, String breed, HorseType type, HorseCondition status, int age, double price, double weight) {
        this.name = name;
        this.breed = breed;
        this.type = type;
        this.status = status;
        this.age = age;
        this.price = price;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public HorseType getType() {
        return type;
    }

    public HorseCondition getStatus() {
        return status;
    }

    public void setStatus(HorseCondition status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public double getPrice() {
        return price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void print() {
        System.out.println(this.toString());
    }

    public String toString() {
        return "Koń " + name + "\nRasa " + breed + "\nTyp " + type + "\nWiek " + age + " Status " + status + "\n Waga " + weight + "kg\n" + "Cena " + price + " PLN";
    }

    @Override
    public int compareTo(Horse other) {
        int nameCompare = this.name.compareTo(other.name);
        if (nameCompare != 0) return nameCompare;
        int breedCompare = this.breed.compareTo(other.breed);
        if (breedCompare != 0) return breedCompare;
        return Integer.compare(this.age, other.age);
    }
}