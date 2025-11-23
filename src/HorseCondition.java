public enum HorseCondition {
    ZDROWY("Zdrowy"),
    CHORY("Chory"),
    TRENING("Trening"),
    KARENCJA("Karencja"),
    SPRZEDANY("Sprzedany");

    private final String name;

    HorseCondition(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}