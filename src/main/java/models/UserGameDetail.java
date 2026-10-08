package models;

public class UserGameDetail {
    private int id;
    private boolean played;
    private boolean platinum;
    private boolean hundredPercent;
    private boolean wishlistGames;
    private boolean wishlistPlatinum;
    private int gameRating;
    private int platinumDifficulty;
    private double timePlayedHours;
    private UserAccount account;
    private Game game;

    public UserGameDetail(int id, boolean played, boolean platinum, boolean hundredPercent,
                          boolean wishlistGames, boolean wishlistPlatinum, int gameRating,
                          int platinumDifficulty, double timePlayedHours,
                          UserAccount account, Game game) {
        this.id = id;
        this.played = played;
        this.platinum = platinum;
        this.hundredPercent = hundredPercent;
        this.wishlistGames = wishlistGames;
        this.wishlistPlatinum = wishlistPlatinum;
        this.gameRating = gameRating;
        this.platinumDifficulty = platinumDifficulty;
        this.timePlayedHours = timePlayedHours;
        this.account = account;
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean isPlatinum() {
        return platinum;
    }

    public void setPlatinum(boolean platinum) {
        this.platinum = platinum;
    }

    public boolean isHundredPercent() {
        return hundredPercent;
    }

    public void setHundredPercent(boolean hundredPercent) {
        this.hundredPercent = hundredPercent;
    }

    public boolean isWishlistGames() {
        return wishlistGames;
    }

    public void setWishlistGames(boolean wishlistGames) {
        this.wishlistGames = wishlistGames;
    }

    public boolean isWishlistPlatinum() {
        return wishlistPlatinum;
    }

    public void setWishlistPlatinum(boolean wishlistPlatinum) {
        this.wishlistPlatinum = wishlistPlatinum;
    }

    public int getGameRating() {
        return gameRating;
    }

    public void setGameRating(int gameRating) {
        this.gameRating = gameRating;
    }

    public int getPlatinumDifficulty() {
        return platinumDifficulty;
    }

    public void setPlatinumDifficulty(int platinumDifficulty) {
        this.platinumDifficulty = platinumDifficulty;
    }

    public double getTimePlayedHours() {
        return timePlayedHours;
    }

    public void setTimePlayedHours(double timePlayedHours) {
        this.timePlayedHours = timePlayedHours;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}