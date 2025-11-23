import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Model tabeli dla stadnin
 */
public class StableTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Nazwa", "Konie", "Pojemność", "Zapełnienie %"};
    private List<StableFacade.StableInfo> stables;

    public StableTableModel() {
        this.stables = new ArrayList<>();
    }

    public void setStables(List<StableFacade.StableInfo> stables) {
        this.stables = stables;
        fireTableDataChanged();
    }

    public StableFacade.StableInfo getStableAt(int row) {
        if (row >= 0 && row < stables.size()) {
            return stables.get(row);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return stables.size();
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
        StableFacade.StableInfo stable = stables.get(rowIndex);
        switch (columnIndex) {
            case 0: return stable.getName();
            case 1: return stable.getCurrentHorses();
            case 2: return stable.getMaxCapacity();
            case 3: return String.format("%.1f%%", stable.getCurrentLoad());
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class;
            case 1: return Integer.class;
            case 2: return Integer.class;
            case 3: return String.class;
            default: return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Tylko odczyt
    }
}
