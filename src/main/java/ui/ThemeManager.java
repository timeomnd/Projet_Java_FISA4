package ui;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public static void applyPlayStationTheme() {
        try {
            // 1. Activer le thème moderne "Nimbus" de Java
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // 2. Définir la palette de couleurs PlayStation
            Color psDarkBg = new Color(15, 15, 25);         // Fond sombre profond
            Color psLightBg = new Color(25, 25, 35);        // Fond des listes et champs texte
            Color psBlue = new Color(0, 67, 156);           // Bleu classique PlayStation
            Color textWhite = new Color(240, 240, 240);     // Texte blanc cassé (plus doux pour les yeux)
            Color platinumColor = new Color(180, 190, 200); // Gris bleuté pour le platine

            // 3. Injecter les couleurs dans le système
            UIManager.put("control", psDarkBg);                  // Fond de base des fenêtres
            UIManager.put("nimbusBase", psBlue);                 // Couleur principale (barres, scroll, etc.)
            UIManager.put("nimbusLightBackground", psLightBg);   // Fond des JList et JTextField
            UIManager.put("nimbusSelectionBackground", psBlue);  // Couleur quand on clique sur un jeu
            UIManager.put("text", textWhite);                    // Couleur par défaut du texte

            // Personnalisation des textes spécifiques
            UIManager.put("Label.foreground", textWhite);
            UIManager.put("CheckBox.foreground", textWhite);
            UIManager.put("TitledBorder.titleColor", psBlue);

            // Personnalisation des boutons
            UIManager.put("Button.background", psBlue);
            UIManager.put("Button.textForeground", Color.WHITE);
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));

        } catch (Exception e) {
            System.out.println("Erreur lors du chargement du thème : " + e.getMessage());
        }
    }
}