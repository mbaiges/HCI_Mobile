package ar.edu.itba.hci.uzr.intellifox.wrappers;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class TypeAndDeviceId {
    @SerializedName("typeName")
    private String typeName;

    @SerializedName("deviceId")
    private String deviceId;

    public TypeAndDeviceId(String typeName, String deviceId) {
        this.typeName = typeName;
        this.deviceId = deviceId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeAndDeviceId that = (TypeAndDeviceId) o;
        return typeName.equals(that.typeName) &&
                deviceId.equals(that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, deviceId);
    }
}
