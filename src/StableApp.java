import javax.swing.*;

/**
 * Główna klasa aplikacji - punkt startowy
 */
public class StableApp {
    public static void main(String[] args) {
        // Uruchomienie w wątku Swing (EDT - Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                // Ustawienie Look and Feel dla lepszego wyglądu
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Uruchomienie ekranu logowania
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
