package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import java.util.Objects;

public class MinimumComparableDevice {
    private Device device;

    private String deviceId;
    private String deviceName;
    private String status;

    public MinimumComparableDevice(Device device) {
        this.device = device;
        this.deviceId = device.getId();
        this.deviceName = device.getName();
        if (this.device.getState() != null) {
            this.status = this.device.getState().getStatus();
        }
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.deviceId = device.getId();
        this.deviceName = device.getName();
        if (this.device.getState() != null) {
            this.status = this.device.getState().getStatus();
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinimumComparableDevice that = (MinimumComparableDevice) o;
        return deviceId.equals(that.deviceId) &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, deviceName, status);
    }
}
