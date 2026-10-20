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

    // NOUVEAU : Le catalogue global de la BDD
    private List<Game> globalCatalog;

    public DashboardWindow() {
        super("My Games Library - Dashboard", 700, 500);
        library = new ArrayList<>();
        globalCatalog = new ArrayList<>();

        loadGlobalCatalog(); // Charge tous les jeux de la BDD
        loadMockData();      // Charge la bibliothèque de l'utilisateur

        refreshList(false);
        this.setVisible(true);
    }

    @Override
    protected void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setCellRenderer(new GameListRenderer());

        JScrollPane scrollPane = new JScrollPane(gameList);

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

    // --- NOUVELLE MÉTHODE ---
    private void loadGlobalCatalog() {
        // Dans la version finale, tu appelleras ton GameDao ici :
        // List<GameDto> dtos = new GameDao().getAllGames();
        // for(GameDto dto : dtos) { globalCatalog.add(new Game(dto.getId(), dto.getTitle(), dto.getCover())); }

        // Simulation du catalogue BDD pour tester le filtre :
        globalCatalog.add(new Game(1, "Elden Ring", "/covers/eldenring.jpeg"));
        globalCatalog.add(new Game(2, "The Last of Us", "/covers/thelastofus.jpeg"));
        globalCatalog.add(new Game(3, "Cyberpunk 2077", "/covers/cyberpunk.jpeg"));
        globalCatalog.add(new Game(4, "Ghost of Tsushima", "/covers/default.jpeg"));
        globalCatalog.add(new Game(5, "God of War", "/covers/default.jpeg"));
        globalCatalog.add(new Game(6, "Grand Theft Auto V", "/covers/default.jpeg"));
    }

    private void loadMockData() {
        Registration reg = new Registration(1, "John", "Doe", "john@email.com", "2026-10-15 10:00:00");
        UserAccount user = new UserAccount(1, "admin", "1234", reg);

        // L'utilisateur a déjà le jeu ID 1 dans sa bibliothèque
        library.add(new UserGameDetail(1, true, true, true, false, false, 10, 8, 120.5, user, globalCatalog.get(0)));
    }

    private void refreshList(boolean onlyPlatinum) {
        listModel.clear();
        for (UserGameDetail detail : library) {
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

    public int getLibrarySize() { return library.size(); }
    public List<UserGameDetail> getLibrary() { return library; }

    // Fournit le catalogue global pour l'autocomplétion
    public List<Game> getGlobalCatalog() { return globalCatalog; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginWindow();
            }
        }
        else if (e.getSource() == wishlistButton) {
            new WishlistWindow(this);
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
                JOptionPane.showMessageDialog(this, "Please select a game first.", "No selection", JOptionPane.WARNING_MESSAGE);
            } else {
                UserGameDetail selectedDetail = null;
                for (UserGameDetail detail : library) {
                    if (detail.getGame().getId() == selectedGame.getId()) { selectedDetail = detail; break; }
                }
                if (selectedDetail != null) { new GameDetailWindow(selectedDetail); }
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
                try {
                    ImageIcon originalIcon = null;
                    String coverPath = game.getCover();
                    if (coverPath.startsWith("/")) {
                        java.net.URL imageUrl = getClass().getResource(coverPath);
                        if (imageUrl != null) originalIcon = new ImageIcon(imageUrl);
                    } else {
                        originalIcon = new ImageIcon(coverPath);
                    }
                    if (originalIcon != null && originalIcon.getIconWidth() > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaledImage));
                    } else {
                        label.setIcon(null);
                    }
                } catch (Exception ex) { label.setIcon(null); }
            }
            return label;
        }
    }
}