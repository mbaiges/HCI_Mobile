package ar.edu.itba.hci.uzr.intellifox.api.models.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RoomMeta {

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("location")
    @Expose(serialize = false)
    private RoomMetaLocation location;

    public RoomMeta(String desc, String icon) {
        this.desc = desc;
        this.icon = icon;
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public RoomMetaLocation getLocation() {
        return location;
    }

    public void setLocation(RoomMetaLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RoomMeta{" +
                "desc='" + desc + '\'' +
                ", icon='" + icon + '\'' +
                ", location=" + location +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomMeta roomMeta = (RoomMeta) o;
        return Objects.equals(desc, roomMeta.desc) &&
                Objects.equals(icon, roomMeta.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desc, icon);
    }
}
