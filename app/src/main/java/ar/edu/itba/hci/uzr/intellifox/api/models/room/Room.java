package ar.edu.itba.hci.uzr.intellifox.api.models.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Room {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("meta")
    @Expose
    private RoomMeta meta;

    public Room() {
    }

    public Room(String name, RoomMeta meta) {
        this.name = name;
        this.meta = meta;
    }

    public Room(String id, String name, RoomMeta meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RoomMeta getMeta() {
        return meta;
    }

    public void setMeta(RoomMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", meta=" + meta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id, room.id) &&
                Objects.equals(name, room.name) &&
                Objects.equals(meta, room.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, meta);
    }
}
