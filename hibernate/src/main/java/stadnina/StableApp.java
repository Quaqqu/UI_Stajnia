package stadnina;

import javax.swing.*;

public class StableApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // To uruchomi Hibernate przy starcie
                HibernateUtil.getSessionFactory();
                new LoginFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}