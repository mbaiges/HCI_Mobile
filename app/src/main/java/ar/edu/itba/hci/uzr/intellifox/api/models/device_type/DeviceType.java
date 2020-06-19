package ar.edu.itba.hci.uzr.intellifox.api.models.device_type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceType {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("powerUasge")
    @Expose
    private String powerUasge;

    public DeviceType() {
    }

    public DeviceType(String name) {
        this.name = name;
    }

    public DeviceType(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "DeviceType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
