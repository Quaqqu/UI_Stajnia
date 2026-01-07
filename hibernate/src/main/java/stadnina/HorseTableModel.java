package stadnina;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class HorseTableModel extends AbstractTableModel {
    // Dodano kolumnę "Liczba ocen" zgodnie z wymogiem
    private final String[] columnNames = {"Imię", "Rasa", "Typ", "Status", "Wiek", "Cena", "Waga", "Oceny"};
    private List<Horse> horses;

    public HorseTableModel() {
        this.horses = new ArrayList<>();
    }

    public void setHorses(List<Horse> horses) {
        this.horses = horses;
        fireTableDataChanged();
    }

    public Horse getHorseAt(int row) {
        if (row >= 0 && row < horses.size()) return horses.get(row);
        return null;
    }

    @Override
    public int getRowCount() {
        return horses.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Horse horse = horses.get(rowIndex);
        switch (columnIndex) {
            case 0: return horse.getName();
            case 1: return horse.getBreed();
            case 2: return horse.getType();
            case 3: return horse.getStatus();
            case 4: return horse.getAge();
            case 5: return String.format("%.2f PLN", horse.getPrice());
            case 6: return String.format("%.1f kg", horse.getWeight());
            case 7:
                // Wyświetlanie średniej ocen (wymóg PDF)
                if (horse.getRatings().isEmpty()) return "Brak";
                double avg = horse.getRatings().stream().mapToInt(Rating::getValue).average().orElse(0.0);
                return String.format("%.2f (%d głosów)", avg, horse.getRatings().size());
            default: return null;
        }
    }
}