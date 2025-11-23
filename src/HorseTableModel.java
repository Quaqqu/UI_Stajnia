import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Model tabeli dla koni
 */
public class HorseTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Imię", "Rasa", "Typ", "Status", "Wiek", "Cena (PLN)", "Waga (kg)"};
    private List<Horse> horses;

    public HorseTableModel() {
        this.horses = new ArrayList<>();
    }

    public void setHorses(List<Horse> horses) {
        this.horses = horses;
        fireTableDataChanged();
    }

    public Horse getHorseAt(int row) {
        if (row >= 0 && row < horses.size()) {
            return horses.get(row);
        }
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
            case 2: return horse.getType().toString();
            case 3: return horse.getStatus().toString();
            case 4: return horse.getAge();
            case 5: return String.format("%.2f", horse.getPrice());
            case 6: return String.format("%.1f", horse.getWeight());
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: case 1: case 2: case 3: case 5: case 6: return String.class;
            case 4: return Integer.class;
            default: return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Tylko odczyt
    }
}
