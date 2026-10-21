package ui;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public static void applyPlayStationTheme() {
        try {
            // Enable Java's modern "Nimbus" theme
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // Define the PlayStation color palette
            Color psDarkBg = new Color(15, 15, 25);         // Deep dark background
            Color psLightBg = new Color(25, 25, 35);        // Background for lists and text fields
            Color psBlue = new Color(0, 67, 156);           // Classic PlayStation blue
            Color textWhite = new Color(240, 240, 240);     // Off-white text (easier on the eyes)
            Color platinumColor = new Color(180, 190, 200); // Blue-grey for platinum

            // Inject colors into the system
            UIManager.put("control", psDarkBg);                  // Base background for windows
            UIManager.put("nimbusBase", psBlue);                 // Main color (scrollbars, etc.)
            UIManager.put("nimbusLightBackground", psLightBg);   // Background for JList and JTextField
            UIManager.put("nimbusSelectionBackground", psBlue);  // Color when a game is clicked/selected
            UIManager.put("text", textWhite);                    // Default text color

            // Customization for specific texts
            UIManager.put("Label.foreground", textWhite);
            UIManager.put("CheckBox.foreground", textWhite);
            UIManager.put("TitledBorder.titleColor", psBlue);

            // Button customization
            UIManager.put("Button.background", psBlue);
            UIManager.put("Button.textForeground", Color.WHITE);
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));

        } catch (Exception e) {
            System.out.println("Error loading theme: " + e.getMessage());
        }
    }
}