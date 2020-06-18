package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;

public class Device {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private DeviceType type;
    @SerializedName("meta")
    @Expose
    private DeviceMeta meta;

    public Device() {
    }

    public Device(String name, DeviceType type, DeviceMeta meta) {
        this.name = name;
        this.type = type;
        this.meta = meta;
    }

    public Device(String id, DeviceType type, String name, DeviceMeta meta) {
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

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", meta=" + meta +
                '}';
    }
}