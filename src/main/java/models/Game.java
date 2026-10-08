package models;

public class Game {
    private int id;
    private String title;
    private String cover;

    public Game(int id, String title, String cover) {
        this.id = id;
        this.title = title;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return this.title;
    }
}