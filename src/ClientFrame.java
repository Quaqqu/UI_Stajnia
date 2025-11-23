import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Panel klienta - tylko przeglądanie danych
 */
public class ClientFrame extends JFrame {
    private StableFacade facade;
    private JTable stablesTable;
    private JTable horsesTable;
    private StableTableModel stableModel;
    private HorseTableModel horseModel;
    private JTextField filterField;
    private JComboBox<String> statusComboBox;
    private String selectedStableName = null;

    public ClientFrame(StableFacade facade) {
        this.facade = facade;
        initUI();
        loadStables();
    }

    private void initUI() {
        setTitle("Panel Klienta - Przeglądanie Oferty Stadniny");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Panel główny
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 250, 245));

        // Nagłówek
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel z tabelami
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setLeftComponent(createStablesPanel());
        splitPane.setRightComponent(createHorsesPanel());

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Stopka z informacją
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(60, 179, 113));
        JLabel infoLabel = new JLabel("💡 Zainteresowany zakupem? Skontaktuj się z administracją!");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.WHITE);
        footerPanel.add(infoLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 179, 113));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("🐴 KLIENT - Oferta Stadniny");
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
                BorderFactory.createLineBorder(new Color(60, 179, 113), 2),
                "Nasze Stadniny",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(60, 179, 113)
        ));

        // Tabela stadnin
        stableModel = new StableTableModel();
        stablesTable = new JTable(stableModel);
        stablesTable.setRowHeight(25);
        stablesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stablesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        stablesTable.getTableHeader().setBackground(new Color(60, 179, 113));
        stablesTable.getTableHeader().setForeground(Color.WHITE);

        // Wyśrodkowanie
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

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(240, 255, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel infoLabel = new JLabel("📍 Wybierz stadninę, aby zobaczyć dostępne konie");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPanel.add(infoLabel);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHorsesPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                "Dostępne Konie",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(100, 149, 237)
        ));

        // Tabela koni
        horseModel = new HorseTableModel();
        horsesTable = new JTable(horseModel);
        horsesTable.setRowHeight(25);
        horsesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        horsesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        horsesTable.getTableHeader().setBackground(new Color(100, 149, 237));
        horsesTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(horsesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Panel filtrowania (tylko odczyt, bez modyfikacji)
        JPanel filterPanel = new JPanel(new BorderLayout(5, 5));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel filtersGrid = new JPanel(new GridLayout(2, 2, 5, 5));
        filtersGrid.setBorder(BorderFactory.createTitledBorder("Wyszukiwanie"));

        filtersGrid.add(new JLabel("Nazwa:"));
        filterField = new JTextField();
        filterField.addActionListener(e -> applyNameFilter());
        filtersGrid.add(filterField);

        filtersGrid.add(new JLabel("Status:"));
        String[] statuses = {"Wszystkie", "Zdrowy", "Chory", "Trening", "Karencja", "Sprzedany"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.addActionListener(e -> applyStatusFilter());
        filtersGrid.add(statusComboBox);

        filterPanel.add(filtersGrid, BorderLayout.NORTH);

        // Przycisk kontaktu
        JButton contactBtn = new JButton("📞 Zapytaj o kontakt");
        contactBtn.setBackground(new Color(100, 149, 237));
        contactBtn.setForeground(Color.WHITE);
        contactBtn.setFont(new Font("Arial", Font.BOLD, 13));
        contactBtn.setFocusPainted(false);
        contactBtn.addActionListener(e -> requestContact());
        filterPanel.add(contactBtn, BorderLayout.SOUTH);

        panel.add(filterPanel, BorderLayout.SOUTH);

        return panel;
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

    private void requestContact() {
        int selectedRow = horsesTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Proszę wybrać konia, o którym chcesz zapytać!",
                    "Informacja",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Horse horse = horseModel.getHorseAt(selectedRow);
        String message = String.format(
                "Zapytanie o konia:\n\n" +
                "Imię: %s\n" +
                "Rasa: %s\n" +
                "Wiek: %d lat\n" +
                "Cena: %.2f PLN\n\n" +
                "Dziękujemy za zainteresowanie!\n" +
                "Nasz administrator skontaktuje się z Tobą wkrótce.",
                horse.getName(), horse.getBreed(), horse.getAge(), horse.getPrice()
        );

        JOptionPane.showMessageDialog(this,
                message,
                "Zapytanie wysłane",
                JOptionPane.INFORMATION_MESSAGE);
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
