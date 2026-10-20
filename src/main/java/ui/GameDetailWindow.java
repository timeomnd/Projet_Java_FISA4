package ui;

import models.UserGameDetail;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameDetailWindow extends BaseWindow {

    private UserGameDetail gameDetail;

    // UI Components
    private JLabel coverLabel;
    private JLabel titleLabel;
    private JCheckBox playedCheck;
    private JCheckBox platinumCheck;
    private JCheckBox hundredPercentCheck;
    private JLabel ratingLabel;
    private JLabel difficultyLabel;
    private JLabel timePlayedLabel;
    private JButton closeButton;

    public GameDetailWindow(UserGameDetail gameDetail) {
        // Call BaseWindow constructor (which calls initUI)
        super("Game Details", 450, 350);

        // CRITICAL: Prevent closing the entire app when closing this specific window
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.gameDetail = gameDetail;

        // Fill the UI with the actual data from the object
        loadGameData();

        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- WEST PANEL (Cover Image) ---
        coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(150, 210));
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // We add a subtle border around the image area
        coverLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(coverLabel, BorderLayout.WEST);

        // --- CENTER PANEL (Details & Stats) ---
        JPanel detailsPanel = new JPanel(new GridLayout(7, 1, 5, 5));

        titleLabel = new JLabel("Title");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        playedCheck = new JCheckBox("Played");
        playedCheck.setEnabled(false); // Read-only mode

        platinumCheck = new JCheckBox("Platinum Trophy");
        platinumCheck.setEnabled(false);

        hundredPercentCheck = new JCheckBox("100% Completed");
        hundredPercentCheck.setEnabled(false);

        ratingLabel = new JLabel("Rating: ");
        difficultyLabel = new JLabel("Platinum Difficulty: ");
        timePlayedLabel = new JLabel("Time Played: ");

        detailsPanel.add(titleLabel);
        detailsPanel.add(playedCheck);
        detailsPanel.add(platinumCheck);
        detailsPanel.add(hundredPercentCheck);
        detailsPanel.add(ratingLabel);
        detailsPanel.add(difficultyLabel);
        detailsPanel.add(timePlayedLabel);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        // --- SOUTH PANEL (Close Button) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        closeButton = new JButton("Close");
        // Use a lambda expression to close the window
        closeButton.addActionListener(e -> this.dispose());
        bottomPanel.add(closeButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private void loadGameData() {
        if (gameDetail == null) return;

        // Set Texts
        titleLabel.setText(gameDetail.getGame().getTitle());
        this.setTitle(gameDetail.getGame().getTitle() + " - Stats");

        // Set Booleans
        playedCheck.setSelected(gameDetail.isPlayed());
        platinumCheck.setSelected(gameDetail.isPlatinum());
        hundredPercentCheck.setSelected(gameDetail.isHundredPercent());

        // Set Primitives (int, double)
        ratingLabel.setText("Rating: " + gameDetail.getGameRating() + " / 10");
        difficultyLabel.setText("Platinum Difficulty: " + gameDetail.getPlatinumDifficulty() + " / 10");
        timePlayedLabel.setText("Time Played: " + gameDetail.getTimePlayedHours() + " hours");

        // Load Image (handles both internal resources and local PC files)
        try {
            ImageIcon originalIcon = null;
            String coverPath = gameDetail.getGame().getCover();

            if (coverPath.startsWith("/")) {
                URL imageUrl = getClass().getResource(coverPath);
                if (imageUrl != null) originalIcon = new ImageIcon(imageUrl);
            } else {
                originalIcon = new ImageIcon(coverPath); // Charge depuis le PC
            }

            if (originalIcon != null && originalIcon.getIconWidth() > 0) {
                Image scaledImage = originalIcon.getImage().getScaledInstance(150, 210, Image.SCALE_SMOOTH);
                coverLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                coverLabel.setText("No Image");
            }
        } catch (Exception ex) {
            coverLabel.setText("No Image");
        }
    }
}