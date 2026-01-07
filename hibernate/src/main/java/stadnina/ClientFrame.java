package stadnina;

import javax.swing.*;

public class ClientFrame extends JFrame {
    public ClientFrame(StableFacade facade) {
        setTitle("Panel Klienta");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new JLabel("Panel Klienta - Wersja Podstawowa", SwingConstants.CENTER));
    }
}