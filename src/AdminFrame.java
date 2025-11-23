import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Panel administracyjny z pełnymi uprawnieniami
 */
public class AdminFrame extends JFrame {
    private StableFacade facade;
    private JTable stablesTable;
    private JTable horsesTable;
    private StableTableModel stableModel;
    private HorseTableModel horseModel;
    private JTextField filterField;
    private JComboBox<String> statusComboBox;
    private String selectedStableName = null;

    public AdminFrame(StableFacade facade) {
        this.facade = facade;
        initUI();
        loadStables();
    }

    private void initUI() {
        setTitle("Panel Administracyjny - Zarządzanie Stadniną");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Panel główny
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));

        // Nagłówek
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel z tabelami (split)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(createStablesPanel());
        splitPane.setRightComponent(createHorsesPanel());

        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 130, 180));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("🐴 ADMINISTRATOR - Zarządzanie Stadniną");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Wyloguj");
        logoutBtn.setBackground(new Color(220, 80, 80));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> logout());

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(logoutBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStablesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                "Stadniny",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(70, 130, 180)
        ));

        // Tabela stadnin
        stableModel = new StableTableModel();
        stablesTable = new JTable(stableModel);
        stablesTable.setRowHeight(25);
        stablesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stablesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        stablesTable.getTableHeader().setBackground(new Color(70, 130, 180));
        stablesTable.getTableHeader().setForeground(Color.WHITE);

        // Wyśrodkowanie danych
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < stablesTable.getColumnCount(); i++) {
            stablesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        stablesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                onStableSelected();
            }
        });

        JScrollPane scrollPane = new JScrollPane(stablesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Przyciski operacji na stadninach
        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton addStableBtn = createStyledButton("➕ Dodaj Stadninę", new Color(60, 179, 113));
        addStableBtn.addActionListener(e -> addStable());

        JButton removeStableBtn = createStyledButton("❌ Usuń Stadninę", new Color(220, 80, 80));
        removeStableBtn.addActionListener(e -> removeStable());

        JButton sortBtn = createStyledButton("📊 Sortuj wg Zapełnienia", new Color(100, 149, 237));
        sortBtn.addActionListener(e -> sortStablesByLoad());

        btnPanel.add(addStableBtn);
        btnPanel.add(removeStableBtn);
        btnPanel.add(sortBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHorsesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(60, 179, 113), 2),
                "Konie w wybranej stadninie",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(60, 179, 113)
        ));

        // Tabela koni
        horseModel = new HorseTableModel();
        horsesTable = new JTable(horseModel);
        horsesTable.setRowHeight(25);
        horsesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        horsesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        horsesTable.getTableHeader().setBackground(new Color(60, 179, 113));
        horsesTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(horsesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel filtrowania i operacji
        JPanel controlPanel = new JPanel(new BorderLayout(5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Filtry
        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtrowanie"));

        filterPanel.add(new JLabel("Nazwa:"));
        filterField = new JTextField();
        filterField.addActionListener(e -> applyNameFilter());
        filterPanel.add(filterField);

        filterPanel.add(new JLabel("Status:"));
        String[] statuses = {"Wszystkie", "Zdrowy", "Chory", "Trening", "Karencja", "Sprzedany"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.addActionListener(e -> applyStatusFilter());
        filterPanel.add(statusComboBox);

        controlPanel.add(filterPanel, BorderLayout.NORTH);

        // Przyciski operacji na koniach
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        
        JButton addHorseBtn = createStyledButton("➕ Dodaj Konia", new Color(60, 179, 113));
        addHorseBtn.addActionListener(e -> addHorse());

        JButton removeHorseBtn = createStyledButton("❌ Usuń Konia", new Color(220, 80, 80));
        removeHorseBtn.addActionListener(e -> removeHorse());

        btnPanel.add(addHorseBtn);
        btnPanel.add(removeHorseBtn);

        controlPanel.add(btnPanel, BorderLayout.SOUTH);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        return btn;
    }

    private void loadStables() {
        List<StableFacade.StableInfo> stables = facade.getAllStablesInfo();
        stableModel.setStables(stables);
    }

    private void onStableSelected() {
        int selectedRow = stablesTable.getSelectedRow();
        if (selectedRow >= 0) {
            StableFacade.StableInfo info = stableModel.getStableAt(selectedRow);
            selectedStableName = info.getName();
            loadHorses();
        }
    }

    private void loadHorses() {
        if (selectedStableName != null) {
            List<Horse> horses = facade.getHorsesInStable(selectedStableName);
            horseModel.setHorses(horses);
            filterField.setText("");
            statusComboBox.setSelectedIndex(0);
        }
    }

    private void addStable() {
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Nazwa:"));
        panel.add(nameField);
        panel.add(new JLabel("Pojemność:"));
        panel.add(capacityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Dodaj nową stadninę",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                facade.addStable(name, capacity);
                loadStables();
                JOptionPane.showMessageDialog(this, "Stadnina dodana pomyślnie!");
            } catch (StableException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pojemność musi być liczbą!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeStable() {
        int selectedRow = stablesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz stadninę do usunięcia!", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StableFacade.StableInfo info = stableModel.getStableAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Czy na pewno usunąć stadninę: " + info.getName() + "?",
                "Potwierdzenie", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.removeStable(info.getName());
                loadStables();
                horseModel.setHorses(List.of());
                selectedStableName = null;
                JOptionPane.showMessageDialog(this, "Stadnina usunięta!");
            } catch (StableException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void sortStablesByLoad() {
        List<StableFacade.StableInfo> stables = facade.getAllStablesInfo();
        facade.sortStablesByLoad(stables);
        stableModel.setStables(stables);
        JOptionPane.showMessageDialog(this, "Posortowano stadniny według zapełnienia!");
    }

    private void addHorse() {
        if (selectedStableName == null) {
            JOptionPane.showMessageDialog(this, "Wybierz najpierw stadninę!", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField();
        JTextField breedField = new JTextField();
        JComboBox<HorseType> typeCombo = new JComboBox<>(HorseType.values());
        JComboBox<HorseCondition> statusCombo = new JComboBox<>(HorseCondition.values());
        JTextField ageField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField weightField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.add(new JLabel("Imię:"));
        panel.add(nameField);
        panel.add(new JLabel("Rasa:"));
        panel.add(breedField);
        panel.add(new JLabel("Typ:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);
        panel.add(new JLabel("Wiek:"));
        panel.add(ageField);
        panel.add(new JLabel("Cena (PLN):"));
        panel.add(priceField);
        panel.add(new JLabel("Waga (kg):"));
        panel.add(weightField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Dodaj nowego konia",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                facade.addHorse(selectedStableName,
                        nameField.getText().trim(),
                        breedField.getText().trim(),
                        (HorseType) typeCombo.getSelectedItem(),
                        (HorseCondition) statusCombo.getSelectedItem(),
                        Integer.parseInt(ageField.getText().trim()),
                        Double.parseDouble(priceField.getText().trim()),
                        Double.parseDouble(weightField.getText().trim()));
                loadHorses();
                loadStables(); // Odśwież również stadniny (zmienia się zapełnienie)
                JOptionPane.showMessageDialog(this, "Koń dodany pomyślnie!");
            } catch (HorseException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Wprowadź poprawne wartości liczbowe!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeHorse() {
        int selectedRow = horsesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Wybierz konia do usunięcia!", "Błąd", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Horse horse = horseModel.getHorseAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Czy na pewno usunąć konia: " + horse.getName() + "?",
                "Potwierdzenie", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.removeHorse(selectedStableName, horse);
                loadHorses();
                loadStables();
                JOptionPane.showMessageDialog(this, "Koń usunięty!");
            } catch (HorseException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void applyNameFilter() {
        if (selectedStableName == null) return;
        String filter = filterField.getText().trim();
        List<Horse> filtered = facade.filterHorsesByName(selectedStableName, filter);
        horseModel.setHorses(filtered);
    }

    private void applyStatusFilter() {
        if (selectedStableName == null) return;
        int index = statusComboBox.getSelectedIndex();
        HorseCondition condition = index == 0 ? null : HorseCondition.values()[index - 1];
        List<Horse> filtered = facade.filterHorsesByCondition(selectedStableName, condition);
        horseModel.setHorses(filtered);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Czy na pewno chcesz się wylogować?",
                "Wylogowanie", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }
}
