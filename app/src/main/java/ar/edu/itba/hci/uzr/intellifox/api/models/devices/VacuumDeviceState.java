package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class VacuumDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("batteryLever")
    @Expose
    private Integer batteryLever;
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

    public Integer getBatteryLever() {
        return batteryLever;
    }

    public void setBatteryLever(Integer batteryLever) {
        this.batteryLever = batteryLever;
    }

    public VacuumLocation getLocation() {
        return location;
    }

    public void setLocation(VacuumLocation location) {
        this.location = location;
    }
}
