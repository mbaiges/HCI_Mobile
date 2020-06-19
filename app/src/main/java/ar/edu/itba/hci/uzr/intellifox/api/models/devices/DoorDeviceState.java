package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class DoorDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lock")
    @Expose
    private String lock;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
