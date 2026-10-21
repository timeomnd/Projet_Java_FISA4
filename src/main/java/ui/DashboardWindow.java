package ui;

import dao.GameDao;
import dao.UserGamesDao;
import models.Game;
import models.UserGameDetail;
import models.dto.GameDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DashboardWindow extends BaseWindow implements ActionListener {

    private int activeUserId; // Stores the logged-in user ID
    private JButton wishlistButton;
    private JButton addGameButton;
    private JButton viewDetailsButton;
    private JButton logoutButton;
    private JCheckBox platinumFilterCheckbox;
    private JList<Game> gameList;
    private DefaultListModel<Game> listModel;

    private List<UserGameDetail> library;
    private List<Game> globalCatalog;

    public DashboardWindow(int activeUserId) {
        super("My Games Library - Dashboard", 700, 500);
        this.activeUserId = activeUserId;
        this.library = new ArrayList<>();
        this.globalCatalog = new ArrayList<>();

        loadGlobalCatalog();
        loadLibraryFromDatabase();

        refreshList(false);
        this.setVisible(true);
    }

    public int getActiveUserId() {
        return activeUserId;
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

    public void loadGlobalCatalog() {
        globalCatalog.clear();
        GameDao gameDao = new GameDao();
        List<GameDto> dtos = gameDao.getAllGames();
        for(GameDto dto : dtos) {
            globalCatalog.add(new Game(dto.getId(), dto.getTitle(), dto.getCoverPath()));
        }
    }

    public void loadLibraryFromDatabase() {
        UserGamesDao userGamesDao = new UserGamesDao();
        this.library = userGamesDao.getUserLibrary(activeUserId);
        refreshList(platinumFilterCheckbox != null && platinumFilterCheckbox.isSelected());
    }

    private void refreshList(boolean onlyPlatinum) {
        if (listModel == null) return;

        listModel.clear();
        for (UserGameDetail detail : library) {
            if (!detail.isWishlistGames()) {
                if (!onlyPlatinum || detail.isPlatinum()) {
                    listModel.addElement(detail.getGame());
                }
            }
        }
    }

    public int getLibrarySize() { return library.size(); }
    public List<UserGameDetail> getLibrary() { return library; }
    public List<Game> getGlobalCatalog() { return globalCatalog; }

    public void addGameToLibrary(UserGameDetail newGameDetail) {
        library.add(newGameDetail);
        refreshList(platinumFilterCheckbox.isSelected());
    }

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

                    if (coverPath == null || coverPath.isEmpty()) {
                        label.setIcon(null);
                        return label;
                    }

                    if (coverPath.startsWith("/")) {
                        // Project integrated image
                        java.net.URL imageUrl = getClass().getResource(coverPath);
                        if (imageUrl != null) {
                            originalIcon = new ImageIcon(imageUrl);
                        } else {
                            System.err.println("IMAGE INTROUVABLE DANS LE PROJET : " + coverPath);
                        }
                    } else {
                        // Image loaded from the PC
                        java.io.File file = new java.io.File(coverPath);
                        if (file.exists()) {
                            originalIcon = new ImageIcon(coverPath);
                        } else {
                            System.err.println("IMAGE INTROUVABLE SUR LE PC : " + coverPath);
                        }
                    }

                    if (originalIcon != null && originalIcon.getIconWidth() > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 70, Image.SCALE_SMOOTH);
                        label.setIcon(new ImageIcon(scaledImage));
                    } else {
                        label.setIcon(null);
                    }
                } catch (Exception ex) {
                    System.err.println("Erreur de chargement pour " + game.getTitle() + " : " + ex.getMessage());
                    label.setIcon(null);
                }
            }
            return label;
        }
    }
}