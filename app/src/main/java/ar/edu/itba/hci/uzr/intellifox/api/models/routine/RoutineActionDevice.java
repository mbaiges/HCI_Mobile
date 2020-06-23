package ar.edu.itba.hci.uzr.intellifox.api.models.routine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceMeta;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;

public class RoutineActionDevice<T extends DeviceState>{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose(serialize = false)
    private String name;
    @SerializedName("type")
    @Expose(serialize = false)
    private DeviceType type;
    @SerializedName("room")
    private Room room;
    @Expose(serialize = false)
    private T state;
    @SerializedName("meta")
    @Expose(serialize = false)
    private DeviceMeta meta;

    public RoutineActionDevice() {
    }

    public RoutineActionDevice(String name, DeviceType type, DeviceMeta meta) {
        this.name = name;
        this.type = type;
        this.meta = meta;
    }

    public RoutineActionDevice(String id, DeviceType type, String name, DeviceMeta meta) {
        this.id = id;
        this.type = type;
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

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public DeviceMeta getMeta() {
        return meta;
    }

    public void setMeta(DeviceMeta meta) {
        this.meta = meta;
    }

    public Room getRoom() { return room; }

    public void setRoom(Room room) { this.room = room; }

    public T getState() { return state; }

    public void setState(T state) { this.state = state; }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", room=" + room +
                ", state=" + state +
                ", meta=" + meta +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineActionDevice device = (RoutineActionDevice) o;
        return Objects.equals(id, device.id) &&
                Objects.equals(name, device.name) &&
                Objects.equals(type, device.type) &&
                Objects.equals(room, device.room) &&
                Objects.equals(state, device.state) &&
                Objects.equals(meta, device.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, room, state, meta);
    }
}
