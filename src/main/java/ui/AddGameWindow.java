package ui;

import models.Game;
import models.UserGameDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGameWindow extends BaseWindow implements ActionListener {

    // Reference to the dashboard to update the list
    private DashboardWindow dashboard;

    // Form components
    private JTextField titleField;
    private JCheckBox playedCheck;
    private JCheckBox platinumCheck;
    private JCheckBox hundredPercentCheck;
    private JSpinner ratingSpinner;
    private JSpinner difficultySpinner;
    private JTextField timePlayedField;

    private JButton saveButton;
    private JButton cancelButton;

    public AddGameWindow(DashboardWindow dashboard) {
        super("Add a New Game", 400, 450);
        this.dashboard = dashboard;

        // Prevent closing the whole app
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- CENTER PANEL (Form Grid) ---
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 15));

        formPanel.add(new JLabel("Game Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

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
        // JSpinner is perfect for number selection
        ratingSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        formPanel.add(ratingSpinner);

        formPanel.add(new JLabel("Platinum Difficulty (0-10):"));
        difficultySpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        formPanel.add(difficultySpinner);

        formPanel.add(new JLabel("Time Played (Hours):"));
        timePlayedField = new JTextField("0.0");
        formPanel.add(timePlayedField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // --- SOUTH PANEL (Buttons) ---
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
            this.dispose(); // Just close the window
        }
        else if (e.getSource() == saveButton) {
            saveGame();
        }
    }

    private void saveGame() {
        String title = titleField.getText().trim();

        // Basic validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a game title.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse time played safely
        double timePlayed = 0.0;
        try {
            timePlayed = Double.parseDouble(timePlayedField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number for Time Played (e.g. 12.5).",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve values from the UI
        int rating = (int) ratingSpinner.getValue();
        int difficulty = (int) difficultySpinner.getValue();
        int newId = dashboard.getLibrarySize() + 1; // Fake ID generation

        // Create the objects
        Game newGame = new Game(newId, title, "/covers/default.jpg");

        UserGameDetail newDetail = new UserGameDetail(
                newId,
                playedCheck.isSelected(),
                platinumCheck.isSelected(),
                hundredPercentCheck.isSelected(),
                false, // wishlistGames
                false, // wishlistPlatinum
                rating,
                difficulty,
                timePlayed,
                null, // UserAccount (null for now)
                newGame
        );

        // Send data back to the Dashboard
        dashboard.addGameToLibrary(newDetail);

        JOptionPane.showMessageDialog(this,
                title + " has been added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose(); // Close window after saving
    }
}