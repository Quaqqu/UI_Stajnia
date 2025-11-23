import javax.swing.*;
import java.awt.*;

/**
 * Okno logowania do systemu
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private StableFacade facade;

    public LoginFrame() {
        this.facade = new StableFacade();
        initUI();
    }

    private void initUI() {
        setTitle("System Zarządzania Stadniną - Logowanie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel główny
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 245));

        // Tytuł
        JLabel titleLabel = new JLabel("Witaj w Systemie Stadniny", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 100));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel formularza
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(new Color(240, 240, 245));

        JLabel userLabel = new JLabel("Login:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel passLabel = new JLabel("Hasło:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Panel przycisków
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));

        JButton adminButton = new JButton("Admin");
        adminButton.setFont(new Font("Arial", Font.BOLD, 14));
        adminButton.setBackground(new Color(70, 130, 180));
        adminButton.setForeground(Color.WHITE);
        adminButton.setFocusPainted(false);
        adminButton.addActionListener(e -> login("admin"));

        JButton clientButton = new JButton("Klient");
        clientButton.setFont(new Font("Arial", Font.BOLD, 14));
        clientButton.setBackground(new Color(60, 179, 113));
        clientButton.setForeground(Color.WHITE);
        clientButton.setFocusPainted(false);
        clientButton.addActionListener(e -> login("client"));

        buttonPanel.add(adminButton);
        buttonPanel.add(clientButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Enter na password field
        passwordField.addActionListener(e -> login("admin"));
    }

    private void login(String role) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Proszę podać login i hasło!",
                    "Błąd logowania",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prosta walidacja (w prawdziwej aplikacji byłaby baza danych)
        boolean validAdmin = role.equals("admin") && username.equals("admin") && password.equals("admin");
        boolean validClient = role.equals("client") && username.equals("client") && password.equals("client");

        if (validAdmin || validClient) {
            dispose();
            if (role.equals("admin")) {
                SwingUtilities.invokeLater(() -> new AdminFrame(facade).setVisible(true));
            } else {
                SwingUtilities.invokeLater(() -> new ClientFrame(facade).setVisible(true));
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Nieprawidłowy login lub hasło!\n\nTestowe dane:\nAdmin: admin/admin\nKlient: client/client",
                    "Błąd logowania",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
