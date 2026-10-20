package ui;

import models.Game;
import models.UserAccount;
import models.UserGameDetail;
import models.Registration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardWindow extends BaseWindow implements ActionListener {

    private JButton wishlistButton;
    private JButton addGameButton;
    private JButton viewDetailsButton;
    private JButton logoutButton;
    private JCheckBox platinumFilterCheckbox;
    private JList<Game> gameList;
    private DefaultListModel<Game> listModel;

    private List<UserGameDetail> library;

    public DashboardWindow() {
        super("My Games Library - Dashboard", 700, 500);
        library = new ArrayList<>();
        loadMockData();
        refreshList(false);
        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JLabel welcomeLabel = new JLabel("Welcome to your Game Library!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        wishlistButton = new JButton("My Wishlist \uD83D\uDCDD");
        wishlistButton.addActionListener(this);

        topLeftPanel.add(welcomeLabel);
        topLeftPanel.add(wishlistButton);

        platinumFilterCheckbox = new JCheckBox("Show only Platinum (100%) games");
        platinumFilterCheckbox.addActionListener(this);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(platinumFilterCheckbox, BorderLayout.EAST);

        // --- CENTER PANEL ---
        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setCellRenderer(new GameListRenderer());

        JScrollPane scrollPane = new JScrollPane(gameList);

        // --- BOTTOM PANEL ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        addGameButton = new JButton("Add a Game");
        viewDetailsButton = new JButton("View Details");
        logoutButton = new JButton("Logout");

        addGameButton.addActionListener(this);
        viewDetailsButton.addActionListener(this);
        logoutButton.addActionListener(this);

        bottomPanel.add(addGameButton);
        bottomPanel.add(viewDetailsButton);
        bottomPanel.add(logoutButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    private void loadMockData() {
        Registration reg = new Registration(1, "John", "Doe", "john@email.com", "2026-10-15 10:00:00");
        UserAccount user = new UserAccount(1, "admin", "1234", reg);

        Game g1 = new Game(1, "Elden Ring", "/covers/eldenring.jpeg");
        Game g2 = new Game(2, "The Last of Us", "/covers/thelastofus.jpeg");
        Game g3 = new Game(3, "Cyberpunk 2077", "/covers/cyberpunk.jpeg");
        Game g4 = new Game(4, "Ghost of Tsushima", "/covers/default.jpeg");

        library.add(new UserGameDetail(1, true, true, true, false, false, 10, 8, 120.5, user, g1));
        library.add(new UserGameDetail(2, true, false, false, false, false, 9, 4, 45.0, user, g2));
        library.add(new UserGameDetail(3, true, false, false, false, false, 8, 6, 80.0, user, g3));

        // Wishlist Mock Data (wishlistGames = true)
        library.add(new UserGameDetail(4, false, false, false, true, true, 0, 0, 0.0, user, g4));
    }

    private void refreshList(boolean onlyPlatinum) {
        listModel.clear();
        for (UserGameDetail detail : library) {
            // Filtrage : n'afficher que les jeux qui NE SONT PAS dans la wishlist
            if (!detail.isWishlistGames()) {
                if (onlyPlatinum) {
                    if (detail.isPlatinum()) {
                        listModel.addElement(detail.getGame());
                    }
                } else {
                    listModel.addElement(detail.getGame());
                }
            }
        }
    }

    public void addGameToLibrary(UserGameDetail newGameDetail) {
        library.add(newGameDetail);
        refreshList(platinumFilterCheckbox.isSelected());
    }

    public int getLibrarySize() {
        return library.size();
    }

    public List<UserGameDetail> getLibrary() {
        return library;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginWindow();
            }
        }
        else if (e.getSource() == wishlistButton) {
            new WishlistWindow(this); // Ouvre la wishlist
        }
        else if (e.getSource() == platinumFilterCheckbox) {
            refreshList(platinumFilterCheckbox.isSelected());
        }
        else if (e.getSource() == addGameButton) {
            new AddGameWindow(this);
        }
        else if (e.getSource() == viewDetailsButton) {
            Game selectedGame = gameList.getSelectedValue();

            if (selectedGame == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a game from the list first.",
                        "No selection",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                UserGameDetail selectedDetail = null;
                for (UserGameDetail detail : library) {
                    if (detail.getGame().getId() == selectedGame.getId()) {
                        selectedDetail = detail;
                        break;
                    }
                }
                if (selectedDetail != null) {
                    new GameDetailWindow(selectedDetail);
                }
            }
        }
    }

    private class GameListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Game) {
                Game game = (Game) value;
                label.setText(game.getTitle());

                // --- LOGIQUE POUR GÉRER RESSOURCE INTERNE / PC LOCAL ---
                try {
                    ImageIcon originalIcon = null;
                    String coverPath = game.getCover();

                    if (coverPath.startsWith("/")) {
                        java.net.URL imageUrl = getClass().getResource(coverPath);
                        if (imageUrl != null) originalIcon = new ImageIcon(imageUrl);
                    } else {
                        originalIcon = new ImageIcon(coverPath); // Charge depuis le PC
                    }

                    if (originalIcon != null && originalIcon.getIconWidth() > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
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