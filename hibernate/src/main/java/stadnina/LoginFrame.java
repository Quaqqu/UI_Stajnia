package stadnina;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private StableFacade facade;

    public LoginFrame() {
        this.facade = new StableFacade();
        initUI();
    }

    private void initUI() {
        setTitle("System Stadniny - Logowanie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Login:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Hasło:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginBtn = new JButton("Zaloguj");
        loginBtn.addActionListener(e -> login());
        add(loginBtn);
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.equals("admin") && pass.equals("admin")) {
            dispose();
            new AdminFrame(facade).setVisible(true);
        } else if (user.equals("client") && pass.equals("client")) {
            dispose();
            new ClientFrame(facade).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Błąd! Spróbuj admin/admin");
        }
    }
}