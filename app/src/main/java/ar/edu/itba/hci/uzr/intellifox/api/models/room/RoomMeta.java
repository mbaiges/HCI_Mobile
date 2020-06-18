package ar.edu.itba.hci.uzr.intellifox.api.models.room;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RoomMeta {

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("icon")
    @Expose
    private String icon;

    public RoomMeta(String desc, String icon) {
        this.desc = desc;
        this.icon = icon;
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

    @Override
    public String toString() {
        return "RoomMeta{" +
                "desc='" + desc + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
