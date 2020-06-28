package ar.edu.itba.hci.uzr.intellifox.api.models.device_type;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class DeviceType {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("powerUsage")
    @Expose
    private Double powerUsage;

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

    public Double getPowerUsage() {
        return powerUsage;
    }

    public void setPowerUsage(Double powerUsage) {
        this.powerUsage = powerUsage;
    }

    @Override
    public String toString() {
        return "DeviceType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceType that = (DeviceType) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(powerUsage, that.powerUsage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, powerUsage);
    }
}
