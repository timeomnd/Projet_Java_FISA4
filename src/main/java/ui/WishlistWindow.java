package ui;

import models.Game;
import models.UserGameDetail;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class WishlistWindow extends BaseWindow implements ActionListener {

    private DashboardWindow dashboard;
    private JList<Game> wishlistList;
    private DefaultListModel<Game> listModel;
    private JButton addWishlistButton;
    private JButton closeButton;

    public WishlistWindow(DashboardWindow dashboard) {
        super("My Wishlist", 500, 400);
        this.dashboard = dashboard;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        refreshWishlist();
        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Games I Want to Play \uD83C\uDFAE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        wishlistList = new JList<>(listModel);
        wishlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wishlistList.setCellRenderer(new WishlistRenderer());

        mainPanel.add(new JScrollPane(wishlistList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addWishlistButton = new JButton("Add to Wishlist");
        closeButton = new JButton("Close");

        addWishlistButton.addActionListener(this);
        closeButton.addActionListener(this);

        bottomPanel.add(addWishlistButton);
        bottomPanel.add(closeButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        this.add(mainPanel);
    }

    public void refreshWishlist() {
        listModel.clear();
        for (UserGameDetail detail : dashboard.getLibrary()) {
            if (detail.isWishlistGames()) {
                listModel.addElement(detail.getGame());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            this.dispose();
        }
        else if (e.getSource() == addWishlistButton) {
            JPanel formPanel = new JPanel(new GridLayout(3, 1, 5, 5));
            JTextField titleField = new JTextField();
            JCheckBox platCheck = new JCheckBox("Aiming for Platinum Trophy");

            formPanel.add(new JLabel("Game Title:"));
            formPanel.add(titleField);

            // --- NOUVEAU : Bouton pour la jaquette dans la wishlist ---
            JPanel coverPanel = new JPanel(new BorderLayout(5, 0));
            JButton chooseCoverBtn = new JButton("Choose Cover...");
            JLabel coverLabel = new JLabel(" Default");
            coverLabel.setForeground(Color.GRAY);

            // On utilise un tableau de 1 élément pour modifier la variable dans le bouton
            final String[] selectedPath = {null};

            chooseCoverBtn.addActionListener(ev -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png"));
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    selectedPath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                    coverLabel.setText(" " + fileChooser.getSelectedFile().getName());
                }
            });

            coverPanel.add(chooseCoverBtn, BorderLayout.WEST);
            coverPanel.add(coverLabel, BorderLayout.CENTER);
            formPanel.add(coverPanel);

            JPanel checksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            checksPanel.add(platCheck);
            formPanel.add(checksPanel);
            // -------------------------------------------------------------

            int result = JOptionPane.showConfirmDialog(this, formPanel,
                    "Add to Wishlist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText().trim();
                if (!title.isEmpty()) {
                    String finalCover = (selectedPath[0] != null) ? selectedPath[0] : "/covers/default.jpeg";
                    Game newGame = new Game(dashboard.getLibrarySize() + 1, title, finalCover);

                    UserGameDetail newDetail = new UserGameDetail(
                            dashboard.getLibrarySize() + 1, false, false, false,
                            true, platCheck.isSelected(),
                            0, 0, 0.0, null, newGame
                    );

                    dashboard.addGameToLibrary(newDetail);
                    refreshWishlist();
                }
            }
        }
    }

    private class WishlistRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Game) {
                Game game = (Game) value;

                String extraInfo = "";
                for (UserGameDetail d : dashboard.getLibrary()) {
                    if (d.getGame().getId() == game.getId() && d.isWishlistGames()) {
                        if (d.isWishlistPlatinum()) extraInfo += " [\uD83C\uDFC6 Plat Aim]";
                        break;
                    }
                }

                label.setText(game.getTitle() + extraInfo);

                // --- LOGIQUE POUR GÉRER RESSOURCE INTERNE / PC LOCAL ---
                try {
                    ImageIcon originalIcon = null;
                    String coverPath = game.getCover();

                    if (coverPath.startsWith("/")) {
                        URL imageUrl = getClass().getResource(coverPath);
                        if (imageUrl != null) originalIcon = new ImageIcon(imageUrl);
                    } else {
                        originalIcon = new ImageIcon(coverPath); // Charge depuis le PC
                    }

                    if (originalIcon != null && originalIcon.getIconWidth() > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(40, 55, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaledImage));
                    } else {
                        label.setIcon(null);
                    }
                } catch (Exception ex) {
                    label.setIcon(null);
                }
            }
            return label;
        }
    }
}