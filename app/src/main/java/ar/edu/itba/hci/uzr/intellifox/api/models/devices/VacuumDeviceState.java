package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class VacuumDeviceState extends DeviceState {
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("batteryLevel")
    @Expose
    private Integer batteryLevel;
    @SerializedName("location")
    @Expose
    private VacuumLocation location;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLever(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public VacuumLocation getLocation() {
        return location;
    }

    public void setLocation(VacuumLocation location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VacuumDeviceState that = (VacuumDeviceState) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(mode, that.mode) &&
                Objects.equals(batteryLevel, that.batteryLevel) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, mode, batteryLevel, location);
    }
}
