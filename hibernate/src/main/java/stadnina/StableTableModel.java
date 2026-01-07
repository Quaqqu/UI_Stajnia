package stadnina;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class StableTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Nazwa", "Liczba koni", "Pojemność", "Zapełnienie"};

    // ZMIANA: Teraz lista trzyma StableInfo, a nie Stable
    private List<StableFacade.StableInfo> stables;

    public StableTableModel() {
        this.stables = new ArrayList<>();
    }

    // ZMIANA: Metoda przyjmuje listę StableInfo
    public void setStables(List<StableFacade.StableInfo> stables) {
        this.stables = stables;
        fireTableDataChanged();
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
}