package ui;

import dao.GameDao;
import models.Game;
import models.UserGameDetail;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGameWindow extends BaseWindow implements ActionListener {

    private DashboardWindow dashboard;

    private JTextField titleField;
    private JPopupMenu suggestionMenu;
    private Game selectedCatalogGame = null;
    private boolean isSelecting = false; // Sécurité pour le clic

    private JCheckBox playedCheck;
    private JCheckBox platinumCheck;
    private JCheckBox hundredPercentCheck;
    private JSpinner ratingSpinner;
    private JSpinner difficultySpinner;
    private JTextField timePlayedField;

    private JButton chooseCoverButton;
    private JLabel coverPathLabel;
    private String selectedCoverPath = null;

    private JButton saveButton;
    private JButton cancelButton;

    public AddGameWindow(DashboardWindow dashboard) {
        super("Add a New Game", 450, 500);
        this.dashboard = dashboard;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15));

        formPanel.add(new JLabel("Game Title:"));
        titleField = new JTextField();
        suggestionMenu = new JPopupMenu();

        suggestionMenu.setLightWeightPopupEnabled(false);
        suggestionMenu.setFocusable(false);

        titleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filterSuggestions(); }
            @Override public void removeUpdate(DocumentEvent e) { filterSuggestions(); }
            @Override public void changedUpdate(DocumentEvent e) { filterSuggestions(); }
        });

        formPanel.add(titleField);

        formPanel.add(new JLabel("Cover Image:"));
        JPanel coverPanel = new JPanel(new BorderLayout(5, 0));
        chooseCoverButton = new JButton("Browse...");
        chooseCoverButton.addActionListener(this);
        coverPathLabel = new JLabel(" Default");
        coverPathLabel.setForeground(Color.GRAY);

        coverPanel.add(chooseCoverButton, BorderLayout.WEST);
        coverPanel.add(coverPathLabel, BorderLayout.CENTER);
        formPanel.add(coverPanel);

        formPanel.add(new JLabel("Played:"));
        playedCheck = new JCheckBox();
        formPanel.add(playedCheck);

        formPanel.add(new JLabel("Platinum Trophy:"));
        platinumCheck = new JCheckBox();
        formPanel.add(platinumCheck);

        formPanel.add(new JLabel("100% Completed:"));
        hundredPercentCheck = new JCheckBox();
        formPanel.add(hundredPercentCheck);

        formPanel.add(new JLabel("Rating (0-10):"));
        ratingSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        formPanel.add(ratingSpinner);

        formPanel.add(new JLabel("Platinum Difficulty (0-10):"));
        difficultySpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        formPanel.add(difficultySpinner);

        formPanel.add(new JLabel("Time Played (Hours):"));
        timePlayedField = new JTextField("0.0");
        formPanel.add(timePlayedField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.add(mainPanel);
    }

    // Méthode appelée à chaque milliseconde où le texte change
    private void filterSuggestions() {
        if (isSelecting) return; // Si on vient de cliquer sur un jeu, on ne relance pas le filtre

        // On utilise invokeLater pour ne pas bloquer l'interface graphique pendant la frappe
        SwingUtilities.invokeLater(() -> {
            selectedCatalogGame = null;
            chooseCoverButton.setEnabled(true);
            coverPathLabel.setText(" Default");

            String text = titleField.getText().trim().toLowerCase();
            suggestionMenu.removeAll();

            if (text.isEmpty()) {
                suggestionMenu.setVisible(false);
                return;
            }

            boolean found = false;
            for (Game g : dashboard.getGlobalCatalog()) {
                String lowerTitle = g.getTitle().toLowerCase();

                if (lowerTitle.startsWith(text) || lowerTitle.contains(" " + text)) {
                    JMenuItem item = new JMenuItem(g.getTitle());

                    // On utilise le vrai ActionListener, le clic est maintenant garanti
                    item.addActionListener(ev -> {
                        isSelecting = true;
                        titleField.setText(g.getTitle());
                        selectedCatalogGame = g;
                        coverPathLabel.setText(" [Cover from Database]");
                        chooseCoverButton.setEnabled(false);
                        suggestionMenu.setVisible(false);
                        isSelecting = false;
                    });

                    suggestionMenu.add(item);
                    found = true;
                }
            }

            if (found) {
                suggestionMenu.pack(); // Ajuste la taille de la boîte
                suggestionMenu.show(titleField, 0, titleField.getHeight());
            } else {
                suggestionMenu.setVisible(false);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.dispose();
        }
        else if (e.getSource() == chooseCoverButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedCoverPath = fileChooser.getSelectedFile().getAbsolutePath();
                coverPathLabel.setText(" " + fileChooser.getSelectedFile().getName());
            }
        }
        else if (e.getSource() == saveButton) {
            saveGame();
        }
    }

    private void saveGame() {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a game title.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int targetGameId;

        // 1. Check if game already exists or create it
        if (selectedCatalogGame != null) {
            targetGameId = selectedCatalogGame.getId();
        } else {
            // Insert new game into the global catalog
            String finalCoverPath = (selectedCoverPath != null) ? selectedCoverPath : "/covers/default.jpeg";
            GameDao gameDao = new GameDao();
            targetGameId = gameDao.addCustomGame(title, finalCoverPath);

            if(targetGameId == -1) {
                JOptionPane.showMessageDialog(this, "Error creating the game in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // 2. Validate time played
        double timePlayed = 0.0;
        try {
            timePlayed = Double.parseDouble(timePlayedField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number for Time Played.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Save to User Games using DTO
        models.dto.UserGameDto dto = new models.dto.UserGameDto(
                dashboard.getActiveUserId(),
                targetGameId,
                playedCheck.isSelected(),
                platinumCheck.isSelected(),
                hundredPercentCheck.isSelected(),
                false, // wishlistGames
                false, // wishlistPlatinum
                (int) ratingSpinner.getValue(),
                (int) difficultySpinner.getValue(),
                timePlayed
        );

        dao.UserGamesDao userGamesDao = new dao.UserGamesDao();
        userGamesDao.saveUserGame(dto);

        // 4. Reload everything and close
        dashboard.loadGlobalCatalog(); // In case a new game was added
        dashboard.loadLibraryFromDatabase();
        this.dispose();
    }
}