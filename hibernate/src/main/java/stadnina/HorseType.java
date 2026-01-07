package stadnina;

public enum HorseType {
    ZIMNOKRWISTY("Zimnokrwisty"),
    GORACOKRWISTY("Gorącokrwisty");

    private final String displayName;

    HorseType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}