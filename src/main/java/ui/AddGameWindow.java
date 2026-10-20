package ui;

import models.Game;
import models.UserGameDetail;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGameWindow extends BaseWindow implements ActionListener {

    private DashboardWindow dashboard;

    private JTextField titleField;
    private JCheckBox playedCheck;
    private JCheckBox platinumCheck;
    private JCheckBox hundredPercentCheck;
    private JSpinner ratingSpinner;
    private JSpinner difficultySpinner;
    private JTextField timePlayedField;

    // Nouveaux composants pour la jaquette
    private JButton chooseCoverButton;
    private JLabel coverPathLabel;
    private String selectedCoverPath = null; // Par défaut, null

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

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 15)); // Passé à 8 lignes

        formPanel.add(new JLabel("Game Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        // --- NOUVEAU : Champ pour choisir l'image ---
        formPanel.add(new JLabel("Cover Image:"));
        JPanel coverPanel = new JPanel(new BorderLayout(5, 0));
        chooseCoverButton = new JButton("Browse...");
        chooseCoverButton.addActionListener(this);
        coverPathLabel = new JLabel(" Default");
        coverPathLabel.setForeground(Color.GRAY);

        coverPanel.add(chooseCoverButton, BorderLayout.WEST);
        coverPanel.add(coverPathLabel, BorderLayout.CENTER);
        formPanel.add(coverPanel);
        // ---------------------------------------------

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            this.dispose();
        }
        else if (e.getSource() == chooseCoverButton) {
            // Ouvre l'explorateur de fichiers de l'utilisateur
            JFileChooser fileChooser = new JFileChooser();
            // Filtre pour n'accepter que les images
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedCoverPath = fileChooser.getSelectedFile().getAbsolutePath();
                coverPathLabel.setText(" " + fileChooser.getSelectedFile().getName()); // Affiche juste le nom
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

        double timePlayed = 0.0;
        try {
            timePlayed = Double.parseDouble(timePlayedField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Time Played.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int rating = (int) ratingSpinner.getValue();
        int difficulty = (int) difficultySpinner.getValue();
        int newId = dashboard.getLibrarySize() + 1;

        // Si l'utilisateur n'a rien choisi, on garde l'image par défaut
        String finalCoverPath = (selectedCoverPath != null) ? selectedCoverPath : "/covers/default.jpeg";

        Game newGame = new Game(newId, title, finalCoverPath);

        UserGameDetail newDetail = new UserGameDetail(
                newId, playedCheck.isSelected(), platinumCheck.isSelected(), hundredPercentCheck.isSelected(),
                false, false, rating, difficulty, timePlayed, null, newGame
        );

        dashboard.addGameToLibrary(newDetail);
        this.dispose();
    }
}