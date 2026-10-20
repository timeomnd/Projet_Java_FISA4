package models.dto;

public class GameDto {
    private int id;
    private String title;
    private String coverPath;

    public GameDto(int id, String title, String coverPath) {
        this.id = id;
        this.title = title;
        this.coverPath = coverPath;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getCoverPath() { return coverPath; }

    // Overriding toString is very useful for Java Swing (JComboBox)
    // so it displays the title in the dropdown menu.
    @Override
    public String toString() {
        return title;
    }
}
