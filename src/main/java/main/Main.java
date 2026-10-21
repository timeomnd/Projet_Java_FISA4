package main;

import ui.ThemeManager;
import ui.LoginWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread (Best practice for Swing)
        SwingUtilities.invokeLater(() -> {
            ThemeManager.applyPlayStationTheme();
            new LoginWindow();
        });
    }
}