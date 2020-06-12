package ar.edu.itba.hci.uzr.intellifox.ui.routines;

public class Routine {
    private int id;
    private String name, color;

    public Routine(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public void setId(int id) { this.id = id; }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
