package ar.edu.itba.hci.uzr.intellifox.ui.devices;

public class DeviceType {
    private int id, icon;
    private String name, description;

    public DeviceType(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIcon(int icon) { this.icon = icon; }

    public int getIcon() {
        return this.icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
