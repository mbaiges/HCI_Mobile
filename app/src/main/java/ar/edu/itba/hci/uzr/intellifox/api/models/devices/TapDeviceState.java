package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class TapDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TapDeviceState that = (TapDeviceState) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }
}
