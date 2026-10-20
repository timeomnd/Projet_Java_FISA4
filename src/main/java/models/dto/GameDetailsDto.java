package models.dto;

public class GameDetailsDto {
    private String title;
    private String coverPath;
    private boolean played;
    private boolean platinum;
    private boolean is100Percent;
    private Integer rating;
    private Integer difficulty;
    private Double timePlayed;

    // Constructor to hold all the data needed for the Stats Window
    public GameDetailsDto(String title, String coverPath, boolean played, boolean platinum,
                          boolean is100Percent, Integer rating, Integer difficulty, Double timePlayed) {
        this.title = title;
        this.coverPath = coverPath;
        this.played = played;
        this.platinum = platinum;
        this.is100Percent = is100Percent;
        this.rating = rating;
        this.difficulty = difficulty;
        this.timePlayed = timePlayed;
    }

    // --- Getters ---
    public String getTitle() { return title; }
    public String getCoverPath() { return coverPath; }
    public boolean isPlayed() { return played; }
    public boolean isPlatinum() { return platinum; }
    public boolean is100Percent() { return is100Percent; }
    public Integer getRating() { return rating; }
    public Integer getDifficulty() { return difficulty; }
    public Double getTimePlayed() { return timePlayed; }
}
