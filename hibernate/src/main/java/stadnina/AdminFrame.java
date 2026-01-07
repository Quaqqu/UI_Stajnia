package stadnina;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminFrame extends JFrame {
    private StableFacade facade;
    private StableTableModel stableModel;
    private HorseTableModel horseModel;
    private JTable stableTable;
    private JTable horseTable;

    public AdminFrame(StableFacade facade) {
        this.facade = facade;
        initUI();
        refreshData();
    }

    private void initUI() {
        setTitle("System Zarządzania Stadniną (Full Lab 4)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- MODELE I TABELE ---
        stableModel = new StableTableModel();
        horseModel = new HorseTableModel();

        // Panel tabeli Stadnin
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Stadniny"));
        stableTable = new JTable(stableModel);
        topPanel.add(new JScrollPane(stableTable), BorderLayout.CENTER);

        // Panel tabeli Koni
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Konie (Wybierz stadninę powyżej)"));
        horseTable = new JTable(horseModel);
        bottomPanel.add(new JScrollPane(horseTable), BorderLayout.CENTER);

        // SplitPane (Podział góra/dół)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        // --- PANEL PRZYCISKÓW ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 5, 5, 5)); // 2 rzędy przycisków

        // -- WIERSZ 1: STADNINY I PLIKI --
        JButton refreshBtn = new JButton("Odśwież (DB)");
        refreshBtn.addActionListener(e -> refreshData());

        JButton addStableBtn = new JButton("Dodaj Stadninę");
        addStableBtn.setBackground(new Color(220, 255, 220)); // Jasnozielony
        addStableBtn.addActionListener(e -> addStableAction());

        JButton removeStableBtn = new JButton("USUŃ Stadninę");
        removeStableBtn.setBackground(new Color(255, 200, 200)); // Jasnoczerwony
        removeStableBtn.addActionListener(e -> removeStableAction());

        JButton csvBtn = new JButton("Eksport CSV");
        csvBtn.addActionListener(e -> {
            try {
                facade.exportCSV("stadnina_export.csv");
                JOptionPane.showMessageDialog(this, "Zapisano do pliku 'stadnina_export.csv'!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Błąd CSV: " + ex.getMessage()); }
        });

        JButton binSaveBtn = new JButton("Zapisz Binarnie");
        binSaveBtn.addActionListener(e -> {
            try {
                facade.saveBinary("stadnina_backup.bin");
                JOptionPane.showMessageDialog(this, "Zapisano stan do 'stadnina_backup.bin'!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Błąd zapisu: " + ex.getMessage()); }
        });

        // -- WIERSZ 2: KONIE I WCZYTYWANIE --
        JButton addHorseBtn = new JButton("Dodaj Konia");
        addHorseBtn.setBackground(new Color(220, 255, 220));
        addHorseBtn.addActionListener(e -> addHorseAction());

        JButton editHorseBtn = new JButton("Edytuj Konia");
        editHorseBtn.addActionListener(e -> editHorseAction());

        JButton rateHorseBtn = new JButton("Oceń Konia");
        rateHorseBtn.setBackground(new Color(255, 255, 200)); // Jasnożółty
        rateHorseBtn.addActionListener(e -> rateHorseAction());

        JButton removeHorseBtn = new JButton("USUŃ Konia");
        removeHorseBtn.setBackground(new Color(255, 200, 200));
        removeHorseBtn.addActionListener(e -> removeHorseAction());

        JButton binLoadBtn = new JButton("Wczytaj (Podgląd)");
        binLoadBtn.addActionListener(e -> loadBinaryAction());

        // Dodanie przycisków do panelu
        buttonPanel.add(refreshBtn);
        buttonPanel.add(addStableBtn);
        buttonPanel.add(removeStableBtn);
        buttonPanel.add(csvBtn);
        buttonPanel.add(binSaveBtn);

        buttonPanel.add(addHorseBtn);
        buttonPanel.add(editHorseBtn);
        buttonPanel.add(rateHorseBtn);
        buttonPanel.add(removeHorseBtn);
        buttonPanel.add(binLoadBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Obsługa kliknięcia w tabelę stadnin (filtrowanie koni)
        stableTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && stableTable.getSelectedRow() != -1) {
                // Pobieramy nazwę, ale musimy sprawdzić czy to obiekt Info czy Stable (zależnie od trybu)
                // Model zwraca String w kolumnie 0, więc jest bezpiecznie.
                String stableName = (String) stableModel.getValueAt(stableTable.getSelectedRow(), 0);

                // Jeśli jesteśmy w trybie "Podglądu" (po wczytaniu binarnym),
                // konie nie będą pobierane z bazy, ale z pamięci managera?
                // W trybie podglądu, konie dla wybranej stadniny musimy wyciągnąć "ręcznie" jeśli nie są w bazie.
                // Uproszczenie: LoadBinaryAction ładuje dane do UI, więc kliknięcie tutaj
                // odświeży konie z bazy (co może wyczyścić podgląd koni).
                // Aby podgląd działał idealnie, trzeba by przełączyć aplikację w tryb offline,
                // ale na potrzeby lab to wystarczy:
                loadHorsesForStable(stableName);
            }
        });
    }

    // --- METODY POMOCNICZE ---

    private void refreshData() {
        // Pobiera aktualny stan z bazy danych
        List<Stable> stables = facade.getAllStables();
        stableModel.setStables(convertToInfo(stables));
        horseModel.setHorses(new ArrayList<>()); // Czyścimy tabelę koni
    }

    private void loadHorsesForStable(String stableName) {
        List<Horse> horses = facade.getHorsesInStable(stableName);
        horseModel.setHorses(horses);
    }

    // Pomocnicza metoda konwersji Stable -> StableInfo (dla tabeli)
    private List<StableFacade.StableInfo> convertToInfo(List<Stable> stables) {
        List<StableFacade.StableInfo> infos = new ArrayList<>();
        for (Stable s : stables) {
            double load = (s.getMaxCapacity() == 0) ? 0 :
                    (double) s.getStableHorses().size() / s.getMaxCapacity() * 100;

            infos.add(new StableFacade.StableInfo(
                    s.getStableName(),
                    s.getStableHorses().size(),
                    s.getMaxCapacity(),
                    load
            ));
        }
        return infos;
    }

    // --- AKCJE PRZYCISKÓW ---

    private void addStableAction() {
        String name = JOptionPane.showInputDialog(this, "Podaj nazwę stadniny:");
        if(name != null && !name.trim().isEmpty()) {
            try {
                facade.addStable(name, 10);
                refreshData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage()); }
        }
    }

    private void removeStableAction() {
        int selectedRow = stableTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zaznacz stadninę do usunięcia!");
            return;
        }
        String stableName = (String) stableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Usunąć stadninę '" + stableName + "'?\nZostaną usunięte także wszystkie konie!",
                "Potwierdź", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                facade.removeStable(stableName);
                refreshData();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage()); }
        }
    }

    private void addHorseAction() {
        int selectedRow = stableTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zaznacz stadninę, do której chcesz dodać konia!");
            return;
        }
        String stableName = (String) stableModel.getValueAt(selectedRow, 0);
        String name = JOptionPane.showInputDialog(this, "Podaj imię konia:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                // Domyślne wartości, resztę można edytować
                facade.addHorse(stableName, name, "Nieznana", HorseType.GORACOKRWISTY,
                        HorseCondition.ZDROWY, 3, 5000.0, 400.0);
                loadHorsesForStable(stableName);
                refreshData(); // Odświeża licznik koni w stadninie
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Błąd: " + ex.getMessage()); }
        }
    }

    private void removeHorseAction() {
        int selectedRow = horseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zaznacz konia do usunięcia!");
            return;
        }
        Horse horse = horseModel.getHorseAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this, "Usunąć konia " + horse.getName() + "?", "Potwierdź", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String stableName = horse.getStable().getStableName();
            facade.removeHorse(horse);
            loadHorsesForStable(stableName);
            refreshData();
        }
    }

    private void editHorseAction() {
        int selectedRow = horseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zaznacz konia do edycji!");
            return;
        }
        Horse horse = horseModel.getHorseAt(selectedRow);

        // Formularz edycji
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField nameF = new JTextField(horse.getName());
        JTextField breedF = new JTextField(horse.getBreed());
        JComboBox<HorseType> typeBox = new JComboBox<>(HorseType.values()); typeBox.setSelectedItem(horse.getType());
        JComboBox<HorseCondition> statusBox = new JComboBox<>(HorseCondition.values()); statusBox.setSelectedItem(horse.getStatus());
        JSpinner ageS = new JSpinner(new SpinnerNumberModel(horse.getAge(), 0, 40, 1));
        JTextField priceF = new JTextField(String.valueOf(horse.getPrice()));
        JTextField weightF = new JTextField(String.valueOf(horse.getWeight()));

        panel.add(new JLabel("Imię:")); panel.add(nameF);
        panel.add(new JLabel("Rasa:")); panel.add(breedF);
        panel.add(new JLabel("Typ:")); panel.add(typeBox);
        panel.add(new JLabel("Status:")); panel.add(statusBox);
        panel.add(new JLabel("Wiek:")); panel.add(ageS);
        panel.add(new JLabel("Cena:")); panel.add(priceF);
        panel.add(new JLabel("Waga:")); panel.add(weightF);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edycja konia", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                horse.setName(nameF.getText());
                horse.setBreed(breedF.getText());
                horse.setType((HorseType) typeBox.getSelectedItem());
                horse.setStatus((HorseCondition) statusBox.getSelectedItem());
                horse.setAge((Integer) ageS.getValue());
                horse.setPrice(Double.parseDouble(priceF.getText().replace(",", ".")));
                horse.setWeight(Double.parseDouble(weightF.getText().replace(",", ".")));

                facade.updateHorse(horse);
                loadHorsesForStable(horse.getStable().getStableName());
            } catch(Exception e) { JOptionPane.showMessageDialog(this, "Błąd danych: " + e.getMessage()); }
        }
    }

    private void rateHorseAction() {
        int selectedRow = horseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zaznacz konia do oceny!");
            return;
        }
        Horse horse = horseModel.getHorseAt(selectedRow);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JSlider slider = new JSlider(0, 5, 5);
        slider.setMajorTickSpacing(1); slider.setPaintTicks(true); slider.setPaintLabels(true);
        JTextField descField = new JTextField();

        panel.add(new JLabel("Ocena:")); panel.add(slider);
        panel.add(new JLabel("Opis:")); panel.add(descField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Oceń konia: " + horse.getName(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            facade.addRating(horse, slider.getValue(), descField.getText());
            JOptionPane.showMessageDialog(this, "Dodano ocenę!");
            loadHorsesForStable(horse.getStable().getStableName());
        }
    }

    // --- KLUCZOWE: WCZYTYWANIE BINARNE (TRYB PODGLĄDU) ---
    private void loadBinaryAction() {
        try {
            List<Stable> loadedStables = facade.loadBinary("stadnina_backup.bin");

            // Konwertujemy wczytane dane do widoku tabeli
            List<StableFacade.StableInfo> infos = convertToInfo(loadedStables);

            // Ustawiamy dane w tabeli (tylko w UI!)
            stableModel.setStables(infos);
            horseModel.setHorses(new ArrayList<>()); // Czyścimy podgląd koni

            JOptionPane.showMessageDialog(this,
                    "TRYB PODGLĄDU ARCHIWUM\n" +
                            "Wyświetlane są dane z pliku. Baza danych nie została zmieniona.\n" +
                            "Możesz przeglądać listę stadnin.\n" +
                            "Aby wrócić do normalnej pracy, kliknij 'Odśwież (DB)'.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Błąd wczytywania pliku (czy na pewno istnieje?): " + ex.getMessage());
        }
    }
}