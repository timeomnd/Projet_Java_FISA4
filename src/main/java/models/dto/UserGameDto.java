package models.dto;

public class UserGameDto {
    private int idAuthentication;
    private int idGames;
    private boolean played;
    private boolean platinum;
    private boolean is100Percent;
    private boolean wishlistGames;
    private boolean wishlistPlatinum;
    private Integer gameRating;
    private Integer platinumDifficulty;
    private Double timePlayedHours;

    public UserGameDto(int idAuthentication, int idGames, boolean played, boolean platinum,
                       boolean is100Percent, boolean wishlistGames, boolean wishlistPlatinum,
                       Integer gameRating, Integer platinumDifficulty, Double timePlayedHours) {
        this.idAuthentication = idAuthentication;
        this.idGames = idGames;
        this.played = played;
        this.platinum = platinum;
        this.is100Percent = is100Percent;
        this.wishlistGames = wishlistGames;
        this.wishlistPlatinum = wishlistPlatinum;
        this.gameRating = gameRating;
        this.platinumDifficulty = platinumDifficulty;
        this.timePlayedHours = timePlayedHours;
    }

    public int getIdAuthentication() { return idAuthentication; }
    public int getIdGames() { return idGames; }
    public boolean isPlayed() { return played; }
    public boolean isPlatinum() { return platinum; }
    public boolean is100Percent() { return is100Percent; }
    public boolean isWishlistGames() { return wishlistGames; }
    public boolean isWishlistPlatinum() { return wishlistPlatinum; }
    public Integer getGameRating() { return gameRating; }
    public Integer getPlatinumDifficulty() { return platinumDifficulty; }
    public Double getTimePlayedHours() { return timePlayedHours; }
}
